package pet.minecraft.Hydraulicraft.baseClasses.entities;

import net.minecraft.nbt.NBTTagCompound;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.Functions;
import pet.minecraft.Hydraulicraft.lib.config.Constants;

public abstract class TileConsumer extends MachineEntity {
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Function that gets called to let the machine do its work
	 * if Simulate is true, the machine shouldn't be doing anything. just returning how much it would've used.
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
		float less = workFunction(true); 
		if(getPressure() > less && less > 0){
			less = workFunction(false);
			float newPressure = getPressure() - less;
			setPressure(newPressure);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			Functions.checkSidesSetPressure(worldObj, xCoord, yCoord, zCoord, newPressure);
			//So.. the water in this block should be going done a bit.
			if(isOilStored()){
				setStored((int)(getStored() - (less * Constants.USING_WATER_PENALTY)), false);
				Functions.checkAndFillSideBlocks(worldObj, xCoord, yCoord, zCoord);
			}
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
