package k4unl.minecraft.Hydraulicraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingHelmet;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingSuit;
import k4unl.minecraft.Hydraulicraft.lib.BucketHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.OreDictionaryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class HCItems {
    public static Item       gasket;
    public static Item       ingotCopper;
    public static Item       ingotEnrichedCopper;
    public static Item       ingotLead;
    public static Item       itemFrictionPlate;
    public static Item       itemDebugger;
    public static ItemChunks itemChunk;
    public static ItemDusts  itemDust;

    public static Item itemBacon;
    public static Item itemMovingPane;

    public static Item itemMiningHelmet;
    public static Item itemLamp;
    public static Item itemDiamondShards;

    public static Item itemEnrichedCopperDust;

    public static Item itemEnderLolly;
    public static Item itemIPCard;

    public static Item itemBucketOil;
    public static Item itemBucketHydraulicOil;
    public static Item itemBucketLubricant;
    public static Item itemBucketFluoricCarbonFluid;

    public static Item itemDivingHelmet;
    public static Item itemDivingController;
    public static Item itemDivingLegs;
    public static Item itemDivingBoots;

    public static Item itemhydraulicWrench;
    public static Item itemCannister;

    public static Item itemDrill;
    public static Item itemSaw;

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Initializes the items.
     */
    public static void init() {
        gasket = new ItemGasket();
        ingotCopper = new IngotCopper();
        ingotLead = new IngotLead();
        ingotEnrichedCopper = new IngotEnrichedCopper();
        itemFrictionPlate = new ItemFrictionPlate();
        itemDebugger = new ItemDebug();
        itemBucketOil = new ItemBucketBase(Fluids.fluidOilBlock, Names.itemBucketOil);
        itemBucketHydraulicOil = new ItemBucketBase(Fluids.fluidHydraulicOilBlock, Names.itemBucketHydraulicOil);
        itemBucketLubricant = new ItemBucketBase(Fluids.fluidLubricantBlock, Names.itemBucketLubricant);
        itemBucketFluoricCarbonFluid = new ItemBucketBase(Fluids.fluidFluoroCarbonFluidBlock, Names.itemBucketFluoroCarbon);

        if (!HCConfig.INSTANCE.getBool("disableBacon")) {
            itemBacon = new ItemBacon();
        }
        if (!HCConfig.INSTANCE.getBool("disableEnderLolly")) {
            itemEnderLolly = new ItemEnderLolly();
        }


        itemChunk = new ItemChunks();
        itemDust = new ItemDusts();

        itemMovingPane = new ItemMovingPane();
        itemMiningHelmet = new ItemMiningHelmet();
        itemLamp = new ItemLamp();
        itemDiamondShards = new ItemDiamondShard();
        itemEnrichedCopperDust = new ItemCopperEnrichedDust();


        itemIPCard = new ItemIPCard();

        itemDivingHelmet = new ItemDivingHelmet();
        itemDivingController = new ItemDivingSuit(1);
        itemDivingLegs = new ItemDivingSuit(2);
        itemDivingBoots = new ItemDivingSuit(3);

        itemhydraulicWrench = new ItemHydraulicWrench();
        itemCannister = new ItemCannister();

        itemDrill = new ItemHydraulicDrill();
        itemSaw = new ItemHydraulicSaw();

        registerItems();
    }

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Registers the Items to the GameRegistry
     */
    public static void registerItems() {
        GameRegistry.registerItem(gasket, Names.itemGasket.unlocalized);

        ingotCopper = OreDictionaryHelper.registerItem(ingotCopper, Names.ingotCopper.unlocalized);
        ingotEnrichedCopper = OreDictionaryHelper.registerItem(ingotEnrichedCopper, Names.ingotEnrichedCopper.unlocalized);
        ingotLead = OreDictionaryHelper.registerItem(ingotLead, Names.ingotLead.unlocalized);

        GameRegistry.registerItem(itemFrictionPlate, Names.itemFrictionPlate.unlocalized);
        GameRegistry.registerItem(itemDebugger, Names.itemDebugger.unlocalized);


        GameRegistry.registerItem(itemDust, Names.itemDust.unlocalized);
        GameRegistry.registerItem(itemChunk, Names.itemChunk.unlocalized);
        GameRegistry.registerItem(itemMiningHelmet, Names.itemMiningHelmet.unlocalized);
        GameRegistry.registerItem(itemLamp, Names.itemLamp.unlocalized);

        GameRegistry.registerItem(itemMovingPane, "item" + Names.blockMovingPane.unlocalized);
        GameRegistry.registerItem(itemDiamondShards, Names.itemDiamondShard.unlocalized);
        GameRegistry.registerItem(itemEnrichedCopperDust, Names.itemCopperEnrichedDust.unlocalized);

        GameRegistry.registerItem(itemIPCard, Names.itemIPCard.unlocalized);

        GameRegistry.registerItem(itemDivingHelmet, Names.itemDivingHelmet.unlocalized);
        GameRegistry.registerItem(itemDivingController, Names.itemDivingChest.unlocalized);
        GameRegistry.registerItem(itemDivingLegs, Names.itemDivingLegs.unlocalized);
        GameRegistry.registerItem(itemDivingBoots, Names.itemDivingBoots.unlocalized);

        
        OreDictionary.registerOre(Names.itemDiamondShard.unlocalized, new ItemStack(itemDiamondShards));
        OreDictionary.registerOre(Names.itemCopperEnrichedDust.unlocalized, new ItemStack(itemEnrichedCopperDust));


        GameRegistry.registerItem(itemBucketOil, Names.itemBucketOil.unlocalized);
        GameRegistry.registerItem(itemBucketHydraulicOil, Names.itemBucketHydraulicOil.unlocalized);
        GameRegistry.registerItem(itemBucketLubricant, Names.itemBucketLubricant.unlocalized);
        GameRegistry.registerItem(itemBucketFluoricCarbonFluid, Names.itemBucketFluoroCarbon.unlocalized);

        if (!HCConfig.INSTANCE.getBool("disableBacon")) {
            OreDictionary.registerOre(Names.itemBacon.unlocalized, new ItemStack(itemBacon));
            GameRegistry.registerItem(itemBacon, Names.itemBacon.unlocalized);
        }
        if (!HCConfig.INSTANCE.getBool("disableEnderLolly")) {
            GameRegistry.registerItem(itemEnderLolly, Names.itemEnderLolly.unlocalized);
        }

        GameRegistry.registerItem(itemhydraulicWrench, Names.itemHydraulicWrench.unlocalized);
        GameRegistry.registerItem(itemCannister, Names.itemCannister.unlocalized);

        GameRegistry.registerItem(itemDrill, Names.itemDrill.unlocalized);
        GameRegistry.registerItem(itemSaw, Names.itemSaw.unlocalized);


        FluidStack st = FluidRegistry.getFluidStack(Names.fluidHydraulicOil.getLowerUnlocalized(), FluidContainerRegistry.BUCKET_VOLUME);
        FluidContainerRegistry.registerFluidContainer(st, new ItemStack(itemBucketHydraulicOil), new ItemStack((Item) Item.itemRegistry.getObject("bucket")));
        BucketHandler.INSTANCE.buckets.put(Fluids.fluidHydraulicOilBlock, itemBucketHydraulicOil);


        st = FluidRegistry.getFluidStack(Names.fluidOil.getLowerUnlocalized(), FluidContainerRegistry.BUCKET_VOLUME);
        FluidContainerRegistry.registerFluidContainer(st, new ItemStack(itemBucketOil), new ItemStack((Item) Item.itemRegistry.getObject("bucket")));
        BucketHandler.INSTANCE.buckets.put(Fluids.fluidOilBlock, itemBucketOil);

        st = FluidRegistry.getFluidStack(Names.fluidLubricant.getLowerUnlocalized(), FluidContainerRegistry.BUCKET_VOLUME);
        FluidContainerRegistry.registerFluidContainer(st, new ItemStack(itemBucketLubricant), new ItemStack((Item) Item.itemRegistry.getObject("bucket")));
        BucketHandler.INSTANCE.buckets.put(Fluids.fluidLubricantBlock, itemBucketLubricant);

        st = FluidRegistry.getFluidStack(Names.fluidFluoroCarbon.getLowerUnlocalized(), FluidContainerRegistry.BUCKET_VOLUME);
        FluidContainerRegistry.registerFluidContainer(st, new ItemStack(itemBucketFluoricCarbonFluid), new ItemStack((Item) Item.itemRegistry.getObject("bucket")));
        BucketHandler.INSTANCE.buckets.put(Fluids.fluidFluoroCarbonFluidBlock, itemBucketFluoricCarbonFluid);
    }
}

