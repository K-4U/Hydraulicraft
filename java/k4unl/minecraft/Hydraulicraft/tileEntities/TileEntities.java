package k4unl.minecraft.Hydraulicraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TilePressureDisposal;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.tileEntities.transporter.TilePressureHose;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes all the tile entities.
	 */
	public static void init(){
		GameRegistry.registerTileEntity(TileHydraulicPump.class, "tileHydraulicPump");
		GameRegistry.registerTileEntity(TileHydraulicLavaPump.class, "tileHydraulicLavaPump");
		GameRegistry.registerTileEntity(TileHydraulicFrictionIncinerator.class, "tileHydraulicFrictionIncinerator");
		GameRegistry.registerTileEntity(TileHydraulicPressureVat.class, "tileHydraulicPressureVat");
		GameRegistry.registerTileEntity(TileHydraulicCrusher.class, "tileHydraulicCrusher");
		GameRegistry.registerTileEntity(TileHydraulicWasher.class, "tileHydraulicWasher");
		GameRegistry.registerTileEntity(TileHydraulicMixer.class, "tileHydraulicMixer");
		GameRegistry.registerTileEntity(TileHydraulicPiston.class, "tileHydraulicPiston");
		GameRegistry.registerTileEntity(TilePressureDisposal.class, "tilePressureDisposal");
		GameRegistry.registerTileEntity(TileHydraulicValve.class, "tileHydraulicValve");
		GameRegistry.registerTileEntity(TileInterfaceValve.class, "tileInterfaceValve");
		
		
		GameRegistry.registerTileEntity(TileHarvesterFrame.class, "tileHarvesterFrame");
		GameRegistry.registerTileEntity(TileHydraulicHarvester.class, "tileHydraulicHarvester");
		GameRegistry.registerTileEntity(TileHarvesterTrolley.class, "tileHarvesterTrolley");
		
		GameRegistry.registerTileEntity(TilePressureHose.class, "tilePressureHose");
		GameRegistry.registerTileEntity(TileInfiniteSource.class, "tileInfiniteSource");
		GameRegistry.registerTileEntity(TileMovingPane.class, "tileMovingPane");
		
	}
}
