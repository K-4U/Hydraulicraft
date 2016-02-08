package k4unl.minecraft.Hydraulicraft.tileEntities.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;

/**
 * @author Koen Beckers (K-4U)
 */
public class TileFluidTank extends TileEntity implements IFluidHandler, ITickable {

    private boolean hasUpdated = false;
    private FluidTank fluidTank;

    public TileFluidTank() {

        this.fluidTank = new FluidTank(16 * FluidContainerRegistry.BUCKET_VOLUME);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        hasUpdated = true;
        return fluidTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {

        hasUpdated = true;
        return fluidTank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        hasUpdated = true;
        return fluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

        return from.equals(EnumFacing.DOWN) || from.equals(EnumFacing.UP);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {

        return from.equals(EnumFacing.DOWN) || from.equals(EnumFacing.UP);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        return new FluidTankInfo[] { fluidTank.getInfo() };
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {

        NBTTagCompound tagCompound = packet.getNbtCompound();
        readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 4, tagCompound);
    }


    @Override
    public void writeToNBT(NBTTagCompound compound) {

        super.writeToNBT(compound);
        fluidTank.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);
        fluidTank = fluidTank.readFromNBT(compound);

    }

    @Override
    public void update() {

        if (hasUpdated && worldObj.getTotalWorldTime() % 20 == 0) {
            hasUpdated = false;
            markDirty();
            worldObj.markBlockForUpdate(getPos());
        }
    }
}
