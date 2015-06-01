package k4unl.minecraft.Hydraulicraft.client.renderers;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererMovingPane;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester.RendererHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester.RendererHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.client.renderers.generators.RendererHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.client.renderers.generators.RendererHydraulicPump;
import k4unl.minecraft.Hydraulicraft.client.renderers.gow.RendererPortalBase;
import k4unl.minecraft.Hydraulicraft.client.renderers.gow.RendererPortalFrame;
import k4unl.minecraft.Hydraulicraft.client.renderers.gow.RendererPortalTeleporter;
import k4unl.minecraft.Hydraulicraft.client.renderers.items.*;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererArchimedesScrew;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererInterfaceValve;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererJarOfDirt;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartHose;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFluidPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileJarOfDirt;
import k4unl.minecraft.Hydraulicraft.tileEntities.transporter.TilePressureHose;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class Renderers {
	public static int rendererIdJarOfDirt;


	public static void init(){
		rendererIdJarOfDirt = RenderingRegistry.getNextAvailableRenderId();

		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterFrame.class, new RendererHarvesterFrame());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterTrolley.class, new RendererHarvesterTrolley());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPiston.class, new RendererHydraulicPiston());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicLavaPump.class, new RendererHydraulicLavaPump());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPump.class, new RendererHydraulicPump());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePressureHose.class, new RendererPartHose());
		ClientRegistry.bindTileEntitySpecialRenderer(TileMovingPane.class, new RendererMovingPane());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicPiston), new RendererHydraulicPistonItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicHarvesterFrame), new RendererHarvesterItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.harvesterTrolley), new RendererHarvesterTrolleyItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicPump),  new RendererHydraulicPumpItem());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.hydraulicLavaPump),  new RendererHydraulicLavaPumpItem());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.blockHose),  new RendererHydraulicHoseItem());
		MinecraftForgeClient.registerItemRenderer(Multipart.itemPartHose, new RendererHydraulicHoseItem());
		MinecraftForgeClient.registerItemRenderer(Multipart.itemPartValve, new RendererPartValveItem());
		MinecraftForgeClient.registerItemRenderer(Multipart.itemPartFluidPipe, new RendererPartFluidPipeItem());
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalBase.class, new RendererPortalBase());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalFrame.class, new RendererPortalFrame());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalTeleporter.class, new RendererPortalTeleporter());
		
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.portalBase), new ItemRendererPortalBase());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.portalFrame), new ItemRendererPortalFrame());

		RenderingRegistry.registerBlockHandler(new RendererInterfaceValve());
		ClientRegistry.bindTileEntitySpecialRenderer(TileInterfaceValve.class, new RendererInterfaceValve());

		ClientRegistry.bindTileEntitySpecialRenderer(TileJarOfDirt.class, new RendererJarOfDirt());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(HCBlocks.blockJarDirt), new ItemRendererJarOfDirt());

		RenderingRegistry.registerBlockHandler(new RendererArchimedesScrew());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicFluidPump.class, new RendererArchimedesScrew());

		ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterFrame.class, new RendererHarvesterFrame());
	}
}
