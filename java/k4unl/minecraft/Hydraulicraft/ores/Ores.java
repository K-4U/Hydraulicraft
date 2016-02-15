package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class Ores {

    public static Block oreCopper;
    public static Block oreLead;
    public static Block oreLonezium;
    public static Block oreNadsiumBicarbinate;
    public static Block oreBeachium;
    public static Block oreFoxium;

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Initializes the ores.
     */
    public static void init() {

        oreCopper = new OreCopper();
        oreLead = new OreLead();
        oreLonezium = new OreLonezium();
        oreNadsiumBicarbinate = new OreNadsiumBicarbinate();
        oreBeachium = new OreBeachium();
        oreFoxium = new OreFoxium();

        registerOres();
    }


    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Registers the ores to the GameRegistry
     */
    public static void registerOres() {

        register(oreCopper, Names.oreCopper.unlocalized);
        register(oreLead, Names.oreLead.unlocalized);
        register(oreLonezium, Names.oreLonezium.unlocalized);
        register(oreNadsiumBicarbinate, Names.oreNadsiumBicarbinate.unlocalized);
        register(oreBeachium, Names.oreBeachium.unlocalized);
        register(oreFoxium, Names.oreFoxium.unlocalized);
    }

    private static void register(Block toRegister, String name) {

        GameRegistry.registerBlock(toRegister, name);
        OreDictionary.registerOre(name, toRegister);
    }
}

