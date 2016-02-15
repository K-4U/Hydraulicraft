package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHarvester;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

public class TileHydraulicPiston extends TileHydraulicBaseNoPower {

    private float oldExtendedLength;
    private float extendedLength;
    private float   maxLength       = 4F;
    private float   extendTarget    = 0F;
    private float   movingSpeed     = 0.05F;
    private float   movingSpeedBack = 0.1F;
    private boolean harvesterPart   = false;


    private boolean isRetracting;
    private EnumFacing facing = EnumFacing.NORTH;
    private Location               harvesterLocation;
    private TileHydraulicHarvester harvester;

    public TileHydraulicPiston() {

        super();
    }

    public float getExtendTarget() {

        return extendTarget;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        extendedLength = tagCompound.getFloat("extendedLength");
        maxLength = tagCompound.getFloat("maxLength");
        extendTarget = tagCompound.getFloat("extendTarget");
        harvesterPart = tagCompound.getBoolean("harvesterPart");
        isRetracting = tagCompound.getBoolean("isMoving");
        oldExtendedLength = tagCompound.getFloat("oldExtendedLength");
        movingSpeed = tagCompound.getFloat("movingSpeed");
        movingSpeedBack = tagCompound.getFloat("movingSpeedBack");

        harvesterLocation = new Location(tagCompound.getIntArray("harvesterLocation"));
        facing = EnumFacing.byName(tagCompound.getString("facing"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        tagCompound.setFloat("extendedLength", extendedLength);
        tagCompound.setFloat("maxLength", maxLength);
        tagCompound.setFloat("extendTarget", extendTarget);
        tagCompound.setBoolean("harvesterPart", harvesterPart);
        tagCompound.setBoolean("isMoving", isRetracting);
        tagCompound.setFloat("oldExtendedLength", oldExtendedLength);
        if (harvesterLocation != null) {
            tagCompound.setIntArray("harvesterLocation", harvesterLocation.getLocation());
        }
        tagCompound.setFloat("movingSpeed", movingSpeed);
        tagCompound.setFloat("movingSpeedBack", movingSpeedBack);
        tagCompound.setString("facing", facing.getName());
    }

    public float getExtendedLength() {

        return extendedLength;
    }


    public float getMaxLength() {

        return maxLength;
    }

    public void setIsHarvesterPart(boolean isit, Location _harvesterLocation) {

        harvesterLocation = _harvesterLocation;
        harvesterPart = isit;
        worldObj.markBlockForUpdate(getPos());
    }

    public void setIsHarvesterPart(boolean isit) {

        harvesterPart = isit;
        worldObj.markBlockForUpdate(getPos());
    }

    public void setMaxLength(float newMaxLength) {

        maxLength = newMaxLength;
        worldObj.markBlockForUpdate(getPos());
    }

    public boolean getIsHarvesterPart() {

        return harvesterPart;
    }

    public void extendTo(float blocksToExtend) {

        if (blocksToExtend > maxLength) {
            blocksToExtend = maxLength;
        }
        if (blocksToExtend < 0) {
            blocksToExtend = 0;
        }

        extendTarget = blocksToExtend;

        int compResult = Float.compare(extendTarget, extendedLength);
        if (compResult > 0) {
            isRetracting = false;
        } else if (compResult < 0) {
            isRetracting = true;
        }

        worldObj.markBlockForUpdate(getPos());
    }

    //Called from harvester
    public float workFunction(boolean simulate, EnumFacing from) {

        oldExtendedLength = extendedLength;

        int compResult = Float.compare(extendTarget, extendedLength);
        if (!simulate) {
            recheckSpeed();
            if (compResult > 0 && !isRetracting) {
                extendedLength += movingSpeed;
            } else if (compResult < 0 && isRetracting) {
                extendedLength -= movingSpeedBack;
            } else {
                extendedLength = extendTarget;
                oldExtendedLength = extendedLength;
            }
            worldObj.markBlockForUpdate(getPos());
        }

        if (compResult >= 0) {
            return Constants.PRESSURE_USAGE_PISTON;
        }
        return 0;
    }

    private IHarvester getHarvester() {

        if (harvesterPart && harvester == null && harvesterLocation != null) {
            harvester = (TileHydraulicHarvester) harvesterLocation.getTE(getWorld());
        }
        return harvester;
    }


    private void recheckSpeed() {

        if (getHarvester() != null) {
            TileHydraulicHarvester harv = (TileHydraulicHarvester) getHarvester();

            float pressurePercentage = harv.getPressure(EnumFacing.UP) / harv.getMaxPressure(harv.isOilStored(), EnumFacing.UP);
            float factor = (harv.getPressureTier().ordinal() + 1) * (harv.isOilStored() ? 3.0F : 1.0F) * pressurePercentage;
            movingSpeed = 0.05F * factor;
            movingSpeedBack = 0.1F * factor;
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        float extendedLength = getExtendedLength();
        float minX = 0.0F + getPos().getX();
        float minY = 0.0F + getPos().getY();
        float minZ = 0.0F + getPos().getZ();
        float maxX = 1.0F + getPos().getX();
        float maxY = 1.0F + getPos().getY();
        float maxZ = 1.0F + getPos().getZ();

        EnumFacing dir = (EnumFacing) getWorld().getBlockState(getPos()).getValue(Properties.ROTATION);
        minX += extendedLength * (dir.getFrontOffsetX() < 0 ? dir.getFrontOffsetX() : 0);
        minY += extendedLength * (dir.getFrontOffsetY() < 0 ? dir.getFrontOffsetY() : 0);
        minZ += extendedLength * (dir.getFrontOffsetZ() < 0 ? dir.getFrontOffsetZ() : 0);

        maxX += extendedLength * (dir.getFrontOffsetX() > 0 ? dir.getFrontOffsetX() : 0);
        maxY += extendedLength * (dir.getFrontOffsetY() > 0 ? dir.getFrontOffsetY() : 0);
        maxZ += extendedLength * (dir.getFrontOffsetZ() > 0 ? dir.getFrontOffsetZ() : 0);

        return AxisAlignedBB.fromBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public float getOldExtendedLength() {

        return oldExtendedLength;
    }

    public EnumFacing getFacing() {

        return facing;
    }

    public void setFacing(EnumFacing nFacing) {

        facing = nFacing;
    }

    public void setHarvester(TileHydraulicHarvester harvester) {

        this.harvester = harvester;
    }
}
