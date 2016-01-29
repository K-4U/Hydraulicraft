package k4unl.minecraft.Hydraulicraft.client.models;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class Models {

    public static void init() {
        itemBlockModels();
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
        loadModel(HCBlocks.hydraulicPressurevat, 0);
        loadModel(HCBlocks.hydraulicPressurevat, 1);
        loadModel(HCBlocks.hydraulicPressurevat, 2);
        loadModel(HCBlocks.hydraulicPump, 0);
        loadModel(HCBlocks.hydraulicPump, 1);
        loadModel(HCBlocks.hydraulicPump, 2);
        loadModel(HCBlocks.hydraulicLavaPump, 0);
        loadModel(HCBlocks.hydraulicLavaPump, 1);
        loadModel(HCBlocks.hydraulicLavaPump, 2);

        loadModel(HCBlocks.hydraulicHarvesterSource);

        loadModel(HCBlocks.hydraulicPiston);
        loadModel(HCBlocks.blockCore, 0);
        loadModel(HCBlocks.blockCore, 1);
        loadModel(HCBlocks.blockCore, 2);
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

        loadModel(Ores.oreCopperReplacement);
        loadModel(Ores.oreLeadReplacement);
        loadModel(Ores.oreBeachium);
        loadModel(Ores.oreFoxium);
        loadModel(Ores.oreLonezium);
        loadModel(Ores.oreNadsiumBicarbinate);

        loadModel(HCBlocks.portalBase);
        loadModel(HCBlocks.portalFrame);

        loadModel(Fluids.fluidHydraulicOil.getBlock());
        loadModel(Fluids.fluidOil.getBlock());
        loadModel(Fluids.fluidFluoroCarbonFluid.getBlock());
        loadModel(Fluids.fluidLubricant.getBlock());
    }

    private static void loadModel(Block block){
        loadModel(block, 0);
    }

    private static void loadModel(Block block, int metadata){
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata, new ModelResourceLocation(ModInfo.LID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }

    private static void loadItemModel(Item item){
        loadItemModel(item, 0);
    }

    private static void loadItemModel(Item item, int metadata){
        loadItemModel(item, metadata,  item.getUnlocalizedName().substring(5));
    }

    private static void loadItemModel(Item item, int metadata, String override){
        ModelLoader.setCustomModelResourceLocation(item, metadata,  new ModelResourceLocation(ModInfo.LID + ":" + override, "inventory"));
    }

    public static void itemModels() {
        loadItemModel(HCItems.gasket);
        loadItemModel(HCItems.ingotCopper);
        loadItemModel(HCItems.ingotEnrichedCopper);
        loadItemModel(HCItems.ingotLead);
        loadItemModel(HCItems.itemFrictionPlate);
        loadItemModel(HCItems.itemDebugger);


        for (int i = 0;i < HCItems.itemChunk.getChunks().size(); i++){
            loadItemModel(HCItems.itemChunk, i, "chunk");
        }
        /*loadItemModel(HCItems.itemChunk);*/
        loadItemModel(HCItems.itemDust);
    
        loadItemModel(HCItems.itemBacon);
        loadItemModel(HCItems.itemMovingPane);
    
        loadItemModel(HCItems.itemMiningHelmet);
        loadItemModel(HCItems.itemLamp);
        loadItemModel(HCItems.itemDiamondShards);
    
        loadItemModel(HCItems.itemEnrichedCopperDust);
    
        loadItemModel(HCItems.itemEnderLolly);
        loadItemModel(HCItems.itemIPCard);

        for (Item bucket : Fluids.buckets) {
            loadItemModel(bucket);
        }

        loadItemModel(HCItems.itemDivingHelmet);
        loadItemModel(HCItems.itemDivingController);
        loadItemModel(HCItems.itemDivingLegs);
        loadItemModel(HCItems.itemDivingBoots);
    
        loadItemModel(HCItems.itemhydraulicWrench);
        loadItemModel(HCItems.itemCannister);
    
        loadItemModel(HCItems.itemDrill);
        loadItemModel(HCItems.itemSaw);
    
        loadItemModel(HCItems.itemPressureGauge);
    }
}
