package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockAssembler;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHydraulicFiller;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.*;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.misc.*;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing.BlockHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.blocks.generators.BlockHydraulicPump;
import k4unl.minecraft.Hydraulicraft.blocks.gow.BlockPortalBase;
import k4unl.minecraft.Hydraulicraft.blocks.gow.BlockPortalTeleporter;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.*;
import k4unl.minecraft.Hydraulicraft.blocks.misc.*;
import k4unl.minecraft.Hydraulicraft.blocks.rubberHarvesting.BlockRubberTap;
import k4unl.minecraft.Hydraulicraft.blocks.storage.BlockFluidTank;
import k4unl.minecraft.Hydraulicraft.blocks.storage.BlockHydraulicPressureReservoir;
import k4unl.minecraft.Hydraulicraft.blocks.worldgen.BlockRubberLeaves;
import k4unl.minecraft.Hydraulicraft.blocks.worldgen.BlockRubberSapling;
import k4unl.minecraft.Hydraulicraft.blocks.worldgen.BlockRubberWood;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCactus;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCrops;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyNetherWart;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleySugarCane;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class HCBlocks {

    public static Block hydraulicPump;
    public static Block hydraulicLavaPump;
    public static Block hydraulicFilter;
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
    public static Block hydraulicHarvesterFrame;
    public static Block harvesterTrolley;
    public static Block pressureDisposal;
    public static Block blockCore;
    public static Block blockValve;
    public static Block blockInterfaceValve;
    public static Block blockCharger;
    public static Block hydraulicFiller;

    public static Block blockHose;

    public static Block blockInfiniteSource;
    public static Block movingPane;
    public static Block blockLight;

    public static Block blockHydraulicFluidPump;
    public static Block blockCopper;
    public static Block blockLead;

    public static Block portalBase;
    public static Block portalFrame;
    public static Block portalTeleporter;

    public static Block blockJarDirt;
    public static Block blockAssembler;
    public static Block blockRefinedLonezium;
    public static Block blockRefinedNadsiumBicarbinate;
    public static Block blockDirtyMineral;

    public static Block blockRubberWood;
    public static Block blockRubberLeaves;
    public static BlockRubberSapling blockRubberSapling;

    public static Block blockFluidTank;
    public static Block blockRubberTap;
    public static Block blockFluidRecombobulator;

    public static Block blockTreeHarvester;
    public static Block blockTreeHarvesterTrolley;

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Initializes the blocks.
     */
    public static void init() {

        hydraulicPump = new BlockHydraulicPump();
        hydraulicLavaPump = new BlockHydraulicLavaPump();
        hydraulicPressurevat = new BlockHydraulicPressureReservoir();
        hydraulicFilter = new BlockHydraulicFilter();
        hydraulicFrictionIncinerator = new BlockHydraulicFrictionIncinerator();
        hydraulicCrusher = new BlockHydraulicCrusher();
        hydraulicPiston = new BlockHydraulicPiston();
        hydraulicWasher = new BlockHydraulicWasher();
        hydraulicPressureWall = new BlockHydraulicPressureWall();
        hydraulicPressureGlass = new BlockHydraulicPressureGlass();
        hydraulicHarvesterSource = new BlockHydraulicHarvester();
        hydraulicHarvesterFrame = new BlockHydraulicHarvesterFrame();
        hydraulicFiller = new BlockHydraulicFiller();

        pressureDisposal = new BlockPressureDisposal();
        blockCore = new BlockHydraulicCore();
        blockValve = new BlockHydraulicValve();
        blockInterfaceValve = new BlockInterfaceValve();

        //blockHose = new BlockHose();
        blockInfiniteSource = new BlockInfiniteSource();
        movingPane = new BlockMovingPane();

        blockLight = new BlockLight();
        blockHydraulicFluidPump = new BlockHydraulicFluidPump();

        blockLead = new BlockLead();
        blockCopper = new BlockCopper();

        portalBase = new BlockPortalBase();
        //portalFrame = new BlockPortalFrame();
        portalTeleporter = new BlockPortalTeleporter();

        blockJarDirt = new BlockJarOfDirt();

        blockCharger = new BlockHydraulicCharger();

        blockAssembler = new BlockAssembler();
        blockRefinedLonezium = new BlockRefinedMineral(Names.blockRefinedLonezium);
        blockRefinedNadsiumBicarbinate = new BlockRefinedMineral(Names.blockRefinedNadsiumBicarbinate);
        blockDirtyMineral = new BlockRefinedMineral(Names.blockDirtyMineral);

        harvesterTrolley = new BlockHarvesterTrolley();
        Hydraulicraft.trolleyRegistrar.registerTrolley(new TrolleyCrops());
        Hydraulicraft.trolleyRegistrar.registerTrolley(new TrolleySugarCane());
        Hydraulicraft.trolleyRegistrar.registerTrolley(new TrolleyNetherWart());
        Hydraulicraft.trolleyRegistrar.registerTrolley(new TrolleyCactus());

        blockRubberWood = new BlockRubberWood();
        blockRubberLeaves = new BlockRubberLeaves();
        blockRubberSapling = new BlockRubberSapling();

        blockFluidTank = new BlockFluidTank();
        blockRubberTap = new BlockRubberTap();
        blockFluidRecombobulator = new BlockFluidRecombobulator();

        blockTreeHarvester = new BlockTreeHarvester();
        blockTreeHarvesterTrolley = new BlockTreeHarvesterTrolley();

        registerBlocks();
    }


    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Registers the blocks to the GameRegistry
     */
    public static void registerBlocks() {

        GameRegistry.registerBlock(hydraulicFrictionIncinerator, HandlerHydraulicBlock.class, Names.blockHydraulicFrictionIncinerator.unlocalized);
        GameRegistry.registerBlock(hydraulicCrusher, HandlerHydraulicBlock.class, Names.blockHydraulicCrusher.unlocalized);
        GameRegistry.registerBlock(hydraulicWasher, HandlerHydraulicBlock.class, Names.blockHydraulicWasher.unlocalized);

        GameRegistry.registerBlock(hydraulicPressureWall, ItemBlock.class, Names.blockHydraulicPressureWall.unlocalized);
        GameRegistry.registerBlock(hydraulicPressureGlass, ItemBlock.class, Names.blockHydraulicPressureGlass.unlocalized);
        GameRegistry.registerBlock(hydraulicFilter, HandlerHydraulicBlock.class, Names.blockHydraulicFilter.unlocalized);
        GameRegistry.registerBlock(hydraulicFiller, HandlerHydraulicBlock.class, Names.blockHydraulicFiller.unlocalized);

        GameRegistry.registerBlock(blockValve, HandlerHydraulicBlock.class, Names.blockValve.unlocalized);
        GameRegistry.registerBlock(blockInterfaceValve, HandlerHydraulicBlock.class, Names.blockInterfaceValve.unlocalized);
        GameRegistry.registerBlock(movingPane, HandlerHydraulicBlock.class, Names.blockMovingPane.unlocalized);

        GameRegistry.registerBlock(hydraulicPressurevat, HandlerPressureVat.class, Names.blockHydraulicPressureReservoir[0].unlocalized);
        GameRegistry.registerBlock(hydraulicPump, HandlerPump.class, Names.blockHydraulicPump[0].unlocalized);
        GameRegistry.registerBlock(hydraulicLavaPump, HandlerLavaPump.class, Names.blockHydraulicLavaPump[0].unlocalized);

        GameRegistry.registerBlock(hydraulicHarvesterSource, HandlerHydraulicBlock.class, Names.blockHydraulicHarvester.unlocalized);
        GameRegistry.registerBlock(hydraulicHarvesterFrame, HandlerHydraulicBlock.class, Names.blockHarvesterFrame.unlocalized);
        GameRegistry.registerBlock(harvesterTrolley, HandlerTrolley.class, Names.blockHarvesterTrolley.unlocalized);
        GameRegistry.registerBlock(hydraulicPiston, HandlerHydraulicBlock.class, Names.blockHydraulicPiston.unlocalized);

        GameRegistry.registerBlock(blockCore, HandlerCoreBlock.class, Names.blockCore[0].unlocalized);

        GameRegistry.registerBlock(blockHydraulicFluidPump, HandlerHydraulicBlock.class, Names.blockHydraulicFluidPump.unlocalized);

        GameRegistry.registerBlock(portalBase, HandlerHydraulicBlock.class, Names.portalBase.unlocalized);
        //GameRegistry.registerBlock(portalFrame, HandlerHydraulicBlock.class, Names.portalFrame.unlocalized);

        GameRegistry.registerBlock(blockJarDirt, HandlerHydraulicBlock.class, Names.blockJarDirt.unlocalized);

        GameRegistry.registerBlock(blockCharger, HandlerHydraulicBlock.class, Names.blockHydraulicCharger.unlocalized);
        GameRegistry.registerBlock(blockAssembler, HandlerHydraulicBlock.class, Names.blockHydraulicAssembler.unlocalized);


        GameRegistry.registerBlock(blockCopper, ItemBlock.class, Names.blockCopper.unlocalized);
        GameRegistry.registerBlock(blockLead, ItemBlock.class, Names.blockLead.unlocalized);

        OreDictionary.registerOre(Names.blockCopper.unlocalized, blockCopper);
        OreDictionary.registerOre(Names.blockLead.unlocalized, blockLead);
        OreDictionary.registerOre("blockGlass", hydraulicPressureGlass);


        GameRegistry.registerBlock(blockLight, HandlerHydraulicBlock.class, Names.blockLight.unlocalized);
        GameRegistry.registerBlock(pressureDisposal, HandlerHydraulicBlock.class, Names.blockPressureDisposal.unlocalized);
        GameRegistry.registerBlock(blockInfiniteSource, HandlerHydraulicBlock.class, Names.blockInfiniteSource.unlocalized);
        GameRegistry.registerBlock(portalTeleporter, HandlerHydraulicBlock.class, Names.portalTeleporter.unlocalized);
        GameRegistry.registerBlock(blockRefinedLonezium, ItemBlock.class, Names.blockRefinedLonezium.unlocalized);
        GameRegistry.registerBlock(blockRefinedNadsiumBicarbinate, ItemBlock.class, Names.blockRefinedNadsiumBicarbinate.unlocalized);
        GameRegistry.registerBlock(blockDirtyMineral, ItemBlock.class, Names.blockDirtyMineral.unlocalized);

        GameRegistry.registerBlock(blockRubberWood, ItemBlock.class, Names.blockRubberWood.unlocalized);
        GameRegistry.registerBlock(blockRubberLeaves, ItemBlock.class, Names.blockRubberLeaves.unlocalized);
        GameRegistry.registerBlock(blockRubberSapling, ItemBlock.class, Names.blockRubberSapling.unlocalized);

        GameRegistry.registerBlock(blockFluidTank, HandlerHydraulicBlock.class, Names.blockFluidTank.unlocalized);
        GameRegistry.registerBlock(blockRubberTap, HandlerHydraulicBlock.class, Names.blockRubberTap.unlocalized);
        GameRegistry.registerBlock(blockFluidRecombobulator, HandlerHydraulicBlock.class, Names.blockFluidRecombobulator.unlocalized);

        GameRegistry.registerBlock(blockTreeHarvester, HandlerHydraulicBlock.class, Names.blockTreeHarvester.unlocalized);
        GameRegistry.registerBlock(blockTreeHarvesterTrolley, HandlerHydraulicBlock.class, Names.blockTreeHarvesterTrolley.unlocalized);
    }
}
