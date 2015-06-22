package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.OreDictionaryHelper;
import net.minecraft.block.Block;

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

		oreCopper = OreDictionaryHelper.registerBlock(oreCopper, Names.oreCopper.unlocalized);
		oreLead = OreDictionaryHelper.registerBlock(oreLead, Names.oreLead.unlocalized);
		oreLonezium = OreDictionaryHelper.registerBlock(oreLonezium, Names.oreLonezium.unlocalized);
		oreNadsiumBicarbinate = OreDictionaryHelper.registerBlock(oreNadsiumBicarbinate, Names.oreNadsiumBicarbinate.unlocalized);
		oreBeachium = OreDictionaryHelper.registerBlock(oreBeachium, Names.oreBeachium.unlocalized);
	}
}

