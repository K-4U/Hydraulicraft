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
		Ids.blockHydraulicFrictionIncinerator.loadBlock(config, Names.blockHydraulicFrictionIncinerator);
		Ids.blockHydraulicCrusher.loadBlock(config, Names.blockHydraulicCrusher);
		Ids.blockHydraulicPressureGauge.loadBlock(config, Names.blockHydraulicPressureGauge);
		Ids.blockHydraulicPressureValve.loadBlock(config, Names.blockHydraulicPressureValve);
		Ids.blockHydraulicPressurevat.loadBlock(config, Names.blockHydraulicPressurevat);
		Ids.blockHydraulicHose.loadBlock(config, Names.blockHydraulicHose);
		Ids.blockHydraulicPiston.loadBlock(config, Names.blockHydraulicPiston);
		Ids.blockHydraulicWasher.loadBlock(config, Names.blockHydraulicWasher);
		Ids.itemFrictionPlate.loadBlock(config, Names.itemFrictionPlate);
		
		Ids.oreCopper.loadBlock(config, Names.oreCopper);
		Ids.oreLead.loadBlock(config, Names.oreLead);
		
		Ids.itemGasket.loadItem(config, Names.itemGasket);
		Ids.itemFrictionPlate.loadItem(config, Names.itemFrictionPlate);
		Ids.ingotCopper.loadItem(config, Names.ingotCopper);
		Ids.ingotEnrichedCopper.loadItem(config, Names.ingotEnrichedCopper);
		Ids.ingotLead.loadItem(config, Names.ingotLead);
	}
}
