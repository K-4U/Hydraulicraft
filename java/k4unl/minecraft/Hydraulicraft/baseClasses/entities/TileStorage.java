package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import k4unl.minecraft.Hydraulicraft.api.IBaseStorage;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileStorage extends MachineEntity implements IBaseStorage {
	private boolean hasReadNBT = false;
	
	
	public TileStorage(TileEntity _target) {
		super(_target);
	}
	
	
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		hasReadNBT = true;
	}
	
	public void updateEntity(){
		//if(hasReadNBT){
		if(tTarget.worldObj.getTotalWorldTime() % 40 == 0){
			Functions.checkAndFillSideBlocks(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
		}
		//}
	}
}
