package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockPressureDisposal;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerCoreBlock;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPressureVat;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPump;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicCore;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureWall;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicValve;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInterfaceValve;
import k4unl.minecraft.Hydraulicraft.blocks.storage.BlockHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks {
	public static Block hydraulicPump;
	public static Block hydraulicMixer;
	public static Block hydraulicFrictionIncinerator;
	public static Block hydraulicCrusher;
	public static Block hydraulicPressureGauge;
	public static Block hydraulicPressureValve;
	public static Block hydraulicPressurevat;
	public static Block hydraulicPiston;
	public static Block hydraulicWasher;
	public static Block hydraulicPressureWall;
	public static Block hydraulicHarvesterSource;
	public static Block harvesterTrolley;
	public static Block pressureDisposal;
	public static Block blockCore;
	public static Block blockValve;
	public static Block blockInterfaceValve;
	
	
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
		//hydraulicPressureGauge = new BlockHydraulicPressureGauge();
		//hydraulicPressureValve = new BlockHydraulicPressureValve();
		hydraulicPiston = new BlockHydraulicPiston();
		hydraulicWasher = new BlockHydraulicWasher();
		hydraulicPressureWall = new BlockHydraulicPressureWall();
		hydraulicHarvesterSource = new BlockHydraulicHarvester();
		
		harvesterTrolley = new BlockHarvesterTrolley();
		
		pressureDisposal = new BlockPressureDisposal();
		blockCore = new BlockHydraulicCore();
		blockValve = new BlockHydraulicValve();
		blockInterfaceValve = new BlockInterfaceValve();
		
		registerBlocks();
		addNames();
		
		registerMultiparts();
	}
	


	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the blocks to the GameRegistry
	 */
	public static void registerBlocks(){
		GameRegistry.registerBlock(hydraulicMixer, Names.blockHydraulicMixer.unlocalized);
		GameRegistry.registerBlock(hydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator.unlocalized);
		GameRegistry.registerBlock(hydraulicCrusher, Names.blockHydraulicCrusher.unlocalized);
		//GameRegistry.registerBlock(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.unlocalized);
		//GameRegistry.registerBlock(hydraulicPressureValve, Names.blockHydraulicPressureValve.unlocalized);
		GameRegistry.registerBlock(hydraulicPiston, Names.blockHydraulicPiston.unlocalized);
		GameRegistry.registerBlock(hydraulicWasher, Names.blockHydraulicWasher.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureWall, Names.blockHydraulicPressureWall.unlocalized);
		
		GameRegistry.registerBlock(pressureDisposal, Names.blockPressureDisposal.unlocalized);
		GameRegistry.registerBlock(blockValve, Names.blockValve.unlocalized);
		GameRegistry.registerBlock(blockInterfaceValve, Names.blockInterfaceValve.unlocalized);
		
		
		GameRegistry.registerBlock(hydraulicPressurevat, HandlerPressureVat.class, Names.blockHydraulicPressurevat[0].unlocalized);
		GameRegistry.registerBlock(hydraulicPump, HandlerPump.class, Names.blockHydraulicPump[0].unlocalized);
		
		GameRegistry.registerBlock(hydraulicHarvesterSource, HandlerHarvester.class, Names.blockHydraulicHarvester[0].unlocalized);
		GameRegistry.registerBlock(harvesterTrolley, HandlerHarvesterTrolley.class, Names.blockHarvesterTrolley[0].unlocalized);
		
		GameRegistry.registerBlock(blockCore, HandlerCoreBlock.class, Names.blockCore[0].unlocalized);
		
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		
		/*
		LanguageRegistry.addName(hydraulicMixer, Names.blockHydraulicMixer.localized);
		LanguageRegistry.addName(hydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator.localized);
		LanguageRegistry.addName(hydraulicCrusher, Names.blockHydraulicCrusher.localized);
		//LanguageRegistry.addName(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.localized);
		//LanguageRegistry.addName(hydraulicPressureValve, Names.blockHydraulicPressureValve.localized);
		//LanguageRegistry.addName(hydraulicPiston, Names.blockHydraulicPiston.localized);
		LanguageRegistry.addName(hydraulicWasher, Names.blockHydraulicWasher.localized);
		LanguageRegistry.addName(hydraulicPressureWall, Names.blockHydraulicPressureWall.localized);
		//LanguageRegistry.addName(hydraulicHarvesterSource, Names.blockHydraulicHarvesterSource.localized);
		
		LanguageRegistry.addName(dummyWasher, Names.blockDummyWasher.localized);
		
		for(int i = 0; i < 3; i++){
			LanguageRegistry.addName(new ItemStack(hydraulicHose, 1, i), Names.blockHydraulicHose[i].localized);
			LanguageRegistry.addName(new ItemStack(hydraulicPressurevat,1,i), Names.blockHydraulicPressurevat[i].localized);
			LanguageRegistry.addName(new ItemStack(hydraulicPump,1,i), Names.blockHydraulicPump[i].localized);
		}*/
				
	}
	
	private static void registerMultiparts() {
		//new RegisterBlockPart(hydraulicHose, PartHose.class).init();
	}
}
