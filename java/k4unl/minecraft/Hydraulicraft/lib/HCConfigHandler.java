package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.k4lib.lib.config.ConfigHandler;
import net.minecraftforge.common.config.Configuration;

/*!
 * @author Koen Beckers
 * @date 13-12-2013
 * The Class that handles all of the config stuff.
 * (it's early, don't expect any good documentation)
 */
public class HCConfigHandler extends ConfigHandler {
    private static Configuration config;


    public static void loadTank(){
        HCConfig.loadTankOptions(config);
        if(config.hasChanged()){
            config.save();
        }
    }
}
