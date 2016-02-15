package k4unl.minecraft.Hydraulicraft.tileEntities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ITickable;
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
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {

        NBTTagCompound tagCompound = packet.getNbtCompound();
        this.readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 4, tagCompound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {

        double xCoord = getPos().getX();
        double yCoord = getPos().getY();
        double zCoord = getPos().getZ();
        return AxisAlignedBB.fromBounds(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

}
