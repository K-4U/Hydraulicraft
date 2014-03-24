package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.TileEntities.transporter.TilePressureHose;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHarvesterItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHarvesterTrolleyItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicHoseItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicLavaPumpItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicPistonItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicPumpItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;

public class Renderers {
	
	public static void init(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterFrame.class, new RendererHarvesterFrame());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterTrolley.class, new RendererHarvesterTrolley());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicHarvester.class, new RendererHarvesterSource());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPiston.class, new RendererHydraulicPiston());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicLavaPump.class, new RendererHydraulicLavaPump());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPump.class, new RendererHydraulicPump());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePressureHose.class, new RendererHydraulicHose());
		
		
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicPiston), new RendererHydraulicPistonItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicHarvesterSource), new RendererHarvesterItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.harvesterTrolley), new RendererHarvesterTrolleyItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicPump),  new RendererHydraulicPumpItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicLavaPump),  new RendererHydraulicLavaPumpItem());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.blockHose),  new RendererHydraulicHoseItem());
		//MinecraftForgeClient.registerItemRenderer(Ids.partValve.act+256,  new RendererPartValveItem());*/
	}
}
