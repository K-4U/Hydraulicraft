package pet.minecraft.Hydraulicraft.items;

import pet.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.Item;

public class Items {
	public static Item gasket;
	public static Item ingotCopper;
	public static Item ingotEnrichedCopper;
	public static Item ingotLead;
	public static Item itemFrictionPlate;
	
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
	}
}
