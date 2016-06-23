package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public final static int MAX_MBAR_OIL_TIER_1 = 1000000;
    public final static int MAX_MBAR_OIL_TIER_2 = 5000000;
    public final static int MAX_MBAR_OIL_TIER_3 = 10000000;

    public final static int MAX_MBAR_WATER_TIER_1 = 500000;
    public final static int MAX_MBAR_WATER_TIER_2 = 1000000;
    public final static int MAX_MBAR_WATER_TIER_3 = 1500000;


    //Generators
    public final static float MAX_LAVA_USAGE = 0.1F;

    public final static float USING_WATER_PENALTY = 0.005F;

    public final static float MIN_PRESSURE_PANE      = 10.0F;
    public final static float PRESSURE_PANE_PER_TICK = 5.0F;

    //GUI
    public final static int COLOR_WATER    = 0x4F006DD9;
    public final static int COLOR_PRESSURE = 0x3FFFFFFF;
    public static final int COLOR_OIL      = 0x4F8C6900;
    public static final int COLOR_RF       = 0x7FE00000;
    public static final int COLOR_EU       = 0x7FEE0000;
    public static final int COLOR_TEXT     = 0xFFFFFFFF;

    //Harvester
    public static final float MIN_REQUIRED_PRESSURE_HARVESTER = 100.0F;
    public static final float PRESSURE_USAGE_PISTON           = 50;

    /**
     * Conversion to other mods
     */
    public final static float WATER_CONVERSION_RATIO = 0.4F;

    //PneumaticCraft
    public static final float CONVERSION_RATIO_HYDRAULIC_AIR   = 0.98F;
    public static final float MIN_REQUIRED_PRESSURE_COMPRESSOR = 100F;

    //Thermal Expansion
    public static final int[] RF_USAGE_PER_TICK             = {50, 150, 200};
    public static final float CONVERSION_RATIO_HYDRAULIC_RF = 0.8F;
    public static final float CONVERSION_RATIO_RF_HYDRAULIC = 0.9F;


    public static final int MAX_TRANSFER_RF = 200;
    public static final int MIN_REQUIRED_RF = 1000;

    public static final float MIN_REQUIRED_PRESSURE_DYNAMO = 10000F;

    //IC2
    public static final int[] EU_USAGE_PER_TICK             = {32, 128, 512};
    public static final int[] INTERNAL_EU_STORAGE           = {4000, 8000, 16000};
    public static final int[] MAX_EU                        = {32, 128, 512};
    public static final float CONVERSION_RATIO_EU_HYDRAULIC = 0.8F;
    public static final float CONVERSION_RATIO_HYDRAULIC_EU = 0.8F;
    public static final int   MIN_REQUIRED_EU               = 128;
    public static final int   MAX_TRANSFER_EU               = 128;

    public static final int PACKET_UPDATE_DISTANCE = 64;

    public static final int KEYS_MINING_HELMET = 0;

    public static final int MAX_TANK_SIZE = 15;

    public static final int MAX_FLUID_TRANSFER_T = 100;

    public static Map<Block, Boolean> TANK_BLACKLIST = new HashMap<Block, Boolean>();
    public static Map<Block, Integer> TANK_SCORELIST = new HashMap<Block, Integer>();

    static {
        TANK_BLACKLIST.put(Blocks.DIRT, true);
        TANK_BLACKLIST.put(Blocks.GRASS, true);
        TANK_BLACKLIST.put(Blocks.ACTIVATOR_RAIL, true);
        TANK_BLACKLIST.put(Blocks.ANVIL, true);
        TANK_BLACKLIST.put(Blocks.BEACON, true);
        TANK_BLACKLIST.put(Blocks.BED, true);
        TANK_BLACKLIST.put(Blocks.BREWING_STAND, true);
        TANK_BLACKLIST.put(Blocks.BROWN_MUSHROOM, true);
        TANK_BLACKLIST.put(Blocks.CACTUS, true);
        TANK_BLACKLIST.put(Blocks.CAKE, true);
        TANK_BLACKLIST.put(Blocks.CARPET, true);
        TANK_BLACKLIST.put(Blocks.CAULDRON, true);
        TANK_BLACKLIST.put(Blocks.CHEST, true);
        TANK_BLACKLIST.put(Blocks.COBBLESTONE_WALL, true);
        TANK_BLACKLIST.put(Blocks.COCOA, true);
        TANK_BLACKLIST.put(Blocks.DAYLIGHT_DETECTOR, true);
        TANK_BLACKLIST.put(Blocks.DEADBUSH, true);
        TANK_BLACKLIST.put(Blocks.DOUBLE_PLANT, true);
        TANK_BLACKLIST.put(Blocks.ENCHANTING_TABLE, true);
        TANK_BLACKLIST.put(Blocks.ENDER_CHEST, true);
        TANK_BLACKLIST.put(Blocks.FLOWER_POT, true);
        TANK_BLACKLIST.put(Blocks.FLOWING_LAVA, true);
        TANK_BLACKLIST.put(Blocks.FLOWING_WATER, true);
        TANK_BLACKLIST.put(Blocks.GOLDEN_RAIL, true);
        TANK_BLACKLIST.put(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, true);
        TANK_BLACKLIST.put(Blocks.HOPPER, true);
        TANK_BLACKLIST.put(Blocks.IRON_BARS, true);
        TANK_BLACKLIST.put(Blocks.IRON_DOOR, true);
        TANK_BLACKLIST.put(Blocks.JUKEBOX, true);
        TANK_BLACKLIST.put(Blocks.LADDER, true);
        TANK_BLACKLIST.put(Blocks.LAVA, true);
        TANK_BLACKLIST.put(Blocks.LEAVES, true);
        TANK_BLACKLIST.put(Blocks.LEAVES2, true);
        TANK_BLACKLIST.put(Blocks.LEVER, true);
        TANK_BLACKLIST.put(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, true);
        TANK_BLACKLIST.put(Blocks.MELON_STEM, true);
        TANK_BLACKLIST.put(Blocks.NETHER_BRICK_FENCE, true);
        TANK_BLACKLIST.put(Blocks.NETHER_WART, true);
        TANK_BLACKLIST.put(Blocks.PISTON, true);
        TANK_BLACKLIST.put(Blocks.POWERED_COMPARATOR, true);
        TANK_BLACKLIST.put(Blocks.POWERED_REPEATER, true);
        TANK_BLACKLIST.put(Blocks.PUMPKIN_STEM, true);
        TANK_BLACKLIST.put(Blocks.RAIL, true);
        TANK_BLACKLIST.put(Blocks.RED_FLOWER, true);
        TANK_BLACKLIST.put(Blocks.RED_MUSHROOM, true);
        TANK_BLACKLIST.put(Blocks.REDSTONE_WIRE, true);
        TANK_BLACKLIST.put(Blocks.REEDS, true);
        TANK_BLACKLIST.put(Blocks.SAND, true);
        TANK_BLACKLIST.put(Blocks.SAPLING, true);
        TANK_BLACKLIST.put(Blocks.SKULL, true);
        TANK_BLACKLIST.put(Blocks.SPONGE, true);
        TANK_BLACKLIST.put(Blocks.STANDING_SIGN, true);
        TANK_BLACKLIST.put(Blocks.STANDING_BANNER, true);
        TANK_BLACKLIST.put(Blocks.STICKY_PISTON, true);
        TANK_BLACKLIST.put(Blocks.STONE_SLAB, true);
        TANK_BLACKLIST.put(Blocks.TALLGRASS, true);
        TANK_BLACKLIST.put(Blocks.TNT, true);
        TANK_BLACKLIST.put(Blocks.TORCH, true);
        TANK_BLACKLIST.put(Blocks.TRAPDOOR, true);
        TANK_BLACKLIST.put(Blocks.TRAPPED_CHEST, true);
        TANK_BLACKLIST.put(Blocks.TRIPWIRE, true);
        TANK_BLACKLIST.put(Blocks.TRIPWIRE_HOOK, true);
        TANK_BLACKLIST.put(Blocks.UNLIT_REDSTONE_TORCH, true);
        TANK_BLACKLIST.put(Blocks.UNPOWERED_COMPARATOR, true);
        TANK_BLACKLIST.put(Blocks.UNPOWERED_REPEATER, true);
        TANK_BLACKLIST.put(Blocks.WOOL, true);
        TANK_BLACKLIST.put(Blocks.WOODEN_BUTTON, true);
        TANK_BLACKLIST.put(Blocks.WOODEN_SLAB, true);
        TANK_BLACKLIST.put(Blocks.VINE, true);
        TANK_BLACKLIST.put(Blocks.YELLOW_FLOWER, true);


        TANK_SCORELIST.put(Blocks.COBBLESTONE, 1);
        TANK_SCORELIST.put(Blocks.STONE, 2);
        TANK_SCORELIST.put(Blocks.IRON_BLOCK, 10);
        TANK_SCORELIST.put(Blocks.GOLD_BLOCK, 20);
        TANK_SCORELIST.put(HCBlocks.hydraulicPressureGlass, 30);
        TANK_SCORELIST.put(HCBlocks.hydraulicPressureGlass, 30);
        TANK_SCORELIST.put(Blocks.DIAMOND_BLOCK, 40);
        TANK_SCORELIST.put(Blocks.EMERALD_BLOCK, 50);
        TANK_SCORELIST.put(Blocks.LOG, 1);
    }
}
