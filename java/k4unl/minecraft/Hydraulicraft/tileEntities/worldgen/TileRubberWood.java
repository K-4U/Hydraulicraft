package k4unl.minecraft.Hydraulicraft.tileEntities.worldgen;

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
            int newValue = 0;
            while (newValue < rubberInside) {
                newValue = new Random().nextInt(HCConfig.INSTANCE.getInt("maxRubberInTree"));
            }
            rubberInside = newValue;
        }
    }
}
