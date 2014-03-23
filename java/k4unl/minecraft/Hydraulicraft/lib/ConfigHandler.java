package k4unl.minecraft.Hydraulicraft.lib;

import java.io.File;

import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import net.minecraftforge.common.config.Configuration;

/*!
 * @author Koen Beckers
 * @date 13-12-2013
 * The Class that handles all of the config stuff.
 * (it's early, don't expect any good documentation)
 */
public class ConfigHandler{
    private static Configuration config;

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Initializes the config. Loads the config
     */
    public static void init(File configFile){
        config = new Configuration(configFile);
        config.load();

        //Wow.. Configs don't have to do that much anymore O_o
        Config.loadConfigOptions(config);
        
        if(config.hasChanged()) {
            config.save();
        }
    }
}
