package k4unl.minecraft.Hydraulicraft.lib;

import java.io.File;

import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraftforge.common.Configuration;

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

        loadIds();
        Config.loadConfigOptions(config);
        
        if(config.hasChanged()) {
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
        Ids.blockHydraulicLavaPump.loadBlock(config, Names.blockHydraulicLavaPump);
        Ids.blockHydraulicFrictionIncinerator.loadBlock(config, Names.blockHydraulicFrictionIncinerator);
        Ids.blockHydraulicCrusher.loadBlock(config, Names.blockHydraulicCrusher);
        Ids.blockHydraulicPressureVat.loadBlock(config, Names.blockHydraulicPressurevat);
        Ids.blockHydraulicPiston.loadBlock(config, Names.blockHydraulicPiston);
        Ids.blockHydraulicWasher.loadBlock(config, Names.blockHydraulicWasher);
        Ids.partHose.loadBlock(config, Names.partHose);
        Ids.blockHydraulicMixer.loadBlock(config, Names.blockHydraulicMixer);
        Ids.blockHydraulicPressureWall.loadBlock(config, Names.blockHydraulicPressureWall);
        Ids.blockHydraulicHarvester.loadBlock(config, Names.blockHydraulicHarvester);
        Ids.blockHarvesterTrolley.loadBlock(config, Names.blockHarvesterTrolley);
        Ids.blockPressureDisposal.loadBlock(config, Names.blockPressureDisposal);
        Ids.blockCore.loadBlock(config, Names.blockCore);
        Ids.blockValve.loadBlock(config, Names.blockValve);
        Ids.blockInterfaceValve.loadBlock(config, Names.blockInterfaceValve);
        
        Ids.partHose.loadBlock(config, Names.partHose);
        Ids.partValve.loadBlock(config, Names.partValve);
        
        Ids.blockFluidOil.loadBlock(config, Names.fluidOil);

        Ids.oreCopper.loadBlock(config, Names.oreCopper);
        Ids.oreLead.loadBlock(config, Names.oreLead);

        Ids.itemGasket.loadItem(config, Names.itemGasket);
        Ids.itemDebug.loadItem(config, Names.itemDebugger);
        Ids.itemFrictionPlate.loadItem(config, Names.itemFrictionPlate);
        Ids.ingotCopper.loadItem(config, Names.ingotCopper);
        Ids.ingotLead.loadItem(config, Names.ingotLead);
        Ids.ingotEnrichedCopper.loadItem(config, Names.ingotEnrichedCopper);
        Ids.itemChunks.loadItem(config, Names.itemChunk);
        Ids.itemDusts.loadItem(config, Names.itemDust);
        Ids.itemBucketOil.loadItem(config, Names.itemBucketOil);
        Ids.itemBacon.loadItem(config, Names.itemBacon);
        
        loadThirdPartyIds();
    }

    private static void loadThirdPartyIds(){
    	Ids.blockHydraulicPneumaticCompressor.loadBlock(config, Names.blockHydraulicPneumaticCompressor);
        
        Ids.blockHydraulicEngine.loadBlock(config, Names.blockHydraulicEngine);
        Ids.blockHydraulicDynamo.loadBlock(config, Names.blockHydraulicDynamo);
        Ids.blockHydraulicGenerator.loadBlock(config, Names.blockHydraulicGenerator);
        
        Ids.blockRFPump.loadBlock(config, Names.blockRFPump);
        Ids.blockElectricPump.loadBlock(config, Names.blockElectricPump);
        Ids.blockKineticPump.loadBlock(config, Names.blockKineticPump);
        
        Ids.blockHydraulicSaw.loadBlock(config, Names.blockHydraulicSaw);
        
    }
}
