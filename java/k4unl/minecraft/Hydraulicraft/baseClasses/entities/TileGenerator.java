package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileGenerator extends MachineEntity {
    /*!
     * @author Koen Beckers
     * @date 14-12-2013
     * Function that gets called when there's work to be done
     * Probably every tick or so.
     */
    public abstract void workFunction();
    
    
    /*!
     * @author Koen Beckers
     * @date 14-12-2013
     * Returns how much the generator can max output
     */
    public abstract int getMaxGenerating();
    
    
    /*!
     * @author Koen Beckers
     * @date 14-12-2013
     * Returns how much the generator is now generating
     */
    public abstract float getGenerating();
    
    
    @Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * This will return the max ammount of bar this consumer can handle.
	 */
	@Override
	public float getMaxPressure(){
		if(isOilStored()){
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_OIL_TIER_3;
			}			
		}else{
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_WATER_TIER_3;
			}	
		}
		return 0;
	}
	
	public int getTier(){
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
}
