package k4unl.minecraft.Hydraulicraft.api;

import net.minecraftforge.common.ForgeDirection;

public interface IHydraulicTransporter extends IHydraulicMachine {
	public boolean isConnectedTo(ForgeDirection dir);
	
}
