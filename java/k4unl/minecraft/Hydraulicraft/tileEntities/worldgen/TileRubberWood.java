package k4unl.minecraft.Hydraulicraft.tileEntities.worldgen;

import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

/**
 * @author Koen Beckers (K-4U)
 */
public class TileRubberWood extends TileEntity {

    private int rubberInside = HCConfig.INSTANCE.getInt("maxRubberInTree");

    public void randomTick() {
        //Refil the rubber!

        if (rubberInside < HCConfig.INSTANCE.getInt("maxRubberInTree")) {
            Log.info(getPos().toString());
            Log.info("Rubber was " + rubberInside);
            int newValue = new Random().nextInt(HCConfig.INSTANCE.getInt("maxRubberInTree"));
            while (newValue < rubberInside && newValue > 0) {
                newValue = new Random().nextInt(HCConfig.INSTANCE.getInt("maxRubberInTree"));
            }
            rubberInside = newValue;
            Log.info("Rubber is now " + rubberInside);
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
}
