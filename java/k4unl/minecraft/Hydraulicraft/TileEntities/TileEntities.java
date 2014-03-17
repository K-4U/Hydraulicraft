package k4unl.minecraft.Hydraulicraft.TileEntities;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TilePressureDisposal;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicLavaPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
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
		
	}
}
