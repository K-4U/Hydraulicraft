package pet.minecraft.Hydraulicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import pet.minecraft.Hydraulicraft.blocks.handlers.HandlerPressureHoze;
import pet.minecraft.Hydraulicraft.blocks.handlers.HandlerPressureVat;
import pet.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static Block hydraulicPump;
	public static Block hydraulicMixer;
	public static Block hydraulicFrictionIncinerator;
	public static Block hydraulicCrusher;
	public static Block hydraulicPressureGauge;
	public static Block hydraulicPressureValve;
	public static Block hydraulicPressurevat;
	public static Block hydraulicHose;
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
		hydraulicFrictionIncinerator = new BlockHydraulicFrictionIncinerator();
		hydraulicCrusher = new BlockHydraulicCrusher();
		hydraulicPressureGauge = new BlockHydraulicPressureGauge();
		hydraulicPressureValve = new BlockHydraulicPressureValve();
		hydraulicHose = new BlockHydraulicHose();
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
		
		GameRegistry.registerBlock(hydraulicMixer, Names.blockHydraulicMixer.unlocalized);
		GameRegistry.registerBlock(hydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator.unlocalized);
		GameRegistry.registerBlock(hydraulicCrusher, Names.blockHydraulicCrusher.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureValve, Names.blockHydraulicPressureValve.unlocalized);
		GameRegistry.registerBlock(hydraulicPiston, Names.blockHydraulicPiston.unlocalized);
		GameRegistry.registerBlock(hydraulicWasher, Names.blockHydraulicWasher.unlocalized);
		
		
		GameRegistry.registerBlock(hydraulicHose, HandlerPressureHoze.class, Names.blockHydraulicHose[0].unlocalized);
		GameRegistry.registerBlock(hydraulicPressurevat, HandlerPressureVat.class, Names.blockHydraulicPressurevat[0].unlocalized);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		LanguageRegistry.addName(hydraulicPump, Names.blockHydraulicPump.localized);
		
		LanguageRegistry.addName(hydraulicMixer, Names.blockHydraulicMixer.localized);
		LanguageRegistry.addName(hydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator.localized);
		LanguageRegistry.addName(hydraulicCrusher, Names.blockHydraulicCrusher.localized);
		LanguageRegistry.addName(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.localized);
		LanguageRegistry.addName(hydraulicPressureValve, Names.blockHydraulicPressureValve.localized);
		LanguageRegistry.addName(hydraulicPiston, Names.blockHydraulicPiston.localized);
		LanguageRegistry.addName(hydraulicWasher, Names.blockHydraulicWasher.localized);
		
		for(int i = 0; i < 3; i++){
			LanguageRegistry.addName(new ItemStack(hydraulicHose, 1, i), Names.blockHydraulicHose[i].localized);
			LanguageRegistry.addName(new ItemStack(hydraulicPressurevat,1,i), Names.blockHydraulicPressurevat[i].localized);
		}
		
		
	}
}
