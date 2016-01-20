package k4unl.minecraft.Hydraulicraft.multipart;


import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import mcmultipart.multipart.*;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

public class PartFluidInterface extends Multipart implements IFluidHandler, IOccludingPart, ISlottedPart, ITickable {
    private static float      pixel        = 1.0F / 16F;
    private static int        expandBounds = -1;
    private        EnumFacing side         = EnumFacing.DOWN;

    public static AxisAlignedBB[] boundingBoxes = new AxisAlignedBB[6];

    private FluidTank tank = new FluidTank(1000);

    static {
        //boundingBoxes[6] = new AxisAlignedBB(center - w, center - w, center - w, center + w, center + w, center + w);

        float thickness = 2 * pixel;
        float width = 8 * pixel;
        float min = 0.5F - (width/2);
        float max = 0.5F + (width/2);
        float tMax = 1.0F - thickness;
        float tMin = 0.0F + thickness;
        Vector3fMax vector;
        int i = 0;
        for (EnumFacing dir : EnumFacing.VALUES) {

            if(dir.equals(EnumFacing.UP)){
                vector = new Vector3fMax(min, tMax, min, max, 1.0F, max);
            }else if(dir.equals(EnumFacing.DOWN)){
                vector = new Vector3fMax(min, 0.0F, min, max, tMin, max);
            }else if(dir.equals(EnumFacing.NORTH)){
                vector = new Vector3fMax(min, min, 0.0F, max, max, tMin);
            }else if(dir.equals(EnumFacing.SOUTH)){
                vector = new Vector3fMax(min, min, tMax, max, max, 1.0F);
            }else if(dir.equals(EnumFacing.WEST)){
                vector = new Vector3fMax(0.0F, min, min, tMin, max, max);
            }else if(dir.equals(EnumFacing.EAST)){
                vector = new Vector3fMax(tMax, min, min, 1.0F, max, max);
            }else{
                vector = new Vector3fMax(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            boundingBoxes[i] = new AxisAlignedBB(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
            i++;
        }
    }

    private boolean isRedstonePowered;

    public void preparePlacement(EnumFacing side_) {

        side = side_;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        if (!from.equals(side))
            return 0;
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {

        if (!from.equals(side))
            return null;

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        if (!from.equals(side))
            return null;

        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

        return from.equals(side);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {

        return from.equals(side);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        return new FluidTankInfo[] { new FluidTankInfo(tank) };
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        super.addCollisionBoxes(mask, list, collidingEntity);
        for(int i = 0; i< 6; i++) {
            list.add(boundingBoxes[i]);
        }
    }

    @Override
    public boolean occlusionTest(IMultipart part) {
        return OcclusionHelper.defaultOcclusionTest(this, part);
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        addCollisionBoxes(null, list, null);
    }



    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        //connectedSides = new HashMap<EnumFacing, TileEntity>();
        //getHandler().updateNetworkOnNextTick(oldPressure);
        //checkConnectedSides();
        //readConnectedSidesFromNBT(tagCompound);
        side = EnumFacing.byName(tag.getString("side"));
        tank.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        super.writeToNBT(tagCompound);
        //writeConnectedSidesToNBT(tagCompound);
        //tagCompound.setDouble("fluidStored", fluidStored);
        tagCompound.setString("side", side.name());
        tank.writeToNBT(tagCompound);
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);

        NBTTagCompound mainCompound = new NBTTagCompound();
        if(getWorld() != null && !getWorld().isRemote){
            mainCompound.setString("side", side.getName());
            tank.writeToNBT(mainCompound);
        }

        buf.writeNBTTagCompoundToBuffer(mainCompound);
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);
        NBTTagCompound mainCompound = null;
        try {
            mainCompound = buf.readNBTTagCompoundFromBuffer();
            side = EnumFacing.byName(mainCompound.getString("side"));
            tank.readFromNBT(mainCompound);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    public void onAdded(){
        super.onAdded();
        if(!getWorld().isRemote) {
            MultipartHandler.updateMultiPart(this);
        }
    }*/

    
    @Override
    public void update(){

        if(!getWorld().isRemote && tank != null && tank.getFluid() != null) {
            //Find a fluid pipe

            if (MultipartHandler.hasPartFluidPipe(getContainer())) {
                PartFluidPipe pipe = MultipartHandler.getFluidPipe(getContainer());
                if (pipe.getFluidStored() == null) {
                    pipe.setFluidStored(tank.getFluid().getFluid());
                }
                if (pipe.getFluidAmountStored() < Constants.MAX_FLUID_TRANSFER_T * 10) {
                    double fluidAdded = (Constants.MAX_FLUID_TRANSFER_T * 10) - pipe.getFluidAmountStored();
                    if (tank.drain((int) fluidAdded, false).amount >= (int) fluidAdded) {
                        pipe.addFluid(fluidAdded);
                        tank.drain((int) fluidAdded, true);
                    }

                }
            }
        }
        if(!getWorld().isRemote){
            if (getIsRedstonePowered()){
                //Find a tank on the side we're on.
                Location tankLocation = new Location(getPos(), side);
                TileEntity te = tankLocation.getTE(getWorld());
                if(te instanceof IFluidHandler){
                    if(tank.getFluid() == null){
                        drainFluid((IFluidHandler)te);
                    } else if(tank.getFluid() != null){
                        if(tank.getFluidAmount() == 0){
                            drainFluid((IFluidHandler) te);
                        }else{
                            FluidTankInfo tankInfo = ((IFluidHandler)te).getTankInfo(side.getOpposite())[0];
                            if(tankInfo != null && tankInfo.fluid != null) {
                                if (tank.getFluid().getFluid() == tankInfo.fluid.getFluid()) {
                                    drainFluid((IFluidHandler) te);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void drainFluid(IFluidHandler fluidHandler){
        FluidStack drained = fluidHandler.drain(side.getOpposite(), Constants.MAX_FLUID_TRANSFER_T, false);
        if(drained != null && drained.amount > 0) {
            int filled = tank.fill(drained, false);
            if(filled > 0){
                //Do the actual filling.
                drained = fluidHandler.drain(side.getOpposite(), filled, true);
                tank.fill(drained, true);
            }
        }
    }

    public void checkRedstonePower() {
        /*boolean isIndirectlyPowered = getWorld().isBlockIndirectlyGettingPowered(getPos());
        if(isIndirectlyPowered && !getIsRedstonePowered()){
            setRedstonePowered(true);
        }else if(getIsRedstonePowered() && !isIndirectlyPowered){
            setRedstonePowered(false);
        }*/
    }

    public boolean getIsRedstonePowered() {
        return isRedstonePowered;
    }

    public void setRedstonePowered(boolean isRedstonePowered) {
        this.isRedstonePowered = isRedstonePowered;
    }


    public void onNeighborChanged(){
        if(!getWorld().isRemote){
            checkRedstonePower();
        }
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.getFaceSlot(side));
    }




}
