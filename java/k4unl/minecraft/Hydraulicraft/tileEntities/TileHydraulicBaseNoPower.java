package k4unl.minecraft.Hydraulicraft.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileHydraulicBaseNoPower extends TileEntity implements ITickable {

    protected boolean isRedstonePowered;


    @Override
    public void update() {

        checkRedstonePower();
    }

    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);

    }

    @Override
    public void writeToNBT(NBTTagCompound tCompound) {

        super.writeToNBT(tCompound);

    }

    public void checkRedstonePower() {

        boolean isIndirectlyPowered = (getWorld().isBlockIndirectlyGettingPowered(getPos()) > 0);
        if (isIndirectlyPowered && !isRedstonePowered) {
            isRedstonePowered = true;
            redstoneChanged();
        } else if (!isIndirectlyPowered && isRedstonePowered) {
            isRedstonePowered = false;
            redstoneChanged();
        }
    }


    protected void redstoneChanged() {

    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

        NBTTagCompound tagCompound = packet.getNbtCompound();
        this.readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new SPacketUpdateTileEntity(getPos(), 4, tagCompound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {

        double xCoord = getPos().getX();
        double yCoord = getPos().getY();
        double zCoord = getPos().getZ();
        return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    public void markBlockForUpdate() {
        worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), worldObj.getBlockState(getPos()), 3);
        // TODO what does the flags: 3 mean? :P
    }

}
