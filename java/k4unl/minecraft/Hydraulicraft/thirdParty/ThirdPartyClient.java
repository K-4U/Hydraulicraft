package k4unl.minecraft.Hydraulicraft.thirdParty;

import net.minecraftforge.client.MinecraftForgeClient;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererHydraulicEngineItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererKineticPumpItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities.ExtraUtilities;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.IndustrialCraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererElectricPumpItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererHydraulicGeneratorItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamoItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererRFPumpItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Loader;

public class ThirdPartyClient {

    public static void initRenderers(){
    	if(Loader.isModLoaded("PneumaticCraft")) {
            Log.info("Pneumaticraft found! Initializing Pneumaticraft support!");
            Pneumaticraft.initRenderers();
        }
        if(Loader.isModLoaded("ExtraUtilities")){
    		Log.info("ExtraUtilities found! Initializing ExtraUtilities support!");
    		ExtraUtilities.initRenderers();
        }
        
        if(Loader.isModLoaded("BuildCraft|Core")){
        	Log.info("Buildcraft found! Initializing Buildcraft support!");
        	ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicEngine.class, new RendererHydraulicEngine());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileKineticPump.class, new RendererKineticPump());
    		
    		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicEngine.act, new RendererHydraulicEngineItem());
    		MinecraftForgeClient.registerItemRenderer(Ids.blockKineticPump.act, new RendererKineticPumpItem());
        }
        
        if(Loader.isModLoaded("ThermalExpansion")){
        	Log.info("Thermal Expansion found! Initializing Thermal Expansion support!");
        	ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileRFPump.class, new RendererRFPump());
    		
    		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicDynamo.act, new RendererHydraulicDynamoItem());
    		MinecraftForgeClient.registerItemRenderer(Ids.blockRFPump.act, new RendererRFPumpItem());
        }
        
        if(Loader.isModLoaded("IC2")){
        	Log.info("Industrialcraft found! Initializing Industrialcraft support!");
        	ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicGenerator.class, new RendererHydraulicGenerator());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileElectricPump.class, new RendererElectricPump());
    		
    		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicGenerator.act, new RendererHydraulicGeneratorItem());
    		MinecraftForgeClient.registerItemRenderer(Ids.blockElectricPump.act, new RendererElectricPumpItem());
        }
    }
}
