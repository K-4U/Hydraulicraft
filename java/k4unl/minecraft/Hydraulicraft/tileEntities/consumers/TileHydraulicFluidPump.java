package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileHydraulicFluidPump extends TileHydraulicBase implements IHydraulicConsumer, IFluidHandler {

    private float   requiredPressure  = 5.0F;
    private int     MAX_DISTANCE      = 30; //30 blocks away..
    private int     fluidHandlersNear = 0;
    private boolean hasPumped         = false;
    private FluidTank tank;
    private EnumFacing facing = EnumFacing.NORTH;
    private float rotational;
    private float oldRotational;

    private Fluid          fluidPumping;
    private Block          blockPumping;
    private List<Location> blocksToScan;
    private List<Location> fluidBlocks;
    private boolean        hasPumped_old;


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
        for (EnumFacing dir : EnumFacing.VALUES) {
            Location toCheck = toScan.getNewOffset(dir, 1);
            if (!toCheck.isInList(fluidBlocks) && !toCheck.isInList(blocksToScan)) {
                if (toCheck.getDifference(getBlockLocation()) < MAX_DISTANCE) {
                    if (toCheck.getBlock(getWorldObj()) == blockPumping) {
                        if (toCheck.getBlock(getWorldObj()) instanceof BlockLiquid) {
                            //We possibly need to do more here..
                            blocksToScan.add(toScan.getNewOffset(dir, 1));
                        } else if (toCheck.getBlock(getWorldObj()) instanceof IFluidBlock) {

                            int check = Float.compare(((IFluidBlock) toCheck.getBlock(getWorldObj())).getFilledPercentage(getWorldObj(), toCheck.toBlockPos()), 1.0F);
                            if (check == 0 && ((IFluidBlock) toCheck.getBlock(getWorldObj())).canDrain(getWorldObj(), toCheck.toBlockPos())) {
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
        if (fluidPumping == null) return false;

        fluidHandlersNear = 0;
        boolean hasFluidHandlerNear = false;
        for (EnumFacing dir : EnumFacing.VALUES) {
            TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
            if (shouldIFillThis instanceof IFluidHandler) {
                if (((IFluidHandler) shouldIFillThis).canFill(dir.getOpposite(), fluidPumping)) {
                    hasFluidHandlerNear = true;
                    fluidHandlersNear++;
                }
            }
        }
        return getPressure(EnumFacing.UP) > requiredPressure && fluidPumping != null && hasFluidHandlerNear && fluidBlocks.size() > 0;
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (canRun()) {
            if (!simulate) {
                doPump();
            }
            //The higher the pressure
            //The higher the speed!
            //But also the more it uses..
            //TODO: Fix this goddamn algorithm so that it makes more sense.
            if (hasPumped) {
                return 0.5F + (getPressure(EnumFacing.UP) * 0.00005F);
            } else {
                return 0.0000001F;
            }
        } else {
            return 0F;
        }
    }

    @Override
    public void update() {

        super.update();
        if (worldObj.isRemote) {
            if (hasPumped)
                rotational += 0.01F;
            if (rotational >= 1.0F) {
                rotational = 0.0F;
            }
        }

        //See if we need to scan blocks
        if (!worldObj.isRemote) {
            if (hasPumped != hasPumped_old) {
                hasPumped_old = hasPumped;
                getHandler().updateBlock();
            }

            //If the ammount of fluid blocks we have isn't filled yet, and we have no list to scan, we're either done
            //Or we haven't started yet.
            //If the fluidPumping block is not null, that means we have found a block earlier.
            if (fluidPumping == null) {
                hasPumped = false;
                Block block = getWorldObj().getBlockState(new BlockPos(getPos().getX() + (facing.getFrontOffsetX() * 2), getPos().getY() - 1, getPos().getZ() + (facing.getFrontOffsetZ() * 2))).getBlock();
                Fluid blockFluid = FluidRegistry.lookupFluidForBlock(block);
                if ((block == Blocks.flowing_lava || block == Blocks.lava) && blockFluid == null) {
                    fluidPumping = FluidRegistry.LAVA;
                    blockPumping = Blocks.flowing_lava;
                }
                if ((block == Blocks.flowing_water || block == Blocks.water) && blockFluid == null) {
                    fluidPumping = FluidRegistry.WATER;
                    blockPumping = Blocks.flowing_water;
                }

                if (block instanceof IFluidBlock) {
                    if (((IFluidBlock) block).canDrain(getWorldObj(), new BlockPos(getPos().getX() + (facing.getFrontOffsetX() * 2), getPos().getY() - 1, getPos().getZ() + (facing.getFrontOffsetZ() * 2)))) {
                        //Now, we also need to grab all the blocks that are connected to this block, and pump from the one furthest away.
                        fluidPumping = ((IFluidBlock) block).getFluid();
                        blockPumping = block;
                    }
                } else if (blockFluid != null && block != null) {
                    //if(getWorldObj().getBlockMetadata(xCoord + (facing.offsetX * 2), yCoord - 1, zCoord + (facing.offsetZ * 2)) == 0){
                    fluidPumping = blockFluid;
                    blockPumping = block;
                    //}
                }
            }
            if (fluidBlocks.size() == 0 && blocksToScan.size() == 0 && fluidPumping != null) {
                blocksToScan.add(new Location(getPos().getX() + (facing.getFrontOffsetX() * 2), getPos().getY() - 1, getPos().getZ() + (facing.getFrontOffsetZ() * 2)));
            }
            if (blocksToScan.size() > 0) {
                //Log.info("Tick");
                List<Location> copyList = new ArrayList<Location>(blocksToScan);
                for (Location loc : copyList) {
                    scanFluidBlocks(loc);
                }
                if (blocksToScan.size() == 0) {
                    //Log.info("Done with scanning. " + fluidBlocks.size() + " blocks found!");
                }
            }
        }
    }

    private void doPump() {
        //TODO: Fix me up with the pressure.
        int toFill = HCConfig.INSTANCE.getInt("waterPumpPerTick");
        if (!(tank.fill(new FluidStack(fluidPumping, toFill), false) < toFill)) {
            tank.fill(new FluidStack(fluidPumping, toFill), true);
            hasPumped = true;
            //We cannot do anything
        } else {
            hasPumped = false;
        }


        if (tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME) {
            //Remove a block from the list.
            //Todo: Remove a block furthest away first
            Location toDrain = fluidBlocks.get(fluidBlocks.size() - 1);

            if (!(toDrain.getBlock(getWorldObj()) instanceof IFluidBlock)) {
                if (!(toDrain.getBlock(getWorldObj()) instanceof BlockLiquid)) {
                    if (toDrain.compare(getPos().getX() + (facing.getFrontOffsetX() * 2), getPos().getY() - 1, getPos().getZ() + (facing.getFrontOffsetZ() * 2))) {
                        //Done pumping.
                        fluidPumping = null;
                    }
                    return;
                }

            }
            if (toDrain.getBlock(getWorldObj()) instanceof IFluidBlock) {
                IFluidBlock theBlock = (IFluidBlock) toDrain.getBlock(getWorldObj());
                theBlock.drain(getWorldObj(), toDrain.toBlockPos(), true);
                fluidBlocks.remove(fluidBlocks.size() - 1);
                hasPumped = true;
            } else if (toDrain.getBlock(getWorldObj()) instanceof BlockLiquid) {
                if (!(toDrain.getBlock(getWorldObj()) == Blocks.water)) {
                    getWorldObj().setBlockState(toDrain.toBlockPos(), Blocks.air.getDefaultState(), 2);
                    fluidBlocks.remove(fluidBlocks.size() - 1);
                }
                hasPumped = true;
            }

            for (EnumFacing dir : EnumFacing.VALUES) {
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
        setFacing(EnumFacing.byName(tagCompound.getString("facing")));
        hasPumped = tagCompound.getBoolean("hasPumped");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        tank.writeToNBT(tagCompound);
        tagCompound.setString("facing", getFacing().getName());
        tagCompound.setBoolean("hasPumped", hasPumped);
    }

    @Override
    public void validate() {

        super.validate();
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return true;
    }

    @Override
    public boolean canWork(EnumFacing dir) {

        return getNetwork(dir) != null && dir.equals(EnumFacing.UP);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource,
                            boolean doDrain) {

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {

        return fluid.equals(FluidRegistry.WATER);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        return new FluidTankInfo[]{new FluidTankInfo(tank)};
    }

    public EnumFacing getFacing() {

        return facing;
    }

    public void setFacing(EnumFacing nFacing) {

        facing = nFacing;
    }

    public double getRotational(float f) {


        float diff = Math.abs(rotational - oldRotational) * f;
        oldRotational = rotational;
        if (hasPumped)
            return rotational + diff;
        return rotational;
    }
}
