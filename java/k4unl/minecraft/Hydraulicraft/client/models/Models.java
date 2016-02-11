package k4unl.minecraft.Hydraulicraft.client.models;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.SubBlockBase;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.multipart.MultipartHandler;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraftforge.client.model.ModelLoader;

public class Models {

    public static void init() {

        itemBlockModels();
        itemModels();
        for (ItemBucket bucket : Fluids.buckets) {
            ModelLoader.setBucketModelDefinition(bucket);
        }
    }

    private static void itemBlockModels() {

        loadModel(HCBlocks.hydraulicCrusher);
        loadModel(HCBlocks.hydraulicFilter);
        loadModel(HCBlocks.hydraulicFiller);
        loadModel(HCBlocks.hydraulicFrictionIncinerator);
        loadModel(HCBlocks.hydraulicPressureGlass);
        loadModel(HCBlocks.hydraulicPressureWall);
        loadModel(HCBlocks.hydraulicWasher);
        loadModel(HCBlocks.blockValve);
        loadModel(HCBlocks.blockInterfaceValve);
        loadModel(HCBlocks.movingPane);
        for (int i = 0; i < 3; i++) {
            loadModel(HCBlocks.hydraulicPressurevat, i, ((HydraulicTieredBlockBase) HCBlocks.hydraulicPressurevat).getUnlocalizedName(i).substring(5));
            loadModel(HCBlocks.hydraulicPump, i, ((HydraulicTieredBlockBase) HCBlocks.hydraulicPump).getUnlocalizedName(i).substring(5));
            loadModel(HCBlocks.hydraulicLavaPump, i, ((HydraulicTieredBlockBase) HCBlocks.hydraulicLavaPump).getUnlocalizedName(i).substring(5));

            loadModel(HCBlocks.blockCore, i, ((SubBlockBase) HCBlocks.blockCore).getUnlocalizedName(i).substring(5));
        }

        loadModel(HCBlocks.hydraulicHarvesterSource);
        loadModel(HCBlocks.hydraulicHarvesterFrame);

        loadModel(HCBlocks.hydraulicPiston);

        loadModel(HCBlocks.blockCharger);
        loadModel(HCBlocks.blockAssembler);
        loadModel(HCBlocks.blockCopper);
        loadModel(HCBlocks.blockLead);

        loadModel(HCBlocks.blockDirtyMineral);
        loadModel(HCBlocks.blockAssembler);
        loadModel(HCBlocks.blockCharger);
        loadModel(HCBlocks.blockRefinedLonezium);
        loadModel(HCBlocks.blockRefinedNadsiumBicarbinate);
        loadModel(HCBlocks.blockHydraulicFluidPump);

        loadModel(HCBlocks.blockJarDirt);

        loadModel(Ores.oreCopper);
        loadModel(Ores.oreLead);
        loadModel(Ores.oreBeachium);
        loadModel(Ores.oreFoxium);
        loadModel(Ores.oreLonezium);
        loadModel(Ores.oreNadsiumBicarbinate);

        loadModel(HCBlocks.portalBase);

        loadModel(Fluids.fluidHydraulicOil.getBlock());
        loadModel(Fluids.fluidOil.getBlock());
        loadModel(Fluids.fluidFluoroCarbonFluid.getBlock());
        loadModel(Fluids.fluidLubricant.getBlock());

        loadModel(HCBlocks.blockRubberLeaves);
        loadModel(HCBlocks.blockRubberSapling);
        loadModel(HCBlocks.blockRubberWood);

        loadModel(HCBlocks.blockFluidTank);
    }

    private static void loadModel(Block block, int metadata, String override) {

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata, new ModelResourceLocation(ModInfo.LID + ":" + override,
                "inventory"));
    }

    private static void loadModel(Block block) {

        loadModel(block, 0);
    }

    private static void loadModel(Block block, int metadata) {

        loadModel(block, metadata, block.getUnlocalizedName().substring(5));
    }

    private static void loadItemModel(Item item) {

        loadItemModel(item, 0);
    }

    private static void loadItemModel(Item item, int metadata) {

        loadItemModel(item, metadata, item.getUnlocalizedName().substring(5));
    }

    private static void loadItemModel(Item item, int metadata, String override) {

        //Log.info("Registering the item model of " + ModInfo.LID + ":" + override);
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(ModInfo.LID + ":" + override, "inventory"));
    }

    public static void itemModels() {

        loadItemModel(HCItems.gasket);
        loadItemModel(HCItems.ingotCopper);
        loadItemModel(HCItems.ingotEnrichedCopper);
        loadItemModel(HCItems.ingotLead);
        loadItemModel(HCItems.itemFrictionPlate);
        loadItemModel(HCItems.itemDebugger);

        for (int i = 0; i < HCItems.itemChunk.getChunks().size(); i++) {
            loadItemModel(HCItems.itemChunk, i, HCItems.itemChunk.getUnlocalizedName(i));
            loadItemModel(HCItems.itemDust, i, HCItems.itemDust.getUnlocalizedName(i));
        }

        if (!HCConfig.INSTANCE.getBool("disableBacon")) {
            loadItemModel(HCItems.itemBacon);
        }
        loadItemModel(HCItems.itemMovingPane);

        loadItemModel(HCItems.itemMiningHelmet);
        loadItemModel(HCItems.itemLamp);
        loadItemModel(HCItems.itemDiamondShards);

        loadItemModel(HCItems.itemEnrichedCopperDust);

        if (!HCConfig.INSTANCE.getBool("disableEnderLolly")) {
            loadItemModel(HCItems.itemEnderLolly);
        }
        loadItemModel(HCItems.itemIPCard);

        loadItemModel(HCItems.itemDivingHelmet);
        loadItemModel(HCItems.itemDivingController);
        loadItemModel(HCItems.itemDivingLegs);
        loadItemModel(HCItems.itemDivingBoots);

        loadItemModel(HCItems.itemhydraulicWrench);
        loadItemModel(HCItems.itemCannister);

        loadItemModel(HCItems.itemDrill);
        loadItemModel(HCItems.itemSaw);

        loadItemModel(HCItems.itemPressureGauge);

        loadItemModel(MultipartHandler.itemPartPortalFrame);
    }
}
