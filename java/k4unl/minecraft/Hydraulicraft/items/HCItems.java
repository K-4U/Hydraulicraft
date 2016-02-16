package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingHelmet;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingSuit;
import k4unl.minecraft.Hydraulicraft.items.upgrades.UpgradeBigDrill;
import k4unl.minecraft.Hydraulicraft.items.upgrades.UpgradeCapitator;
import k4unl.minecraft.Hydraulicraft.items.upgrades.UpgradeCreative;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.OreDictionaryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

    public static Item itemDivingHelmet;
    public static Item itemDivingController;
    public static Item itemDivingLegs;
    public static Item itemDivingBoots;

    public static Item itemhydraulicWrench;
    public static Item itemCannister;

    public static Item itemDrill;
    public static Item itemSaw;

    public static Item itemPressureGauge;

    public static Item itemUpgradeBigDrill;
    public static Item itemUpgradeCapitator;
    public static Item itemUpgradeCreative;
    public static Item itemRubberBar;

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

        if (!HCConfig.INSTANCE.getBool("disableBacon")) {
            itemBacon = new ItemBacon();
        }
        if (!HCConfig.INSTANCE.getBool("disableEnderLolly")) {
            itemEnderLolly = new ItemEnderLolly();
        }

        itemChunk = new ItemChunks();
        itemDust = new ItemDusts();
        for (String oreName : Hydraulicraft.crushableItems) {
            int meta = itemChunk.addChunk(oreName);
            itemDust.addDust(oreName, meta);
        }

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

        itemPressureGauge = new ItemPressureGauge();

        itemUpgradeBigDrill = new UpgradeBigDrill();
        itemUpgradeCapitator = new UpgradeCapitator();
        itemUpgradeCreative = new UpgradeCreative();

        itemRubberBar = new ItemRubberBar();

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

        if (!HCConfig.INSTANCE.getBool("disableBacon")) {
            GameRegistry.registerItem(itemBacon, Names.itemBacon.unlocalized);
            OreDictionary.registerOre(Names.oredictBacon, new ItemStack(itemBacon));
        }
        if (!HCConfig.INSTANCE.getBool("disableEnderLolly")) {
            GameRegistry.registerItem(itemEnderLolly, Names.itemEnderLolly.unlocalized);
        }

        GameRegistry.registerItem(itemhydraulicWrench, Names.itemHydraulicWrench.unlocalized);
        GameRegistry.registerItem(itemCannister, Names.itemCannister.unlocalized);

        GameRegistry.registerItem(itemDrill, Names.itemDrill.unlocalized);
        GameRegistry.registerItem(itemSaw, Names.itemSaw.unlocalized);

        GameRegistry.registerItem(itemPressureGauge, Names.itemPressureGauge.unlocalized);

        GameRegistry.registerItem(itemUpgradeBigDrill, Names.itemUpgradeBigDrill.unlocalized);
        GameRegistry.registerItem(itemUpgradeCapitator, Names.itemUpgradeCapitator.unlocalized);
        GameRegistry.registerItem(itemUpgradeCreative, Names.itemUpgradeCreative.unlocalized);

        GameRegistry.registerItem(itemRubberBar, Names.itemRubberBar.unlocalized);
    }
}

