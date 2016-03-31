package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.worldgen.BlockRubberLeaves;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererMovingPane;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.harvester.RendererHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.client.renderers.generators.RendererHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.client.renderers.generators.RendererHydraulicPump;
import k4unl.minecraft.Hydraulicraft.client.renderers.gow.RendererPortalTeleporter;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererArchimedesScrew;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererInterfaceValve;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererJarOfDirt;
import k4unl.minecraft.Hydraulicraft.client.renderers.rubberHarvesting.RendererRubberTap;
import k4unl.minecraft.Hydraulicraft.client.renderers.storage.RendererBlockFluidTank;
import k4unl.minecraft.Hydraulicraft.client.renderers.transportation.RendererPartHose;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFluidPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileJarOfDirt;
import k4unl.minecraft.Hydraulicraft.tileEntities.rubberHarvesting.TileRubberTap;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileFluidTank;
import mcmultipart.client.multipart.MultipartRegistryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Renderers {

    public static int rendererIdJarOfDirt;


    public static void init() {

        ClientRegistry.bindTileEntitySpecialRenderer(TileHarvesterTrolley.class, new RendererHarvesterTrolley());

        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPiston.class, new RendererHydraulicPiston());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicLavaPump.class, new RendererHydraulicLavaPump());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicPump.class, new RendererHydraulicPump());

        MultipartRegistryClient.bindMultipartSpecialRenderer(PartHose.class, new RendererPartHose());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMovingPane.class, new RendererMovingPane());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePortalTeleporter.class, new RendererPortalTeleporter());

        ClientRegistry.bindTileEntitySpecialRenderer(TileInterfaceValve.class, new RendererInterfaceValve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileJarOfDirt.class, new RendererJarOfDirt());
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(HCBlocks.blockJarDirt), 0, TileJarOfDirt.class);

        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicFluidPump.class, new RendererArchimedesScrew());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFluidTank.class, new RendererBlockFluidTank());

        ClientRegistry.bindTileEntitySpecialRenderer(TileRubberTap.class, new RendererRubberTap());
//		MinecraftForgeClient.registerItemRenderer(HCItems.itemDivingHelmet, new RendererScubaGearItem());
//		MinecraftForgeClient.registerItemRenderer(HCItems.itemDivingController, new RendererScubaGearItem());
//		MinecraftForgeClient.registerItemRenderer(HCItems.itemDivingLegs, new RendererScubaGearItem());
//		MinecraftForgeClient.registerItemRenderer(HCItems.itemDivingBoots, new RendererScubaGearItem());

        ((BlockRubberLeaves)HCBlocks.blockRubberLeaves).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }
}
