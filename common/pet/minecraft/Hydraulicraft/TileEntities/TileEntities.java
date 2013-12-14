package pet.minecraft.Hydraulicraft.TileEntities;

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
	}
}
