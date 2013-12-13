package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;

public class Blocks {
	public static Block hydraulicPump;
	public static Block hydraulicMixer;
	public static Block hydraulicFrictionPlate;
	public static Block hydraulicCrusher;
	public static Block hydraulicPressureGauge;
	public static Block hydraulicPressureValve;
	public static Block hydraulicPressurevat;
	public static Block hydraulicHoze;
	public static Block hydraulicPiston;
	public static Block hydraulicWasher;
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the blocks.
	 */
	public static void init(){
		hydraulicPump = new BlockHydraulicPump();
		hydraulicPressurevat = new BlockHydraulicPressureVat();
		hydraulicMixer = new BlockHydraulicMixer();
		hydraulicFrictionPlate = new BlockHydraulicFrictionPlate();
		hydraulicCrusher = new BlockHydraulicCrusher();
		hydraulicPressureGauge = new BlockHydraulicPressureGauge();
		hydraulicPressureValve = new BlockHydraulicPressureValve();
		hydraulicHoze = new BlockHydraulicHoze();
		hydraulicPiston = new BlockHydraulicPiston();
		hydraulicWasher = new BlockHydraulicWasher();
		
		registerBlocks();
		addNames();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the blocks to the GameRegistry
	 */
	public static void registerBlocks(){
		GameRegistry.registerBlock(hydraulicPump, Names.blockHydraulicPump.unlocalized);
		GameRegistry.registerBlock(hydraulicPressurevat, Names.blockHydraulicPressurevat.unlocalized);
		GameRegistry.registerBlock(hydraulicMixer, Names.blockHydraulicMixer.unlocalized);
		GameRegistry.registerBlock(hydraulicFrictionPlate, Names.blockHydraulicFrictionPlate.unlocalized);
		GameRegistry.registerBlock(hydraulicCrusher, Names.blockHydraulicCrusher.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureValve, Names.blockHydraulicPressureValve.unlocalized);
		GameRegistry.registerBlock(hydraulicHoze, Names.blockHydraulicHoze.unlocalized);
		GameRegistry.registerBlock(hydraulicPiston, Names.blockHydraulicPiston.unlocalized);
		GameRegistry.registerBlock(hydraulicWasher, Names.blockHydraulicWasher.unlocalized);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		LanguageRegistry.addName(hydraulicPump, Names.blockHydraulicPump.localized);
		LanguageRegistry.addName(hydraulicPressurevat, Names.blockHydraulicPressurevat.localized);
		LanguageRegistry.addName(hydraulicMixer, Names.blockHydraulicMixer.localized);
		LanguageRegistry.addName(hydraulicFrictionPlate, Names.blockHydraulicFrictionPlate.localized);
		LanguageRegistry.addName(hydraulicCrusher, Names.blockHydraulicCrusher.localized);
		LanguageRegistry.addName(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.localized);
		LanguageRegistry.addName(hydraulicPressureValve, Names.blockHydraulicPressureValve.localized);
		LanguageRegistry.addName(hydraulicHoze, Names.blockHydraulicHoze.localized);
		LanguageRegistry.addName(hydraulicPiston, Names.blockHydraulicPiston.localized);
		LanguageRegistry.addName(hydraulicWasher, Names.blockHydraulicWasher.localized);
	}
}
