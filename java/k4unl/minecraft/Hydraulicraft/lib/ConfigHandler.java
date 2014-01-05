package k4unl.minecraft.Hydraulicraft.lib;

import java.io.File;

import cpw.mods.fml.common.Loader;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
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
		//Ids.blockHydraulicPressureGauge.loadBlock(config, Names.blockHydraulicPressureGauge);
		//Ids.blockHydraulicPressureValve.loadBlock(config, Names.blockHydraulicPressureValve);
		Ids.blockHydraulicPressureVat.loadBlock(config, Names.blockHydraulicPressurevat);
		//Ids.blockHydraulicPiston.loadBlock(config, Names.blockHydraulicPiston);
		Ids.blockHydraulicWasher.loadBlock(config, Names.blockHydraulicWasher);
		Ids.blockHydraulicHose.loadBlock(config, Names.blockHydraulicHose);
		Ids.blockHydraulicMixer.loadBlock(config, Names.blockHydraulicMixer);
		Ids.blockHydraulicPressureWall.loadBlock(config, Names.blockHydraulicPressureWall);
		Ids.blockHydraulicHarvester.loadBlock(config, Names.blockHydraulicHarvester);
		
		
		Ids.blockFluidOil.loadBlock(config, Names.fluidOil);
		
		Ids.oreCopper.loadBlock(config, Names.oreCopper);
		Ids.oreLead.loadBlock(config, Names.oreLead);
		
		Ids.itemChunks.loadItem(config, Names.itemChunk);
		Ids.itemDebug.loadItem(config, Names.itemDebugger);
		Ids.itemDusts.loadItem(config, Names.itemDust);
		Ids.itemFrictionPlate.loadItem(config, Names.itemFrictionPlate);
		Ids.itemGasket.loadItem(config, Names.itemGasket);
		
		loadThirdPartyIds();
	}
	
	private static void loadThirdPartyIds(){
		if(Loader.isModLoaded("MineMaarten_PneumaticCraft")){
			Ids.blockHydraulicPneumaticCompressor.loadBlock(config, Names.blockHydraulicPneumaticCompressor);
		}
	}
}
