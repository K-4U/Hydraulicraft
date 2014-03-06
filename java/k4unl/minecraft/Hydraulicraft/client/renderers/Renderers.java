package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHarvesterItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHarvesterTrolleyItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicHoseItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicPistonItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicPumpItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Renderers {
	
	public static void init(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterFrame.class, new RendererHarvesterFrame());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterTrolley.class, new RendererHarvesterTrolley());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicHarvester.class, new RendererHarvesterSource());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPiston.class, new RendererHydraulicPiston());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPump.class, new RendererHydraulicPump());
		
		
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicPiston.act, new RendererHydraulicPistonItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicHarvester.act, new RendererHarvesterItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockHarvesterTrolley.act, new RendererHarvesterTrolleyItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicHose.act+256,  new RendererHydraulicHoseItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicPump.act,  new RendererHydraulicPumpItem());
	}
}
