package pet.minecraft.Hydraulicraft.baseClasses.entities;

import net.minecraft.nbt.NBTTagCompound;
import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

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
    public abstract int getBar();
    
    public abstract int getStorage();
    
    @Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
	}
	
}
