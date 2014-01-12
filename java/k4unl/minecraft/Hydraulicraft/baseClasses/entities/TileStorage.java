package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import k4unl.minecraft.Hydraulicraft.api.IBaseStorage;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import net.minecraft.tileentity.TileEntity;

public class TileStorage extends MachineEntity implements IBaseStorage {

	public TileStorage(TileEntity _target) {
		super(_target);
	}
	
	public void updateEntity(){
		Functions.checkAndFillSideBlocks(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
	}
}
