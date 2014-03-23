package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.BucketHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
	public static Item gasket;
	public static Item ingotCopper;
	public static Item ingotEnrichedCopper;
	public static Item ingotLead;
	public static Item itemFrictionPlate;
	public static Item itemDebugger;
	public static ItemChunks itemChunk;
	public static ItemDusts itemDust;
	public static Item itemBucketOil;
	public static Item itemBacon;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the items.
	 */
	public static void init(){
		gasket = new ItemGasket();
		ingotCopper = new IngotCopper();
		ingotLead = new IngotLead();
		ingotEnrichedCopper = new IngotEnrichedCopper();
		itemFrictionPlate = new ItemFrictionPlate();
		itemDebugger = new ItemDebug();
		itemBucketOil = new ItemBucketOil();
		itemBacon = new ItemBacon();
		
		
		itemChunk = new ItemChunks();
		itemDust = new ItemDusts();
		
		
		registerItems();
		addNames();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the Items to the GameRegistry
	 */
	public static void registerItems(){
		GameRegistry.registerItem(gasket, Names.itemGasket.unlocalized);
		GameRegistry.registerItem(ingotCopper, Names.ingotCopper.unlocalized);
		GameRegistry.registerItem(ingotEnrichedCopper, Names.ingotEnrichedCopper.unlocalized);
		GameRegistry.registerItem(ingotLead, Names.ingotLead.unlocalized);
		GameRegistry.registerItem(itemFrictionPlate, Names.itemFrictionPlate.unlocalized);
		GameRegistry.registerItem(itemDebugger, Names.itemDebugger.unlocalized);
		GameRegistry.registerItem(itemBucketOil, Names.itemBucketOil.unlocalized);
		GameRegistry.registerItem(itemBacon, Names.itemBacon.unlocalized);
		
		
		OreDictionary.registerOre(Names.ingotCopper.unlocalized, new ItemStack(ingotCopper));
		OreDictionary.registerOre(Names.ingotLead.unlocalized, new ItemStack(ingotLead));
		OreDictionary.registerOre(Names.ingotEnrichedCopper.unlocalized, new ItemStack(ingotEnrichedCopper));
		OreDictionary.registerOre(Names.itemBacon.unlocalized, new ItemStack(itemBacon));
		
		FluidStack st = FluidRegistry.getFluidStack(Names.fluidOil.getLowerUnlocalized(), FluidContainerRegistry.BUCKET_VOLUME);
		FluidContainerRegistry.registerFluidContainer(st, new ItemStack(itemBucketOil), new ItemStack(Item.bucketEmpty));
		BucketHandler.INSTANCE.buckets.put(Fluids.fluidOilBlock, itemBucketOil);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		/*
		LanguageRegistry.addName(gasket, Names.itemGasket.localized);
		LanguageRegistry.addName(ingotCopper, Names.ingotCopper.localized);
		LanguageRegistry.addName(ingotEnrichedCopper, Names.ingotEnrichedCopper.localized);
		LanguageRegistry.addName(ingotLead, Names.ingotLead.localized);
		LanguageRegistry.addName(itemFrictionPlate, Names.itemFrictionPlate.localized);
		LanguageRegistry.addName(itemDebugger, Names.itemDebugger.localized);
		LanguageRegistry.addName(itemBucketOil, Names.itemBucketOil.localized);*/
	}
}
