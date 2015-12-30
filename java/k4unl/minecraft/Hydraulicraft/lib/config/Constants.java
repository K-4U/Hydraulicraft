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
	
	public final static float MIN_PRESSURE_PANE = 10.0F;
	public final static float PRESSURE_PANE_PER_TICK = 5.0F;
	
	//GUI
	public final static int COLOR_WATER = 0x4F006DD9;
	public final static int COLOR_PRESSURE = 0x3FFFFFFF;
	public static final int COLOR_OIL = 0x4F8C6900;
	public static final int COLOR_RF = 0x7FE00000;
	public static final int COLOR_EU = 0x7FEE0000;
	public static final int COLOR_TEXT = 0xFFFFFFFF;

	//Harvester
	public static final float MIN_REQUIRED_PRESSURE_HARVESTER = 100.0F;
	public static final float PRESSURE_USAGE_PISTON = 50;
	
	/**
	 * Conversion to other mods
	 */
	public final static float WATER_CONVERSION_RATIO = 0.4F;
	
	//PneumaticCraft
	public static final float CONVERSION_RATIO_HYDRAULIC_AIR = 0.98F;
	public static final float MIN_REQUIRED_PRESSURE_COMPRESSOR = 100F;

	//Thermal Expansion
	public static final int[] RF_USAGE_PER_TICK = {50, 150, 200};
	public static final float CONVERSION_RATIO_HYDRAULIC_RF = 0.8F;
	public static final float CONVERSION_RATIO_RF_HYDRAULIC = 0.9F;
	
	
	public static final int MAX_TRANSFER_RF = 200;
	public static final int MIN_REQUIRED_RF = 1000;
	
	public static final float MIN_REQUIRED_PRESSURE_DYNAMO = 10000F;
	
	//IC2
	public static final int[] EU_USAGE_PER_TICK = {32, 128, 512};
	public static final int[] INTERNAL_EU_STORAGE = {4000, 8000, 16000};
	public static final int[] MAX_EU = {32, 128, 512};
	public static final float CONVERSION_RATIO_EU_HYDRAULIC = 0.8F;
	public static final float CONVERSION_RATIO_HYDRAULIC_EU = 0.8F;
	public static final int MIN_REQUIRED_EU = 128;
	public static final int MAX_TRANSFER_EU = 128;
	
	public static final int PACKET_UPDATE_DISTANCE = 64;
	
	public static final int KEYS_MINING_HELMET = 0;

	public static final int MAX_TANK_SIZE = 15;

    public static final int MAX_FLUID_TRANSFER_T    = 100;

    public static Map<Block, Boolean> TANK_BLACKLIST = new HashMap<Block, Boolean>();
    public static Map<Block, Integer> TANK_SCORELIST = new HashMap<Block, Integer>();

    static {
        TANK_BLACKLIST.put(Blocks.dirt, true);
        TANK_BLACKLIST.put(Blocks.grass, true);
        TANK_BLACKLIST.put(Blocks.activator_rail, true);
        TANK_BLACKLIST.put(Blocks.anvil, true);
        TANK_BLACKLIST.put(Blocks.beacon, true);
        TANK_BLACKLIST.put(Blocks.bed, true);
        TANK_BLACKLIST.put(Blocks.brewing_stand, true);
        TANK_BLACKLIST.put(Blocks.brown_mushroom, true);
        TANK_BLACKLIST.put(Blocks.cactus, true);
        TANK_BLACKLIST.put(Blocks.cake, true);
        TANK_BLACKLIST.put(Blocks.carpet, true);
        TANK_BLACKLIST.put(Blocks.cauldron, true);
        TANK_BLACKLIST.put(Blocks.chest, true);
        TANK_BLACKLIST.put(Blocks.cobblestone_wall, true);
        TANK_BLACKLIST.put(Blocks.cocoa, true);
        TANK_BLACKLIST.put(Blocks.daylight_detector, true);
        TANK_BLACKLIST.put(Blocks.deadbush, true);
        TANK_BLACKLIST.put(Blocks.double_plant, true);
        TANK_BLACKLIST.put(Blocks.enchanting_table, true);
        TANK_BLACKLIST.put(Blocks.ender_chest, true);
        TANK_BLACKLIST.put(Blocks.fence, true);
        TANK_BLACKLIST.put(Blocks.fence_gate, true);
        TANK_BLACKLIST.put(Blocks.flower_pot, true);
        TANK_BLACKLIST.put(Blocks.flowing_lava, true);
        TANK_BLACKLIST.put(Blocks.flowing_water, true);
        TANK_BLACKLIST.put(Blocks.golden_rail, true);
        TANK_BLACKLIST.put(Blocks.heavy_weighted_pressure_plate, true);
        TANK_BLACKLIST.put(Blocks.hopper, true);
        TANK_BLACKLIST.put(Blocks.iron_bars, true);
        TANK_BLACKLIST.put(Blocks.iron_door, true);
        TANK_BLACKLIST.put(Blocks.jukebox, true);
        TANK_BLACKLIST.put(Blocks.ladder, true);
        TANK_BLACKLIST.put(Blocks.lava, true);
        TANK_BLACKLIST.put(Blocks.leaves, true);
        TANK_BLACKLIST.put(Blocks.leaves2, true);
        TANK_BLACKLIST.put(Blocks.lever, true);
        TANK_BLACKLIST.put(Blocks.light_weighted_pressure_plate, true);
        TANK_BLACKLIST.put(Blocks.melon_stem, true);
        TANK_BLACKLIST.put(Blocks.nether_brick_fence, true);
        TANK_BLACKLIST.put(Blocks.nether_wart, true);
        TANK_BLACKLIST.put(Blocks.piston, true);
        TANK_BLACKLIST.put(Blocks.powered_comparator, true);
        TANK_BLACKLIST.put(Blocks.powered_repeater, true);
        TANK_BLACKLIST.put(Blocks.pumpkin_stem, true);
        TANK_BLACKLIST.put(Blocks.rail, true);
        TANK_BLACKLIST.put(Blocks.red_flower, true);
        TANK_BLACKLIST.put(Blocks.red_mushroom, true);
        TANK_BLACKLIST.put(Blocks.redstone_wire, true);
        TANK_BLACKLIST.put(Blocks.reeds, true);
        TANK_BLACKLIST.put(Blocks.sand, true);
        TANK_BLACKLIST.put(Blocks.sapling, true);
        TANK_BLACKLIST.put(Blocks.skull, true);
        TANK_BLACKLIST.put(Blocks.sponge, true);
        TANK_BLACKLIST.put(Blocks.standing_sign, true);
        TANK_BLACKLIST.put(Blocks.sticky_piston, true);
        TANK_BLACKLIST.put(Blocks.stone_slab, true);
        TANK_BLACKLIST.put(Blocks.tallgrass, true);
        TANK_BLACKLIST.put(Blocks.tnt, true);
        TANK_BLACKLIST.put(Blocks.torch, true);
        TANK_BLACKLIST.put(Blocks.trapdoor, true);
        TANK_BLACKLIST.put(Blocks.trapped_chest, true);
        TANK_BLACKLIST.put(Blocks.tripwire, true);
        TANK_BLACKLIST.put(Blocks.tripwire_hook, true);
        TANK_BLACKLIST.put(Blocks.unlit_redstone_torch, true);
        TANK_BLACKLIST.put(Blocks.unpowered_comparator, true);
        TANK_BLACKLIST.put(Blocks.unpowered_repeater, true);
        TANK_BLACKLIST.put(Blocks.wool, true);
        TANK_BLACKLIST.put(Blocks.vine, true);
        TANK_BLACKLIST.put(Blocks.yellow_flower, true);


        TANK_SCORELIST.put(Blocks.cobblestone, 1);
        TANK_SCORELIST.put(Blocks.stone, 2);
        TANK_SCORELIST.put(Blocks.iron_block, 10);
        TANK_SCORELIST.put(Blocks.gold_block, 20);
        TANK_SCORELIST.put(HCBlocks.hydraulicPressureGlass, 30);
        TANK_SCORELIST.put(HCBlocks.hydraulicPressureGlass, 30);
        TANK_SCORELIST.put(Blocks.diamond_block, 40);
        TANK_SCORELIST.put(Blocks.emerald_block, 50);
        TANK_SCORELIST.put(Blocks.log, 1);
    }
}
