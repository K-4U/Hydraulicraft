package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileConsumer extends MachineEntity {
	private IHydraulicConsumer target;
	
	public TileConsumer(TileEntity _target){
		super(_target);
		target = (IHydraulicConsumer)_target;
	}

	
	@Override
    public void updateEntity(){
		if(!tTarget.worldObj.isRemote){
	        float less = target.workFunction(true);
	        if(target.getPressure(ForgeDirection.UNKNOWN) >= less && less > 0){
                less = target.workFunction(false);
                float newPressure = target.getPressure(ForgeDirection.UNKNOWN) - less;
                updateBlock();
                target.setPressure(newPressure, ForgeDirection.UNKNOWN);
                //Functions.checkSidesSetPressure(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord, newPressure);
                
                //So.. the water in this block should be going done a bit.
                if(!isOilStored()){
                    setStored((int)(getStored(null) - (less * Constants.USING_WATER_PENALTY)), false);
                    //Functions.checkAndFillSideBlocks(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
                }
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
