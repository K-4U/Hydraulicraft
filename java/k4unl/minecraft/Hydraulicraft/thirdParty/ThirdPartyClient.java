package k4unl.minecraft.Hydraulicraft.thirdParty;

import k4unl.minecraft.Hydraulicraft.lib.Log;
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
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
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
    		
    		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Buildcraft.blockHydraulicEngine), new RendererHydraulicEngineItem());
    		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Buildcraft.blockKineticPump), new RendererKineticPumpItem());
        }
        
        /*
        if(Loader.isModLoaded("ThermalExpansion")){
        	Log.info("Thermal Expansion found! Initializing Thermal Expansion support!");
        	ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileRFPump.class, new RendererRFPump());
    		
    		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(p_150898_0_), new RendererHydraulicDynamoItem());
    		MinecraftForgeClient.registerItemRenderer(Ids.blockRFPump.act, new RendererRFPumpItem());
        }*/
        
        if(Loader.isModLoaded("IC2")){
        	Log.info("Industrialcraft found! Initializing Industrialcraft support!");
        	ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicGenerator.class, new RendererHydraulicGenerator());
    		ClientRegistry.bindTileEntitySpecialRenderer(TileElectricPump.class, new RendererElectricPump());
    		
    		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(IndustrialCraft.blockHydraulicGenerator), new RendererHydraulicGeneratorItem());
    		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(IndustrialCraft.blockElectricPump), new RendererElectricPumpItem());
        }
    }
}
