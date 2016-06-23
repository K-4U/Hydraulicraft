package k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.cofh;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Koen Beckers (K-4U)
 */
public class EnergyStorage {
    
    public EnergyStorage(int i) {

    }

    public EnergyStorage(int i, int maxTransferRf) {

    }

    public int extractEnergy(int rfUsage, boolean b) {

        return rfUsage;
    }

    public int getEnergyStored() {

        return 0;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {

    }

    public void writeToNBT(NBTTagCompound tagCompound) {

    }

    public int getMaxEnergyStored() {
        return 0;
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    public int getMaxExtract() {
        return 0;
    }
}
