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

        loadBlockModel(HCBlocks.hydraulicCrusher);
        loadBlockModel(HCBlocks.hydraulicFilter);
        loadBlockModel(HCBlocks.hydraulicFiller);
        loadBlockModel(HCBlocks.hydraulicFrictionIncinerator);
        loadBlockModel(HCBlocks.hydraulicPressureGlass);
        loadBlockModel(HCBlocks.hydraulicPressureWall);
        loadBlockModel(HCBlocks.hydraulicWasher);
        loadBlockModel(HCBlocks.blockValve);
        loadBlockModel(HCBlocks.blockInterfaceValve);
        loadBlockModel(HCBlocks.movingPane);
        for (int i = 0; i < 3; i++) {
            loadBlockModel(HCBlocks.hydraulicPressurevat, i, ((HydraulicTieredBlockBase) HCBlocks.hydraulicPressurevat).getUnlocalizedName(i).substring(5));
            loadBlockModel(HCBlocks.hydraulicPump, i, ((HydraulicTieredBlockBase) HCBlocks.hydraulicPump).getUnlocalizedName(i).substring(5));
            loadBlockModel(HCBlocks.hydraulicLavaPump, i, ((HydraulicTieredBlockBase) HCBlocks.hydraulicLavaPump).getUnlocalizedName(i).substring(5));

            loadBlockModel(HCBlocks.blockCore, i, ((SubBlockBase) HCBlocks.blockCore).getUnlocalizedName(i).substring(5));
        }

        loadBlockModel(HCBlocks.hydraulicHarvesterSource);
        loadBlockModel(HCBlocks.hydraulicHarvesterFrame);

        loadBlockModel(HCBlocks.hydraulicPiston);

        loadBlockModel(HCBlocks.blockCharger);
        loadBlockModel(HCBlocks.blockAssembler);
        loadBlockModel(HCBlocks.blockCopper);
        loadBlockModel(HCBlocks.blockLead);

        loadBlockModel(HCBlocks.blockDirtyMineral);
        loadBlockModel(HCBlocks.blockAssembler);
        loadBlockModel(HCBlocks.blockCharger);
        loadBlockModel(HCBlocks.blockRefinedLonezium);
        loadBlockModel(HCBlocks.blockRefinedNadsiumBicarbinate);
        loadBlockModel(HCBlocks.blockHydraulicFluidPump);

        loadBlockModel(HCBlocks.blockJarDirt);

        loadBlockModel(Ores.oreCopper);
        loadBlockModel(Ores.oreLead);
        loadBlockModel(Ores.oreBeachium);
        loadBlockModel(Ores.oreFoxium);
        loadBlockModel(Ores.oreLonezium);
        loadBlockModel(Ores.oreNadsiumBicarbinate);

        loadBlockModel(HCBlocks.portalBase);

        loadBlockModel(Fluids.fluidHydraulicOil.getBlock());
        loadBlockModel(Fluids.fluidOil.getBlock());
        loadBlockModel(Fluids.fluidFluoroCarbonFluid.getBlock());
        loadBlockModel(Fluids.fluidLubricant.getBlock());
        loadBlockModel(Fluids.fluidRubber.getBlock());
        loadBlockModel(Fluids.fluidClayWater.getBlock());

        loadBlockModel(HCBlocks.blockRubberLeaves);
        loadBlockModel(HCBlocks.blockRubberSapling);
        loadBlockModel(HCBlocks.blockRubberWood);

        loadBlockModel(HCBlocks.blockFluidTank);
        loadBlockModel(HCBlocks.blockRubberTap);
        loadBlockModel(HCBlocks.blockFluidRecombobulator);

        loadBlockModel(HCBlocks.blockTreeHarvester);
    }

    public static void loadBlockModel(Block block, int metadata, String override) {

        loadItemModel(Item.getItemFromBlock(block), metadata, override);
    }

    public static void loadBlockModel(Block block) {

        loadBlockModel(block, 0);
    }

    public static void loadBlockModel(Block block, int metadata) {

        loadBlockModel(block, metadata, block.getUnlocalizedName().substring(5));
    }

    public static void loadItemModel(Item item) {

        loadItemModel(item, 0);
    }

    public static void loadItemModel(Item item, int metadata) {

        loadItemModel(item, metadata, item.getUnlocalizedName().substring(5));
    }

    public static void loadItemModel(Item item, int metadata, String override) {

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
        loadItemModel(MultipartHandler.itemPartRubberSuckingPipe);
        loadItemModel(HCItems.itemRubberBar);
    }
}
