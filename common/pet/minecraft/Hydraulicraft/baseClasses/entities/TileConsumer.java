package pet.minecraft.Hydraulicraft.baseClasses.entities;

import net.minecraft.nbt.NBTTagCompound;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.Functions;

public abstract class TileConsumer extends MachineEntity {
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Function that gets called to let the machine do its work
	 * Returns the ammount of pressure that gets lost when doing this.
	 */
	
	public abstract float workFunction(boolean simulate);
	
	
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * This will return the max ammount of bar this consumer can handle.
	 */
	public abstract int getMaxBar();
	
	
	@Override
	public void updateEntity(){
		if(getPressure() > workFunction(true)){
			float less = workFunction(false);
			float newPressure = getPressure() - less;
			Functions.checkSidesSetPressure(worldObj, xCoord, yCoord, zCoord, newPressure);
		}
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
