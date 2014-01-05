package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Renderers {
	public static ISimpleBlockRenderingHandler dummyWasher;
	public static int dummyWasherId;
	
	public static void init(){
		dummyWasher = new RendererDummyWasher();
		dummyWasherId = RenderingRegistry.getNextAvailableRenderId();
		
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicHose.class, new RendererHydraulicHose());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterFrame.class, new RendererHarvesterFrame());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterTrolley.class, new RendererHarvesterTrolley());
		
		RenderingRegistry.registerBlockHandler(Blocks.dummyWasher.getRenderType(), dummyWasher);
	}
}
