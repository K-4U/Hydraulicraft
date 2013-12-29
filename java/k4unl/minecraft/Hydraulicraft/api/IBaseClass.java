package k4unl.minecraft.Hydraulicraft.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;

/**
 * DO NOT IMPLEMENT THIS INTERFACE!
 * @author K-4U
 */
public interface IBaseClass {
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

	public int getStored();

	public boolean isOilStored();

	public float getPressure();

	public boolean getRedstonePowered();

	public void dropItemStackInWorld(ItemStack inputInventory);
	
	public List<IHydraulicMachine> getConnectedBlocks(
			List<IHydraulicMachine> mainList);

	public void setPressure(float newPressure);

	public void setStored(int maxStorage, boolean isOil);

	public void setFluidInSystem(int fluidInSystem);

	public int getNetworkCount();

	public void setNetworkCount(int size);

	public void setTotalFluidCapacity(int totalFluidCapacity);
	
	public void updateEntity();

	public int getTotalFluidCapacity();

	public int getFluidInSystem();

	public void checkRedstonePower();
	
	public void updateBlock();
}
