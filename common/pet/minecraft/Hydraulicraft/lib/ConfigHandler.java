package pet.minecraft.Hydraulicraft.lib;

import java.io.File;

import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraftforge.common.Configuration;

/*!
 * @author Koen Beckers
 * @date 13-12-2013
 * The Class that handles all of the config stuff.
 * (it's early, don't expect any good documentation)
 */
public class ConfigHandler {
	private static Configuration config;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the config. Loads the config
	 */
	public static void init(File configFile){
		config = new Configuration(configFile);
		config.load();
		
		loadIds();
		
		if(config.hasChanged()){
			config.save();
		}
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Loads the Id's from the config file to the ID class.
	 */
	private static void loadIds(){
		Ids.blockHydraulicPump.loadBlock(config, Names.blockHydraulicPump);
	}
}
