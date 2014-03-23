package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class Ores {
	public static Block oreCopper;
	public static Block oreLead;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the ores.
	 */
	public static void init(){
		oreCopper = new OreCopper();
		oreLead = new OreLead();
		
		registerOres();
		addNames();
	}
	
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the ores to the GameRegistry
	 */
	public static void registerOres(){
		GameRegistry.registerBlock(oreCopper, Names.oreCopper.unlocalized);
		GameRegistry.registerBlock(oreLead, Names.oreLead.unlocalized);
		
		OreDictionary.registerOre(Names.oreCopper.unlocalized, new ItemStack(oreCopper));
		OreDictionary.registerOre(Names.oreLead.unlocalized, new ItemStack(oreLead));
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		//LanguageRegistry.addName(oreCopper, Names.oreCopper.localized);
		//LanguageRegistry.addName(oreLead, Names.oreLead.localized);
	}
}

