package k4unl.minecraft.Hydraulicraft.thirdParty.rf;

/**
 * @author Koen Beckers (K-4U)
 */
public interface IEnergyInfo {

    /**
     * Returns energy usage/generation per tick (RF/t).
     */
    int getInfoEnergyPerTick();

    /**
     * Returns maximum energy usage/generation per tick (RF/t).
     */
    int getInfoMaxEnergyPerTick();

    /**
     * Returns energy stored (RF).
     */
    int getInfoEnergyStored();

    /**
     * Returns maximum energy stored (RF).
     */
    int getInfoMaxEnergyStored();

}
