package k4unl.minecraft.Hydraulicraft.baseClasses.entities;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.IBaseTransporter;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;


public class TileTransporter extends MachineEntity implements IBaseTransporter{
    private final int storedLiquid = 0;
    private IHydraulicTransporter target;
    
    public TileTransporter(TileEntity _target){
    	super(_target);
    	target = (IHydraulicTransporter)_target;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        super.writeToNBT(tagCompound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(){
        return AxisAlignedBB.getAABBPool().getAABB(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord, tTarget.xCoord + 1, tTarget.yCoord + 1, tTarget.zCoord + 1);
    }
}
