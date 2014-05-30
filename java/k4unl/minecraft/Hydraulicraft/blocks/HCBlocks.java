package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicWaterPump;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockMovingPane;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockPressureDisposal;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerCoreBlock;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHydraulicBlock;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPressureVat;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPump;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockCopper;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicCore;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureGlass;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureWall;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicValve;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInfiniteSource;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockInterfaceValve;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockLead;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockLight;
import k4unl.minecraft.Hydraulicraft.blocks.storage.BlockHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCrops;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary;
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
	public static Block hydraulicPressureGlass;
	public static Block hydraulicHarvesterSource;
	public static Block harvesterTrolley;
	public static Block pressureDisposal;
	public static Block blockCore;
	public static Block blockValve;
	public static Block blockInterfaceValve;
	
	public static Block blockHose;
	
	public static Block blockInfiniteSource;
	public static Block movingPane;
	public static Block blockLight;
	
	public static Block blockHydraulicWaterPump;
	public static Block blockCopper;
	public static Block blockLead;
	
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
		hydraulicPressureGlass = new BlockHydraulicPressureGlass();
		hydraulicHarvesterSource = new BlockHydraulicHarvester();
		
		pressureDisposal = new BlockPressureDisposal();
		blockCore = new BlockHydraulicCore();
		blockValve = new BlockHydraulicValve();
		blockInterfaceValve = new BlockInterfaceValve();
		
		//blockHose = new BlockHose();
		blockInfiniteSource = new BlockInfiniteSource();
		movingPane = new BlockMovingPane();
		
		blockLight = new BlockLight();
		blockHydraulicWaterPump = new BlockHydraulicWaterPump();
		
		blockLead = new BlockLead();
		blockCopper = new BlockCopper();
		
		harvesterTrolley = new BlockHarvesterTrolley();
		
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleyCrops());
		
		registerBlocks();
	}
	


	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the blocks to the GameRegistry
	 */
	public static void registerBlocks(){
		GameRegistry.registerBlock(hydraulicMixer, HandlerHydraulicBlock.class, Names.blockHydraulicMixer.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicFrictionIncinerator, HandlerHydraulicBlock.class, Names.blockHydraulicFrictionIncinerator.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicCrusher, HandlerHydraulicBlock.class, Names.blockHydraulicCrusher.unlocalized, ModInfo.ID);
		//GameRegistry.registerBlock(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.unlocalized);
		//GameRegistry.registerBlock(hydraulicPressureValve, Names.blockHydraulicPressureValve.unlocalized);
		GameRegistry.registerBlock(hydraulicPiston, HandlerHydraulicBlock.class, Names.blockHydraulicPiston.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicWasher, HandlerHydraulicBlock.class, Names.blockHydraulicWasher.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicPressureWall, ItemBlock.class, Names.blockHydraulicPressureWall.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicPressureGlass, ItemBlock.class, Names.blockHydraulicPressureGlass.unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(pressureDisposal, HandlerHydraulicBlock.class, Names.blockPressureDisposal.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockValve, HandlerHydraulicBlock.class, Names.blockValve.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockInterfaceValve, HandlerHydraulicBlock.class, Names.blockInterfaceValve.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockInfiniteSource, HandlerHydraulicBlock.class, Names.blockInfiniteSource.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(movingPane, HandlerHydraulicBlock.class, Names.blockMovingPane.unlocalized, ModInfo.ID);
		
		//GameRegistry.registerBlock(blockHose, HandlerHose.class, Names.partHose[0].unlocalized, ModInfo.ID);
		
		
		GameRegistry.registerBlock(hydraulicPressurevat, HandlerPressureVat.class, Names.blockHydraulicPressurevat[0].unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicPump, HandlerPump.class, Names.blockHydraulicPump[0].unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(hydraulicLavaPump, HandlerLavaPump.class, Names.blockHydraulicLavaPump[0].unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(hydraulicHarvesterSource, HandlerHarvester.class, Names.blockHydraulicHarvester[0].unlocalized, ModInfo.ID);
		
		
		GameRegistry.registerBlock(blockCore, HandlerCoreBlock.class, Names.blockCore[0].unlocalized, ModInfo.ID);
	
		GameRegistry.registerBlock(blockLight, HandlerHydraulicBlock.class, Names.blockLight.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockHydraulicWaterPump, HandlerHydraulicBlock.class, Names.blockHydraulicWaterPump.unlocalized, ModInfo.ID);
		
		GameRegistry.registerBlock(blockCopper, ItemBlock.class, Names.blockCopper.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockLead, ItemBlock.class, Names.blockLead.unlocalized, ModInfo.ID);
		
		OreDictionary.registerOre(Names.blockCopper.unlocalized, blockCopper);
		OreDictionary.registerOre(Names.blockLead.unlocalized, blockLead);
	}
}
