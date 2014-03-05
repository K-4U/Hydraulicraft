package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;

public interface IHydraulicMachine {
	/**
	 * Will be used to calculate the pressure all over the network.
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * @return How much liquid this block can store.
	 */
	public abstract int getMaxStorage();
	
	/**
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * @param isOil Whether or not oil is stored.
	 * @param from TODO
	 * @return The max amount of pressure(bar) this machine can handle.
	 */
	public float getMaxPressure(boolean isOil, ForgeDirection from);

	/**
	 * Called when the block has been broken.
	 * @author Koen Beckers
	 * @date 29-12-2013
	 */
	public void onBlockBreaks();

	public IBaseClass getHandler();
	
	/**
	 * Forward this function to the Base class
	 * @param tagCompound
	 */
	public void readFromNBT(NBTTagCompound tagCompound);
	
	/**
	 * Forward this function to the Base class
	 * @param tagCompound
	 */
	public void writeToNBT(NBTTagCompound tagCompound);
	
	/**
	 * Used to read NBT. Called from handler.
	 * @param tagCompound
	 */
	public void readNBT(NBTTagCompound tagCompound);
	
	/**
	 * Used to write NBT. Called from handler
	 * @param tagCompound
	 */
	public void writeNBT(NBTTagCompound tagCompound);
	
	/**
	 * Forward this function to the Base class
	 * @param net
	 * @param packet
	 */
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet);
	
	/**
	 * Forward this function to the Base class
	 * @return
	 */
	public Packet getDescriptionPacket();
	
	/**
	 * Forward this function to the base class.
	 */
	public void updateEntity();
	
	/**
	 * Forward this function to the base class.
	 */
	public void validate();
	
	/**
	 * Called whenever the pressure has changed
	 */
	public void onPressureChanged(float old);
	
	/**
	 * Called whenever the fluid level has changed
	 */
	public void onFluidLevelChanged(int old);
	
	/**
	 * Function that gets called to check if a network can connect here.
	 * @param side
	 * @return
	 */
	public boolean canConnectTo(ForgeDirection side);


	/**
	 * Function that only triggers on the first tick!
	 */
	public void firstTick();
	
	public PressureNetwork getNetwork(ForgeDirection side);
	
	public void setNetwork(ForgeDirection side, PressureNetwork toSet);
	
	public float getPressure(ForgeDirection from);
	
	public void setPressure(float newPressure, ForgeDirection side);

}
