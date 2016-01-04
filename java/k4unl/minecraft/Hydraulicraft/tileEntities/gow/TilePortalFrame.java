package k4unl.minecraft.Hydraulicraft.tileEntities.gow;

import k4unl.minecraft.Hydraulicraft.network.packets.PacketPortalStateChanged;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.network.NetworkHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TilePortalFrame extends TileHydraulicBaseNoPower {


    private boolean hasSendPacket = true;
    private boolean isActive;
    private Location parentLocation;
    private int colorIndex = 0;


    @Override
    public void update() {
        super.update();
        if (!getWorld().isRemote && !hasSendPacket) {
            hasSendPacket = true;
            NetworkHandler.sendToAllAround(new PacketPortalStateChanged(getPos(), isActive), getWorld());
        }
    }

    @Override
    public void validate() {

        super.validate();

    }


    public boolean isConnectedTo(EnumFacing dir) {

        Location thatLocation = new Location(getPos(), dir);
        return !(getWorld() == null) && (thatLocation.getTE(getWorld()) instanceof TilePortalFrame || thatLocation.getTE(getWorld()) instanceof TilePortalBase);
    }

    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
        super.readFromNBT(tCompound);
        isActive = tCompound.getBoolean("isActive");
        parentLocation = new Location(tCompound.getIntArray("parent"));
        colorIndex = tCompound.getInteger("dye");
    }

    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
        super.writeToNBT(tCompound);
        tCompound.setBoolean("isActive", isActive);
        if (parentLocation != null) {
            tCompound.setIntArray("parent", parentLocation.getIntArray());
        }
        tCompound.setInteger("dye", colorIndex);
    }

    public void setPortalBase(TilePortalBase tilePortalBase) {
        parentLocation = tilePortalBase.getBlockLocation();
        markDirty();
        getWorld().markBlockForUpdate(getPos());
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean b) {
        isActive = b;
        hasSendPacket = false;
        markDirty();
    }

    public Location getBlockLocation() {
        return new Location(getPos());
    }

    public TilePortalBase getBase() {
        return (TilePortalBase) parentLocation.getTE(getWorld());
    }

    public void dye(int i) {
        colorIndex = i;
        markDirty();
        getWorld().markBlockForUpdate(getPos());
    }

    public int getDye() {
        return colorIndex;
    }

}
