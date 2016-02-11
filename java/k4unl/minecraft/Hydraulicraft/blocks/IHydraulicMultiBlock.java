package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;

import java.util.List;

public interface IHydraulicMultiBlock {

    List<TileHydraulicValve> getValves();
}
