package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class MachineEntity extends TileEntity {
	
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
