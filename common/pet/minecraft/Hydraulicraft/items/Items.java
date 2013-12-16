package pet.minecraft.Hydraulicraft.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import pet.minecraft.Hydraulicraft.lib.Functions;
import pet.minecraft.Hydraulicraft.lib.config.Config;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Items {
	public static Item gasket;
	public static Item ingotCopper;
	public static Item ingotEnrichedCopper;
	public static Item ingotLead;
	public static Item itemFrictionPlate;
	public static Item itemDebugger;
	public static ItemChunks itemChunk;
	
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
		
		itemChunk = new ItemChunks();
		
		
		registerItems();
		addNames();
		
		registerChunks();
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
		
		
		OreDictionary.registerOre(Names.ingotCopper.unlocalized, new ItemStack(ingotCopper));
		OreDictionary.registerOre(Names.ingotLead.unlocalized, new ItemStack(ingotLead));
		OreDictionary.registerOre(Names.ingotEnrichedCopper.unlocalized, new ItemStack(ingotEnrichedCopper));
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		LanguageRegistry.addName(gasket, Names.itemGasket.localized);
		LanguageRegistry.addName(ingotCopper, Names.ingotCopper.localized);
		LanguageRegistry.addName(ingotEnrichedCopper, Names.ingotEnrichedCopper.localized);
		LanguageRegistry.addName(ingotLead, Names.ingotLead.localized);
		LanguageRegistry.addName(itemFrictionPlate, Names.itemFrictionPlate.localized);
		LanguageRegistry.addName(itemDebugger, Names.itemDebugger.localized);
	}
	
	public static void registerChunks(){
		//Get items from ore dictionary:
		String[] oreList = OreDictionary.getOreNames();
		
		List<String> allowedList = new ArrayList<String>();
		allowedList.add("Gold");
		allowedList.add("Iron");
		allowedList.add("Copper");
		allowedList.add("Lead");
		
		for (String ore : oreList) {
			if(Config.canBeCrushed(ore)){
				String metalName = Functions.getMetalName(ore);
				if(allowedList.contains(metalName)){
					int subId = itemChunk.addChunk(metalName);
					OreDictionary.registerOre("chunk" + metalName, new ItemStack(itemChunk, 1, subId));
					LanguageRegistry.addName(new ItemStack(itemChunk,1,subId), metalName + " " + Names.itemChunk.localized);
					
					String oreDictName = "ingot" + metalName;
					ItemStack ingotTarget = new ItemStack(OreDictionary.getOreID("ingot" + metalName),1,0);
					FurnaceRecipes.smelting().addSmelting(Ids.itemChunks.act, subId, ingotTarget, 0);
					
				}
			}
		}
	}
}
