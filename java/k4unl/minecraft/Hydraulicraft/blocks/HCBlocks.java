package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockMovingPane;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockPressureDisposal;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerCoreBlock;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHose;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPressureVat;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPump;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicCore;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureWall;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicValve;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInfiniteSource;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInterfaceValve;
import k4unl.minecraft.Hydraulicraft.blocks.storage.BlockHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.blocks.transporter.BlockHose;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import cpw.mods.fml.common.registry.GameRegistry;

public class HCBlocks {
	
	public static Block hydraulicPump;
	public static Block hydraulicLavaPump;
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
	
	public static Block blockHose;
	
	public static Block blockInfiniteSource;
	public static Block movingPane;
	
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes the blocks.
	 */
	public static void init(){
		hydraulicPump = new BlockHydraulicPump();
		hydraulicLavaPump = new BlockHydraulicLavaPump();
		hydraulicPressurevat = new BlockHydraulicPressureVat();
		hydraulicMixer = new BlockHydraulicMixer();
		hydraulicFrictionIncinerator = new BlockHydraulicFrictionIncinerator();
		hydraulicCrusher = new BlockHydraulicCrusher();
		hydraulicPiston = new BlockHydraulicPiston();
		hydraulicWasher = new BlockHydraulicWasher();
		hydraulicPressureWall = new BlockHydraulicPressureWall();
		hydraulicHarvesterSource = new BlockHydraulicHarvester();
		
		harvesterTrolley = new BlockHarvesterTrolley();
		
		pressureDisposal = new BlockPressureDisposal();
		blockCore = new BlockHydraulicCore();
		blockValve = new BlockHydraulicValve();
		blockInterfaceValve = new BlockInterfaceValve();
		
		blockHose = new BlockHose();
		blockInfiniteSource = new BlockInfiniteSource();
		movingPane = new BlockMovingPane();
		
		
		
		registerBlocks();
		addNames();
	}
	


	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the blocks to the GameRegistry
	 */
	public static void registerBlocks(){
		GameRegistry.registerBlock(hydraulicMixer, ItemBlock.class, Names.blockHydraulicMixer.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicFrictionIncinerator, ItemBlock.class, Names.blockHydraulicFrictionIncinerator.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicCrusher, ItemBlock.class, Names.blockHydraulicCrusher.unlocalized, ModInfo.ID);
		//GameRegistry.registerBlock(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.unlocalized);
		//GameRegistry.registerBlock(hydraulicPressureValve, Names.blockHydraulicPressureValve.unlocalized);
		GameRegistry.registerBlock(hydraulicPiston, ItemBlock.class, Names.blockHydraulicPiston.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicWasher, ItemBlock.class, Names.blockHydraulicWasher.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicPressureWall, ItemBlock.class, Names.blockHydraulicPressureWall.unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(pressureDisposal, ItemBlock.class, Names.blockPressureDisposal.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockValve, ItemBlock.class, Names.blockValve.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockInterfaceValve, ItemBlock.class, Names.blockInterfaceValve.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockInfiniteSource, ItemBlock.class, Names.blockInfiniteSource.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(movingPane, ItemBlock.class, Names.blockMovingPane.unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(blockHose, HandlerHose.class, Names.partHose[0].unlocalized, ModInfo.ID);
		
		
		GameRegistry.registerBlock(hydraulicPressurevat, HandlerPressureVat.class, Names.blockHydraulicPressurevat[0].unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicPump, HandlerPump.class, Names.blockHydraulicPump[0].unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicLavaPump, HandlerLavaPump.class, Names.blockHydraulicLavaPump[0].unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(hydraulicHarvesterSource, HandlerHarvester.class, Names.blockHydraulicHarvester[0].unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(harvesterTrolley, HandlerHarvesterTrolley.class, Names.blockHarvesterTrolley[0].unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(blockCore, HandlerCoreBlock.class, Names.blockCore[0].unlocalized, ModInfo.ID);
		
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
}
