package k4unl.minecraft.Hydraulicraft.tileEntities.harvester;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileHarvesterFrame extends TileEntity {
    private boolean isRotated;

    public boolean getIsRotated() {
        return this.isRotated;
    }


    public void setRotated(boolean b) {
        this.isRotated = b;
        worldObj.markBlockForUpdate(getPos());
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        isRotated = tagCompound.getBoolean("isRotated");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("isRotated", isRotated);
    }
}
