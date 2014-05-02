package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;


public interface IHydraulicStorageWithTank extends IHydraulicStorage {
	public int getStored();

	void setStored(int i, boolean isOil);
	
}
