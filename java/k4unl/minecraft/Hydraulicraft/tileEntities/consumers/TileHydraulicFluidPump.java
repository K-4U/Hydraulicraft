package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileHydraulicFluidPump extends TileHydraulicBase implements IHydraulicConsumer, IFluidHandler {

    private float requiredPressure  = 5.0F;
    private int   MAX_DISTANCE      = 30; //30 blocks away..
    private int   fluidHandlersNear = 0;
	private boolean hasPumped = false;
    private FluidTank tank;
    private ForgeDirection facing = ForgeDirection.NORTH;
    private float rotational;
    private float oldRotational;

    private Fluid fluidPumping;
	private Block blockPumping;
    private List<Location> blocksToScan;
    private List<Location> fluidBlocks;


    public TileHydraulicFluidPump() {

        super(2);
        super.init(this);
        tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
        blocksToScan = new ArrayList<Location>();
        fluidBlocks = new ArrayList<Location>();

    }

    private void scanFluidBlocks(Location toScan) {
        blocksToScan.remove(toScan);
        fluidBlocks.add(toScan);
        //Log.info("Scanning " + toScan.printCoords());
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            Location toCheck = toScan.getNewOffset(dir, 1);
            if (!toCheck.isInList(fluidBlocks) && !toCheck.isInList(blocksToScan)){
                if(toCheck.getDifference(getBlockLocation()) < MAX_DISTANCE){
                    if(toCheck.getBlock(getWorldObj()) == blockPumping) {
						if(toCheck.getBlock(getWorld()) instanceof BlockLiquid){
							//We possibly need to do more here..
							blocksToScan.add(toScan.getNewOffset(dir, 1));
						}else if(toCheck.getBlock(getWorld()) instanceof IFluidBlock) {

							int check = Float.compare(((IFluidBlock) toCheck.getBlock(getWorldObj())).getFilledPercentage(getWorldObj(), toCheck.getX(), toCheck
									.getY(), toCheck.getZ()), 1.0F);
							if (check == 0 && ((IFluidBlock) toCheck.getBlock(getWorldObj())).canDrain(getWorldObj(), toCheck.getX(), toCheck.getY(),
									toCheck.getZ())) {
								blocksToScan.add(toScan.getNewOffset(dir, 1));
							}
						}
                    }
                }
            }
        }
    }

    private boolean canRun() {
        //See if it's either a y level above fluid or another screw
        //First we need to grab the block one y level down, and 2 to the facing-direction
        if(fluidPumping == null) return false;

		fluidHandlersNear = 0;
		boolean hasFluidHandlerNear = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
			if(shouldIFillThis instanceof IFluidHandler){
				if(((IFluidHandler)shouldIFillThis).canFill(dir.getOpposite(), fluidPumping)){
					hasFluidHandlerNear = true;
					fluidHandlersNear++;
				}
			}
		}
		return getPressure(ForgeDirection.UNKNOWN) > requiredPressure && fluidPumping != null && hasFluidHandlerNear && fluidBlocks.size() > 0;
	}
	
	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun()) {
			if (!simulate) {
				doPump();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			//TODO: Fix this goddamn algorithm so that it makes more sense.
			if (hasPumped){
				hasPumped = false;
				return 0.5F + (getPressure(ForgeDirection.UNKNOWN) * 0.00005F);
			}else {
				return 0.0000001F;
			}
		}else{
			return 0F;
		}
	}

	@Override
	public void updateEntity(){
		super.updateEntity();
		if(worldObj.isRemote){
			rotational+=0.01F;
			if(rotational >= 1.0F){
				rotational = 0.0F;
			}
		}

        //See if we need to scan blocks
        if(!worldObj.isRemote){
            //If the ammount of fluid blocks we have isn't filled yet, and we have no list to scan, we're either done
            //Or we haven't started yet.
            //If the fluidPumping block is not null, that means we have found a block earlier.
            if(fluidPumping == null){
                Block block = getWorldObj().getBlock(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2));
				Fluid blockFluid = FluidRegistry.lookupFluidForBlock(block);
				if((block == Blocks.flowing_lava || block == Blocks.lava) && blockFluid == null){
					fluidPumping = FluidRegistry.LAVA;
					blockPumping = Blocks.flowing_lava;
				}
				if((block == Blocks.flowing_water || block == Blocks.water) && blockFluid == null){
					fluidPumping = FluidRegistry.WATER;
					blockPumping = Blocks.flowing_water;
				}

                if(block instanceof IFluidBlock){
					if(((IFluidBlock)block).canDrain(getWorldObj(), xCoord+(facing.offsetX * 2),yCoord - 1, zCoord + (facing.offsetZ * 2))) {
						//Now, we also need to grab all the blocks that are connected to this block, and pump from the one furthest away.
						fluidPumping = ((IFluidBlock)block).getFluid();
						blockPumping = block;
					}
                }else if(blockFluid != null && block != null){
					if(getWorld().getBlockMetadata(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2)) == 0){
						fluidPumping = blockFluid;
						blockPumping = block;
					}
				}
            }
            if(fluidBlocks.size() == 0 && blocksToScan.size() == 0 && fluidPumping != null){
                blocksToScan.add(new Location(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2)));
            }
            if(blocksToScan.size() > 0){
                //Log.info("Tick");
                List<Location> copyList = new ArrayList<Location>(blocksToScan);
                for(Location loc : copyList){
                    scanFluidBlocks(loc);
                }
                if(blocksToScan.size() == 0){
                    Log.info("Done with scanning. " + fluidBlocks.size() + " blocks found!");
                }
            }
        }
	}

	private void doPump() {
        //TODO: Fix me up with the pressure.
		int toFill = HCConfig.INSTANCE.getInt("waterPumpPerTick");
        if(!(tank.fill(new FluidStack(fluidPumping, toFill), false) < toFill)){
			tank.fill(new FluidStack(fluidPumping, toFill), true);
			hasPumped = true;
            //We cannot do anything
        }else{
			hasPumped = false;
		}


        if(tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
            //Remove a block from the list.
            //Todo: Remove a block furthest away first
            Location toDrain = fluidBlocks.get(fluidBlocks.size()-1);

            if(!(toDrain.getBlock(getWorldObj()) instanceof IFluidBlock)){
				if(!(toDrain.getBlock(getWorld()) instanceof BlockLiquid)){
					if (toDrain.compare(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2))) {
						//Done pumping.
						fluidPumping = null;
					}
					return;
				}

            }
			if (toDrain.getBlock(getWorld()) instanceof IFluidBlock) {
				IFluidBlock theBlock = (IFluidBlock)toDrain.getBlock(getWorldObj());
				theBlock.drain(getWorldObj(), toDrain.getX(), toDrain.getY(), toDrain.getZ(), true);
				fluidBlocks.remove(fluidBlocks.size()-1);
				hasPumped = true;
			}else if(toDrain.getBlock(getWorld()) instanceof BlockLiquid){
				if(!(toDrain.getBlock(getWorld()) == Blocks.water) && fluidBlocks.size() < 3) {
					getWorld().setBlock(toDrain.getX(), toDrain.getY(), toDrain.getZ(), Blocks.air, 0, 2);
					fluidBlocks.remove(fluidBlocks.size()-1);
				}
				hasPumped = true;
			}

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
                if (shouldIFillThis instanceof IFluidHandler) {
                    if (((IFluidHandler) shouldIFillThis).canFill(dir.getOpposite(), fluidPumping)) {
                        if (((IFluidHandler) shouldIFillThis).fill(dir.getOpposite(), new FluidStack(fluidPumping, FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
                            ((IFluidHandler) shouldIFillThis).fill(dir.getOpposite(), new FluidStack(fluidPumping, FluidContainerRegistry.BUCKET_VOLUME), true);
                            tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);

                        }
                    }
                }
            }
        }
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
        tank.readFromNBT(tagCompound);
        setFacing(ForgeDirection.getOrientation(tagCompound.getInteger("facing")));

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
        tank.writeToNBT(tagCompound);
        tagCompound.setInteger("facing", getFacing().ordinal());
	}

	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public void onFluidLevelChanged(int old) {}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean canWork(ForgeDirection dir) {
		return getNetwork(dir) != null && dir.equals(ForgeDirection.UP);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid.equals(FluidRegistry.WATER);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {

        return new FluidTankInfo[] {new FluidTankInfo(tank)};
	}

	public ForgeDirection getFacing(){
		return facing;
	}

	public void setFacing(ForgeDirection nFacing){
		facing = nFacing;
	}

	public double getRotational(float f) {

		float diff = Math.abs(rotational - oldRotational) * f;
		oldRotational = rotational;
		return rotational + diff;
	}
}
