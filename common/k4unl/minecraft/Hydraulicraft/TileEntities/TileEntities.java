package k4unl.minecraft.Hydraulicraft.TileEntities;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Initializes all the tile entities.
	 */
	public static void init(){
		GameRegistry.registerTileEntity(TileHydraulicPump.class, "tileHydraulicPump");
		GameRegistry.registerTileEntity(TileHydraulicHose.class, "tileHydraulicHose");
		GameRegistry.registerTileEntity(TileHydraulicFrictionIncinerator.class, "tileHydraulicFrictionIncinerator");
		GameRegistry.registerTileEntity(TileHydraulicPressureVat.class, "tileHydraulicPressureVat");
		GameRegistry.registerTileEntity(TileHydraulicCrusher.class, "tileHydraulicCrusher");
		GameRegistry.registerTileEntity(TileHydraulicWasher.class, "tileHydraulicWasher");
		GameRegistry.registerTileEntity(TileHydraulicMixer.class, "TileHydraulicMixer");
	}
}
