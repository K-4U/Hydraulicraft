package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.util.AxisAlignedBB;

public interface IHydraulicTransporter extends IHydraulicMachine {
	public AxisAlignedBB getRenderBoundingBox();
	
	public IBaseTransporter getHandler();
}
