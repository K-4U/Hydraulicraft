package pet.minecraft.Hydraulicraft.client.renderers;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import cpw.mods.fml.client.registry.ClientRegistry;

public class Renderers {
	public static void init(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicHose.class, new RendererHydraulicHose());
	}
}
