package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;

import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import net.minecraft.util.EnumFacing;

public interface ICustomNetwork {

    void updateNetwork(float oldPressure);

    PressureNetwork getNetwork(EnumFacing dir);

    void setNetwork(EnumFacing side, PressureNetwork toSet);
}
