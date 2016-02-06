package k4unl.minecraft.Hydraulicraft.tileEntities.gow;

import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TilePortalFrame extends TileHydraulicBaseNoPower {


    private boolean hasSendPacket = true;
    private Location parentLocation;
    private int colorIndex = 0;


    @Override
    public void update() {
        super.update();
    }

    @Override
    public void validate() {

        super.validate();

    }


    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
        super.readFromNBT(tCompound);
        parentLocation = new Location(tCompound.getIntArray("parent"));
        colorIndex = tCompound.getInteger("dye");
    }

    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
        super.writeToNBT(tCompound);
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
        return getWorld().getBlockState(pos).getValue(Properties.ACTIVE);
    }

    public void setActive(boolean b) {
        getWorld().setBlockState(pos, getBlockType().getDefaultState().withProperty(Properties.ACTIVE, b));
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

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }
}
