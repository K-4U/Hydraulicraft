package k4unl.minecraft.Hydraulicraft.api;

import net.minecraftforge.common.ForgeDirection;

public interface IHydraulicConsumer extends IHydraulicMachine {
	/**
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Function that gets called to let the machine do its work
	 * if Simulate is true, the machine shouldn't be doing anything. just returning how much it would've used.
	 * Returns the amount of pressure that gets lost when doing this.
	 */
	
	public float workFunction(boolean simulate);
	
	public void onBlockBreaks();
	
	
}
