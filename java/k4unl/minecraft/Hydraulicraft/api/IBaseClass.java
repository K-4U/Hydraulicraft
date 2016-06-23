package k4unl.minecraft.Hydraulicraft.api;

import mcmultipart.multipart.Multipart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * DO NOT IMPLEMENT THIS INTERFACE!
 *
 * @author K-4U
 */
public interface IBaseClass {

    /**
     * Set the max ammount of fluid this block can handle during runtime
     *
     * @param maxFluid The ammount of BUCKETS this block can handle
     */
    void setMaxStorage(int maxFluid);


    /**
     * Forward this function to the Base class
     *
     * @param tagCompound
     */
    void readFromNBTI(NBTTagCompound tagCompound);

    /**
     * Forward this function to the Base class
     *
     * @param tagCompound
     */
    void writeToNBTI(NBTTagCompound tagCompound);

    /**
     * Forward this function to the Base class
     *
     * @param net
     * @param packet
     */
    void onDataPacketI(NetworkManager net, SPacketUpdateTileEntity packet);

    /**
     * Forward this function to the Base class
     *
     * @return
     */
    SPacketUpdateTileEntity getUpdatePacketI();

    /**
     * Gets the ammount of fluid stored
     *
     * @return the ammount of fluid stored
     */
    int getStored();

    /**
     * Sets the amount of fluid stored
     *
     * @param maxStorage
     * @param isOil
     * @param doNotify   Whether or not to tell the client block that the fluid has changed
     */
    void setStored(int maxStorage, boolean isOil, boolean doNotify);

    /**
     * @return if Oil is stored in the tank.
     */
    boolean isOilStored();

    /**
     * Sets if oil is stored or not
     *
     * @param b
     */
    void setIsOilStored(boolean b);

    /**
     * Forward this function the the Base class
     */
    void updateEntityI();

    /**
     * Triggers a world.markBlockForUpdate()
     */
    void updateBlock();

    /**
     * Forward this to the base class
     */
    void invalidateI();

    /**
     * Call this function if you want the network to be updated
     *
     * @param oldPressure
     */
    void updateNetworkOnNextTick(float oldPressure);

    /**
     * Call this function if you want the fluids to be equalized troughout the network
     */
    void updateFluidOnNextTick();

    /**
     * Returns the current pressure
     *
     * @param dir
     * @return
     */
    float getPressure(EnumFacing dir);

    /**
     * Returns the max ammount of pressure this machine can handle
     *
     * @param isOilStored
     * @param from
     * @return
     */
    float getMaxPressure(boolean isOilStored, EnumFacing from);

    /**
     * Returns the max amount of fluid storage for this block
     *
     * @return
     */
    int getMaxStorage();

    /**
     * Sets the pressure in the network
     *
     * @param f
     * @param facing
     */
    void setPressure(float f, EnumFacing facing);

    /**
     * Use this to tell the base class to tell its target.
     * Just use init(this);
     *
     * @param target
     */
    void init(TileEntity target);

    void init(Multipart target);

    /**
     * Takes the pressure to add and checks if there is oil in the system.
     * Use this function to add pressure from a generator
     *
     * @param pressureToAdd
     */
    void addPressureWithRatio(float pressureToAdd, EnumFacing from);

    /**
     * Forward this to the handler.
     */
    void validateI();

    PressureTier getPressureTier();


}
