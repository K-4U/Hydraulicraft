package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;


public interface IHydraulicStorageWithTank extends IHydraulicStorage {

    int getStored();

    void setStored(int i, boolean isOil);

}
