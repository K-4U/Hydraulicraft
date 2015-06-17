package k4unl.minecraft.Hydraulicraft.ores;

import cpw.mods.fml.common.registry.GameRegistry;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

public class Ores {
	public static Block oreCopper;
	public static Block oreLead;
	public static Block oreLonezium;
    public static Block oreNadsiumBicarbinate;
    public static Block oreBeachium;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the ores.
	 */
	public static void init(){
		oreCopper = new OreCopper();
		oreLead = new OreLead();
		oreLonezium = new OreLonezium();
        oreNadsiumBicarbinate = new OreNadsiumBicarbinate();
        oreBeachium = new OreBeachium();
		
		registerOres();
	}
	
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the ores to the GameRegistry
	 */
	public static void registerOres(){
		GameRegistry.registerBlock(oreCopper, Names.oreCopper.unlocalized);
		GameRegistry.registerBlock(oreLead, Names.oreLead.unlocalized);
		GameRegistry.registerBlock(oreLonezium, Names.oreLonezium.unlocalized);
        GameRegistry.registerBlock(oreNadsiumBicarbinate, Names.oreNadsiumBicarbinate.unlocalized);
        GameRegistry.registerBlock(oreBeachium, Names.oreBeachium.unlocalized);
		
		OreDictionary.registerOre(Names.oreCopper.unlocalized, oreCopper);
		OreDictionary.registerOre(Names.oreLead.unlocalized, oreLead);
		OreDictionary.registerOre(Names.oreLonezium.unlocalized, oreLonezium);
        OreDictionary.registerOre(Names.oreNadsiumBicarbinate.unlocalized, oreNadsiumBicarbinate);
        OreDictionary.registerOre(Names.oreBeachium.unlocalized, oreBeachium);
	}
}

