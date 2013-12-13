package pet.minecraft.Hydraulicraft.items;

import pet.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.Item;

public class Items {
	public static Item gasket;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the items.
	 */
	public static void init(){
		gasket = new ItemGasket();
		
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
		
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		LanguageRegistry.addName(gasket, Names.itemGasket.localized);
	}
}
