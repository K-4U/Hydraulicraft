package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
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
    private FluidTank tank;
    private ForgeDirection facing = ForgeDirection.NORTH;
    private float rotational;
    private float oldRotational;

    private Block fluidPumping;
    private List<Location> blocksToScan;
    private List<Location> fluidBlocks;


    public TileHydraulicFluidPump() {

        super(PressureTier.LOWPRESSURE, 2);
        super.init(this);
        tank = new FluidTank(100);
        blocksToScan = new ArrayList<Location>();
        fluidBlocks = new ArrayList<Location>();

    }

    private void scanFluidBlocks(Location toScan) {
        blocksToScan.remove(toScan);
        fluidBlocks.add(toScan);
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (!fluidBlocks.contains(toScan.getNewOffset(dir, 1))) {
                if(toScan.getNewOffset(dir, 1).getDifference(getBlockLocation()) < MAX_DISTANCE){
                    if(toScan.getNewOffset(dir, 1).getBlock(getWorldObj()) == fluidPumping)
                    blocksToScan.add(toScan.getNewOffset(dir, 1));
                }
            }
        }
    }

    private boolean canRun() {
        //See if it's either a y level above fluid or another screw
        //First we need to grab the block one y level down, and 2 to the facing-direction

        Block block = getWorldObj().getBlock(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2));
        if(block instanceof IFluidBlock){
            //Now, we also need to grab all the blocks that are connected to this block, and pump from the one furthest away.
            fluidPumping = block;
        }

		fluidHandlersNear = 0;
		boolean hasFluidHandlerNear = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
			if(shouldIFillThis instanceof IFluidHandler){
				if(((IFluidHandler)shouldIFillThis).canFill(dir.getOpposite(), ((IFluidBlock)fluidPumping).getFluid())){
					hasFluidHandlerNear = true;
					fluidHandlersNear++;
				}
			}
		}
		return getPressure(ForgeDirection.UNKNOWN) > requiredPressure && fluidPumping != null && hasFluidHandlerNear && fluidBlocks.size() > 0;
	}
	
	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun()){
			if(!simulate){
				doPump();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
            //TODO: Fix this goddamn algorithm so that it makes more sense.

			return 5F + (getPressure(ForgeDirection.UNKNOWN) * 0.00005F);
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

            if(fluidBlocks.size() == 0 && blocksToScan.size() == 0 && fluidPumping != null){
                blocksToScan.add(new Location(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2)));
            }
            if(blocksToScan.size() > 0){
                for(Location loc : blocksToScan){
                    scanFluidBlocks(loc);
                }
            }
        }
	}

	private void doPump() {
        //TODO: Fix me up with the pressure.
		int toFill = HCConfig.INSTANCE.getInt("waterPumpPerTick");
        tank.fill(new FluidStack(((IFluidBlock)fluidPumping).getFluid(),toFill), true);

        if(tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
            //Remove a block from the list.
            //Todo: Remove a block furthest away first
            fluidBlocks.remove(fluidBlocks.size()-1);

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
                if (shouldIFillThis instanceof IFluidHandler) {
                    if (((IFluidHandler) shouldIFillThis).canFill(dir.getOpposite(), ((IFluidBlock) fluidPumping).getFluid())) {
                        if (((IFluidHandler) shouldIFillThis).fill(dir.getOpposite(), new FluidStack(((IFluidBlock) fluidPumping).getFluid(),
                          FluidContainerRegistry.BUCKET_VOLUME), false) == FluidContainerRegistry.BUCKET_VOLUME) {
                            ((IFluidHandler) shouldIFillThis).fill(dir.getOpposite(), new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME),
                              true);
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
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
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
		if(getNetwork(dir) == null){
			return false;
		}
		return dir.equals(ForgeDirection.UP);
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
		FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
		return tankInfo;
	}

	public ForgeDirection getFacing(){
		return facing;
	}

	public void setFacing(ForgeDirection nFacing){
		facing = nFacing;
        Log.info("Facing new: " + nFacing);
	}

	public double getRotational(float f) {
		float diff = Math.abs(rotational - oldRotational) * f;
		oldRotational = rotational;
		return rotational + diff;
	}
}
