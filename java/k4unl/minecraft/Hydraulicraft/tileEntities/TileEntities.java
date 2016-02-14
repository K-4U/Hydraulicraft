package k4unl.minecraft.Hydraulicraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.*;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.*;
import k4unl.minecraft.Hydraulicraft.tileEntities.rubberHarvesting.TileRubberTap;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileFluidTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureReservoir;
import k4unl.minecraft.Hydraulicraft.tileEntities.worldgen.TileRubberWood;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntities {

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Initializes all the tile entities.
     */
    public static void init() {

        GameRegistry.registerTileEntity(TileHydraulicPump.class, "tileHydraulicPump");
        GameRegistry.registerTileEntity(TileHydraulicLavaPump.class, "tileHydraulicLavaPump");
        GameRegistry.registerTileEntity(TileHydraulicFrictionIncinerator.class, "tileHydraulicFrictionIncinerator");
        GameRegistry.registerTileEntity(TileHydraulicPressureReservoir.class, "tileHydraulicPressureVat");
        GameRegistry.registerTileEntity(TileHydraulicCrusher.class, "tileHydraulicCrusher");
        GameRegistry.registerTileEntity(TileHydraulicWasher.class, "tileHydraulicWasher");
        GameRegistry.registerTileEntity(TileHydraulicFilter.class, "tileHydraulicMixer");
        GameRegistry.registerTileEntity(TileHydraulicPiston.class, "tileHydraulicPiston");
        GameRegistry.registerTileEntity(TilePressureDisposal.class, "tilePressureDisposal");
        GameRegistry.registerTileEntity(TileHydraulicValve.class, "tileHydraulicValve");
        GameRegistry.registerTileEntity(TileInterfaceValve.class, "tileInterfaceValve");
        GameRegistry.registerTileEntity(TileHydraulicCharger.class, "tileHydraulicCharger");

        GameRegistry.registerTileEntity(TileHydraulicHarvester.class, "tileHydraulicHarvester");
        GameRegistry.registerTileEntity(TileHarvesterTrolley.class, "tileHarvesterTrolley");

        //GameRegistry.registerTileEntity(TilePressureHose.class, "tilePressureHose");
        GameRegistry.registerTileEntity(TileInfiniteSource.class, "tileInfiniteSource");
        GameRegistry.registerTileEntity(TileMovingPane.class, "tileMovingPane");
        GameRegistry.registerTileEntity(TileHydraulicFluidPump.class, "tileWaterPump");

        GameRegistry.registerTileEntity(TilePortalBase.class, "tilePortalBase");
        GameRegistry.registerTileEntity(TilePortalTeleporter.class, "tilePortalTeleporter");

        GameRegistry.registerTileEntity(TileChunkLoader.class, "tileChunkLoader");
        GameRegistry.registerTileEntity(TileJarOfDirt.class, "tileJarOfDirt");
        GameRegistry.registerTileEntity(TileAssembler.class, "tileHydraulicAssembler");

        GameRegistry.registerTileEntity(TileHydraulicFiller.class, "tileHydraulicFiller");

        GameRegistry.registerTileEntity(TileFluidTank.class, "tileFluidTank");
        GameRegistry.registerTileEntity(TileRubberWood.class, "tileRubberWood");
        GameRegistry.registerTileEntity(TileRubberTap.class, "tileRubberTap");

    }
}
