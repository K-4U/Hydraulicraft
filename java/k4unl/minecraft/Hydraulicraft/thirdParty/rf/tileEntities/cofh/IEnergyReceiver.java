package k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.cofh;

import net.minecraft.util.EnumFacing;

/**
 * @author Koen Beckers (K-4U)
 */
public interface IEnergyReceiver {
    
    int receiveEnergy(EnumFacing from, int maxReceive,
                      boolean simulate);

    boolean canConnectEnergy(EnumFacing from);

    int getEnergyStored(EnumFacing from);

    int getMaxEnergyStored(EnumFacing from);
}
