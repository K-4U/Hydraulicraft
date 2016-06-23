package k4unl.minecraft.Hydraulicraft.tileEntities.worldgen;

import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Random;

/**
 * @author Koen Beckers (K-4U)
 */
public class TileRubberWood extends TileEntity {

    private int rubberInside = HCConfig.INSTANCE.getInt("maxRubberInTree");

    public void randomTick() {
        //Refil the rubber!

        if (rubberInside < HCConfig.INSTANCE.getInt("maxRubberInTree")) {
            int newValue = new Random().nextInt(HCConfig.INSTANCE.getInt("maxRubberInTree"));
            //Log.info(getPos().toString());
            //Log.info("Rubber: " + rubberInside);
            while (newValue == 0) {
                newValue = new Random().nextInt(HCConfig.INSTANCE.getInt("maxRubberInTree"));
            }
            rubberInside += newValue;
            //Log.info("Rubber: " + rubberInside);
        }
    }

    public int drain(int toDrain, boolean simulate) {

        int drained = toDrain;
        if (rubberInside < drained) {
            drained = rubberInside;
        }
        if(!simulate){
            rubberInside = rubberInside - drained;
        }
        return drained;
    }

    public EnumFacing getFacing() {

        return getWorld().getBlockState(getPos()).getValue(Properties.ROTATION);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        super.readFromNBT(compound);
        rubberInside = compound.getInteger("rubberInside");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        compound = super.writeToNBT(compound);
        compound.setInteger("rubberInside", rubberInside);
        return compound;
    }

    public int getRubber() {

        return rubberInside;
    }

    public boolean hasSpot() {

        return getWorld().getBlockState(getPos()).getValue(Properties.HAS_RUBBER_SPOT);
    }
}
