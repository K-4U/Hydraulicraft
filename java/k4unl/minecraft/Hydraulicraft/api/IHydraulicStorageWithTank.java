package k4unl.minecraft.Hydraulicraft.api;

import net.minecraftforge.common.ForgeDirection;

public interface IHydraulicStorageWithTank extends IHydraulicStorage {
	public int getStored(ForgeDirection from);

	void setStored(int i, boolean isOil);
	
}
