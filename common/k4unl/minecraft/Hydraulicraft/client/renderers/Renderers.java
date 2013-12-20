package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import cpw.mods.fml.client.registry.ClientRegistry;

public class Renderers {
	public static void init(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicHose.class, new RendererHydraulicHose());
	}
}
