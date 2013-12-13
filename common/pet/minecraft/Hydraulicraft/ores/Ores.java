package pet.minecraft.Hydraulicraft.ores;

import pet.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;

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
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		LanguageRegistry.addName(oreCopper, Names.oreCopper.localized);
		LanguageRegistry.addName(oreLead, Names.oreLead.localized);
	}
}

