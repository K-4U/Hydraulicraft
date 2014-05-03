package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;

import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import net.minecraftforge.common.util.ForgeDirection;

public interface ICustomNetwork {
	public void updateNetwork(float oldPressure);
	public PressureNetwork getNetwork(ForgeDirection dir);
	public void setNetwork(ForgeDirection side, PressureNetwork toSet);
}
