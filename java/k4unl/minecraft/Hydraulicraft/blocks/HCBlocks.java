package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicCharger;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockHydraulicWaterPump;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockMovingPane;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.BlockPressureDisposal;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicPump;
import k4unl.minecraft.Hydraulicraft.blocks.gow.BlockPortalBase;
import k4unl.minecraft.Hydraulicraft.blocks.gow.BlockPortalFrame;
import k4unl.minecraft.Hydraulicraft.blocks.gow.BlockPortalTeleporter;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerCoreBlock;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHarvester;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHydraulicBlock;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPressureVat;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerPump;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.misc.*;
import k4unl.minecraft.Hydraulicraft.blocks.storage.BlockHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCactus;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCrops;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyNetherWart;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleySugarCane;
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
	public static Block blockCharger;
	
	public static Block blockHose;
	
	public static Block blockInfiniteSource;
	public static Block movingPane;
	public static Block blockLight;
	
	public static Block blockHydraulicWaterPump;
	public static Block blockCopper;
	public static Block blockLead;
	
	public static Block portalBase;
	public static Block portalFrame;
	public static Block portalTeleporter;

    public static Block blockChunkLoader;
	
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
		blockCharger = new BlockHydraulicCharger();
		
		portalBase = new BlockPortalBase();
		portalFrame = new BlockPortalFrame();
		portalTeleporter = new BlockPortalTeleporter();


        blockChunkLoader = new BlockChunkLoader();

		harvesterTrolley = new BlockHarvesterTrolley();
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleyCrops());
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleySugarCane());
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleyNetherWart());
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleyCactus());
		
		registerBlocks();
	}
	


	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the blocks to the GameRegistry
	 */
	public static void registerBlocks(){
		GameRegistry.registerBlock(hydraulicMixer, HandlerHydraulicBlock.class, Names.blockHydraulicMixer.unlocalized);
		GameRegistry.registerBlock(hydraulicFrictionIncinerator, HandlerHydraulicBlock.class, Names.blockHydraulicFrictionIncinerator.unlocalized);
		GameRegistry.registerBlock(hydraulicCrusher, HandlerHydraulicBlock.class, Names.blockHydraulicCrusher.unlocalized);
		//GameRegistry.registerBlock(hydraulicPressureGauge, Names.blockHydraulicPressureGauge.unlocalized);
		//GameRegistry.registerBlock(hydraulicPressureValve, Names.blockHydraulicPressureValve.unlocalized);
		GameRegistry.registerBlock(hydraulicPiston, HandlerHydraulicBlock.class, Names.blockHydraulicPiston.unlocalized);
		GameRegistry.registerBlock(hydraulicWasher, HandlerHydraulicBlock.class, Names.blockHydraulicWasher.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureWall, ItemBlock.class, Names.blockHydraulicPressureWall.unlocalized);
		GameRegistry.registerBlock(hydraulicPressureGlass, ItemBlock.class, Names.blockHydraulicPressureGlass.unlocalized);
		
		GameRegistry.registerBlock(pressureDisposal, HandlerHydraulicBlock.class, Names.blockPressureDisposal.unlocalized);
		GameRegistry.registerBlock(blockValve, HandlerHydraulicBlock.class, Names.blockValve.unlocalized);
		GameRegistry.registerBlock(blockInterfaceValve, HandlerHydraulicBlock.class, Names.blockInterfaceValve.unlocalized);
		GameRegistry.registerBlock(blockInfiniteSource, HandlerHydraulicBlock.class, Names.blockInfiniteSource.unlocalized);
		GameRegistry.registerBlock(movingPane, HandlerHydraulicBlock.class, Names.blockMovingPane.unlocalized);
		
		//GameRegistry.registerBlock(blockHose, HandlerHose.class, Names.partHose[0].unlocalized, ModInfo.ID);
		
		
		GameRegistry.registerBlock(hydraulicPressurevat, HandlerPressureVat.class, Names.blockHydraulicPressurevat[0].unlocalized);
		GameRegistry.registerBlock(hydraulicPump, HandlerPump.class, Names.blockHydraulicPump[0].unlocalized);
		GameRegistry.registerBlock(hydraulicLavaPump, HandlerLavaPump.class, Names.blockHydraulicLavaPump[0].unlocalized);
		
		GameRegistry.registerBlock(hydraulicHarvesterSource, HandlerHarvester.class, Names.blockHydraulicHarvester[0].unlocalized);
		
		
		GameRegistry.registerBlock(blockCore, HandlerCoreBlock.class, Names.blockCore[0].unlocalized);
	
		GameRegistry.registerBlock(blockLight, HandlerHydraulicBlock.class, Names.blockLight.unlocalized);
		GameRegistry.registerBlock(blockHydraulicWaterPump, HandlerHydraulicBlock.class, Names.blockHydraulicWaterPump.unlocalized);
		
		GameRegistry.registerBlock(blockCopper, ItemBlock.class, Names.blockCopper.unlocalized);
		GameRegistry.registerBlock(blockLead, ItemBlock.class, Names.blockLead.unlocalized);
		
		GameRegistry.registerBlock(harvesterTrolley, HandlerTrolley.class, Names.blockHarvesterTrolley.unlocalized);
		GameRegistry.registerBlock(blockCharger, HandlerHydraulicBlock.class, Names.blockHydraulicCharger.unlocalized);

        GameRegistry.registerBlock(blockChunkLoader, ItemBlock.class,
                Names.blockChunkLoader.unlocalized);

		GameRegistry.registerBlock(portalBase, Names.portalBase.unlocalized);
		GameRegistry.registerBlock(portalFrame, Names.portalFrame.unlocalized);
		GameRegistry.registerBlock(portalTeleporter, Names.portalTeleporter.unlocalized);
		
		OreDictionary.registerOre(Names.blockCopper.unlocalized, blockCopper);
		OreDictionary.registerOre(Names.blockLead.unlocalized, blockLead);
	}
}
