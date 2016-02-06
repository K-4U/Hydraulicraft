package k4unl.minecraft.Hydraulicraft.thirdParty.igwmod;

import igwmod.api.VariableRetrievalEvent;
import igwmod.api.WikiRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IGWHandler {

    public static void init(){
        WikiRegistry.registerWikiTab(new HydraulicraftWikiTab());

        WikiRegistry.registerRecipeIntegrator(new IntegratorAssembler());

        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.blockAssembler, "hydcraft:menu/crafting");
        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.hydraulicWasher, "hydcraft:menu/washer");


        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.hydraulicHarvesterSource, "hydcraft:menu/farms");
        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.harvesterTrolley, "hydcraft:menu/farms");
        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.hydraulicHarvesterFrame, "hydcraft:menu/farms");
        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.hydraulicPiston, "hydcraft:menu/farms");

        WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.portalBase, "hydcraft:menu/portal");
        //WikiRegistry.registerBlockAndItemPageEntry(HCBlocks.portalFrame, "hydcraft:menu/portal");
        WikiRegistry.registerBlockAndItemPageEntry(HCItems.itemIPCard, "hydcraft:menu/portal");

        MinecraftForge.EVENT_BUS.register(new IGWHandler());
    }

    @SubscribeEvent
    public void varRetrieval(VariableRetrievalEvent event){
        String replacement = null;
        if(event.variableName.startsWith("hydcraft:config:d:")){
            String configName = event.variableName.substring("hydcraft:config:d:".length());
            String category = Configuration.CATEGORY_GENERAL;
            if(configName.contains(":")){
                category = configName.split(":")[0];
                configName = configName.split(":")[1];
            }

            replacement = HCConfig.INSTANCE.getDouble(configName,category) + "";
        }else if(event.variableName.startsWith("hydcraft:config:i:")){
            String configName = event.variableName.substring("hydcraft:config:i:".length());
            String category = Configuration.CATEGORY_GENERAL;
            if(configName.contains(":")){
                category = configName.split(":")[0];
                configName = configName.split(":")[1];
            }

            replacement = HCConfig.INSTANCE.getInt(configName, category) + "";
        }else if(event.variableName.startsWith("hydcraft:config:s:")){
            String configName = event.variableName.substring("hydcraft:config:s:".length());
            String category = Configuration.CATEGORY_GENERAL;
            if(configName.contains(":")){
                category = configName.split(":")[0];
                configName = configName.split(":")[1];
            }

            replacement = HCConfig.INSTANCE.getString(configName, category) + "";
        }else if(event.variableName.startsWith("hydcraft:config:b:")){
            String configName = event.variableName.substring("hydcraft:config:b:".length());
            String category = Configuration.CATEGORY_GENERAL;
            if(configName.contains(":")){
                category = configName.split(":")[0];
                configName = configName.split(":")[1];
            }

            replacement = HCConfig.INSTANCE.getBool(configName, category) + "";
        }else if(event.variableName.startsWith("hydcraft:config:bs:")){
            String configName = event.variableName.substring("hydcraft:config:bs:".length());
            String category = Configuration.CATEGORY_GENERAL;
            if(configName.contains(":")){
                category = configName.split(":")[0];
                configName = configName.split(":")[1];
            }

            if(HCConfig.INSTANCE.getBool(configName, category)){
                replacement = "enabled";
            }else{
                replacement = "disabled";
            }
        }
        if(replacement != null){
            event.replacementValue = replacement;
        }
    }
}
