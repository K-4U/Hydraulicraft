package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

public interface IHydraulicTransporter extends IHydraulicMachine {
	public boolean isConnectedTo(ForgeDirection dir);
	
	public boolean getCanGoOverMax();
	
	public void setCanGoOverMax(boolean newValue);
}
