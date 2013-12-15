package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class MachineEntity extends TileEntity {
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block can store.
	 * Will be used to calculate the pressure all over the network.
	 */
	public abstract int getStorage();
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block stores
	 * Will be used to calculate the pressure all over the network.
	 */
	public abstract int getStored();
	
	public abstract int getConnectedBlocks();
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
	}
	
	protected TileEntity getBlockTileEntity(int x, int y, int z){
		return worldObj.getBlockTileEntity(x, y, z);
	}
}
