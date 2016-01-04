package k4unl.minecraft.Hydraulicraft.api;


import net.minecraft.util.EnumFacing;

public interface IHydraulicMachine {
	IBaseClass getHandler();
	
	/**
	 * Called whenever the fluid level has changed
	 */
	void onFluidLevelChanged(int old);
	
	/**
	 * Function that gets called to check if a network can connect here.
	 * @param side
	 * @return
	 */
	boolean canConnectTo(EnumFacing side);
}
