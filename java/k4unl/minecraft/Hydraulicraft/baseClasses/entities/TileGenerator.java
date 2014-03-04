package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import codechicken.nei.WorldOverlayRenderer;
import k4unl.minecraft.Hydraulicraft.api.IBaseGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileGenerator extends MachineEntity implements IBaseGenerator{

	private IHydraulicGenerator target;
	private TileEntity tTarget;
    
    
    public TileGenerator(TileEntity _target) {
		super(_target);
		tTarget = _target;
		target = (IHydraulicGenerator)_target;
	}

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
	public float getMaxPressure(boolean isOil, ForgeDirection from){
		if(isOil){
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
		return tTarget.worldObj.getBlockMetadata(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
	}
	
	
	public void updateEntity(){
		super.updateEntity();
		if(tTarget.worldObj.isRemote) return;
		
		//Call work function:
		target.workFunction();
		//Set own pressure
		/*float prevPressure = target.getPressure(ForgeDirection.UNKNOWN);
		float gen = target.getGenerating();
		int comp = Float.compare(target.getPressure(ForgeDirection.UNKNOWN) + gen, getMaxPressure(target.getHandler().isOilStored(), ForgeDirection.UNKNOWN));
		if(comp < 0){
			target.setPressure(target.getPressure(ForgeDirection.UNKNOWN) + gen,ForgeDirection.UNKNOWN);
		}else if(Float.compare(gen, 0.0F) > 0){
			target.setPressure(getMaxPressure(target.getHandler().isOilStored(), ForgeDirection.UNKNOWN), ForgeDirection.UNKNOWN);
		}*/
		updateBlock();
		/*
		if(getPressure() != prevPressure){
			//Functions.checkSidesSetPressure(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord, getPressure());
		}*/
		
	}
	
}
