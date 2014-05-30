package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererMovingPane;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester.RendererHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester.RendererHarvesterSource;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester.RendererHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.client.renderers.generators.RendererHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.client.renderers.generators.RendererHydraulicPump;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHarvesterItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHarvesterTrolleyItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicHoseItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicLavaPumpItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicPistonItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererHydraulicPumpItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.RendererPartValveItem;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartHose;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.transporter.TilePressureHose;
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
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePressureHose.class, new RendererPartHose());
		
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileMovingPane.class, new RendererMovingPane());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicPiston), new RendererHydraulicPistonItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicHarvesterSource), new RendererHarvesterItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.harvesterTrolley), new RendererHarvesterTrolleyItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicPump),  new RendererHydraulicPumpItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicLavaPump),  new RendererHydraulicLavaPumpItem());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.blockHose),  new RendererHydraulicHoseItem());
		MinecraftForgeClient.registerItemRenderer(Multipart.itemPartHose,  new RendererHydraulicHoseItem());
		MinecraftForgeClient.registerItemRenderer(Multipart.itemPartValve,  new RendererPartValveItem());
		//MinecraftForgeClient.registerItemRenderer(Ids.partValve.act+256,  new RendererPartValveItem());*/
	}
}
