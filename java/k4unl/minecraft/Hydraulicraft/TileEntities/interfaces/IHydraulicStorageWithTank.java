package k4unl.minecraft.Hydraulicraft.TileEntities.interfaces;


public interface IHydraulicStorageWithTank extends IHydraulicStorage {
	public int getStored();

	void setStored(int i, boolean isOil);
	
}
