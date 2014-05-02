package k4unl.minecraft.Hydraulicraft.lib.config;

public class Constants {
	public final static int MAX_MBAR_OIL_TIER_1 = 1000000;
	public final static int MAX_MBAR_OIL_TIER_2 = 5000000;
	public final static int MAX_MBAR_OIL_TIER_3 = 10000000;
	
	public final static int MAX_MBAR_WATER_TIER_1 = 500000;
	public final static int MAX_MBAR_WATER_TIER_2 = 1000000;
	public final static int MAX_MBAR_WATER_TIER_3 = 1500000;
	
	
	//Generators
	public final static int MAX_MBAR_GEN_WATER_TIER_1 = 25;
	public final static int MAX_MBAR_GEN_WATER_TIER_2 = 75;
	public final static int MAX_MBAR_GEN_WATER_TIER_3 = 100;
	
	public final static int MAX_MBAR_GEN_OIL_TIER_1 = 50;
	public final static int MAX_MBAR_GEN_OIL_TIER_2 = 150;
	public final static int MAX_MBAR_GEN_OIL_TIER_3 = 200;
	
	public final static int[] MAX_LAVA_USAGE = {5, 15, 20};
	public final static float CONVERSION_RATIO_LAVA_HYDRAULIC = 10F;
	
	public final static float USING_WATER_PENALTY = 0.5F;
	
	//GUI
	public final static int COLOR_WATER = 0x4F006DD9;
	public final static int COLOR_PRESSURE = 0x3FFFFFFF;
	public static final int COLOR_OIL = 0x4F8C6900;
	public static final int COLOR_RF = 0x7FE00000;
	public static final int COLOR_MJ = 0x5003FFFF;
	public static final int COLOR_EU = 0x7FEE0000;
	public static final int COLOR_TEXT = 0xFFFFFFFF;
	public static final int COLOR_GUI_TERMINAL = 0xFF00FF00;
	
	
	//Mixer
	public final static int OIL_FOR_ONE_SEED	=	100;
	public final static int WATER_FOR_ONE_SEED  = 	50;
	//Washer
	public static final int MIN_REQUIRED_WATER_FOR_WASHER = 10000;
	
	//Harvester
	public static final float MIN_REQUIRED_PRESSURE_HARVESTER = 100.0F;
	public static final float PRESSURE_USAGE_PISTON = 50;
	public static final int HARVESTER_ID_ENDERLILY = 1;
	public static final int HARVESTER_ID_SUGARCANE = 2;
	
	//Friction Incinerator
	public static final float PRESSURE_INCINERATOR = 100.0F;
	
	/**
	 * Conversion to other mods
	 */
	public final static float WATER_CONVERSION_RATIO = 0.4F;
	
	//PneumaticCraft
	public static final float CONVERSION_RATIO_HYDRAULIC_AIR = 0.95F;
	public static final float MIN_REQUIRED_PRESSURE_COMPRESSOR = 100F;
	
	//Buildcraft
	public static final int[] MJ_USAGE_PER_TICK = {30, 90, 180};
	public static final float CONVERSION_RATIO_MJ_HYDRAULIC = 0.08F;
	public static final float CONVERSION_RATIO_HYDRAULIC_MJ = 0.08F;
	public static final float MIN_REQUIRED_PRESSURE_ENGINE = 10000F;
	public static final int ACTIVATION_MJ	= 20;
	public static final float MAX_TRANSFER_MJ = 50;
	
	//Thermal Expansion
	public static final int[] RF_USAGE_PER_TICK = {500, 1500, 2000};
	public static final float CONVERSION_RATIO_HYDRAULIC_RF = 0.8F;
	public static final float CONVERSION_RATIO_RF_HYDRAULIC = 0.8F;
	
	
	public static final int MAX_TRANSFER_RF = 100;
	public static final int MIN_REQUIRED_RF = 1000;
	
	public static final float MIN_REQUIRED_PRESSURE_DYNAMO = 10000F;
	
	//IC2
	public static final int[] EU_USAGE_PER_TICK = {16, 64, 128};
	public static final int[] INTERNAL_EU_STORAGE = {4000, 8000, 16000};
	public static final int[] MAX_EU = {32, 128, 512};
	public static final float CONVERSION_RATIO_EU_HYDRAULIC = 0.8F;
	public static final float CONVERSION_RATIO_HYDRAULIC_EU = 0.8F;
	public static final int MIN_REQUIRED_EU = 128;
	public static final int MAX_TRANSFER_EU = 128;
	
	
	
	
	
	
	
}
