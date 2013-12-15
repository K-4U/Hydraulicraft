package pet.minecraft.Hydraulicraft.baseClasses.entities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.IFluidHandler;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public abstract class TileStorage extends MachineEntity implements IFluidHandler {
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * This will return the max ammount of bar this consumer can handle.
	 */
	public abstract int getMaxBar();
	
	public int getTier(){
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
	}
}
