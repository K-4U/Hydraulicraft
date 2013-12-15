package pet.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class MachineEntity extends TileEntity {
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block can store.
	 * Will be used to calculate the pressure all over the network.
	 */
	public abstract int getStorage();
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block stores
	 * Will be used to calculate the pressure all over the network.
	 */
	public abstract int getStored();
	
	private List<MachineEntity> getMachine(List<MachineEntity> list, World w, int x, int y, int z){
		TileEntity t = w.getBlockTileEntity(x, y, z);
		if(t instanceof MachineEntity){
			list.add((MachineEntity)t);
		}
		return list;
	}
	
	
	
	public List<MachineEntity> getConnectedBlocks(MachineEntity caller){
		//It should check the connecting blocks
		//And check how much liquid they have
		//Get liquid from them
		
		int x = xCoord;
		int y = yCoord;
		int z = zCoord;
		List<MachineEntity> machines = new ArrayList<MachineEntity>();
		machines = getMachine(machines, worldObj, x-1, y, z);
		machines = getMachine(machines, worldObj, x+1, y, z); 
		machines = getMachine(machines, worldObj, x, y-1, z);
		machines = getMachine(machines, worldObj, x, y+1, z);
		machines = getMachine(machines, worldObj, x, y, z-1);
		machines = getMachine(machines, worldObj, x, y, z+1);
		
		//Remove the caller from the equation
		if(caller != null){
			machines.remove(caller);
		}
		
		//Get own entity
		MachineEntity ownEntity = (MachineEntity)worldObj.getBlockTileEntity(x, y, z);
		
		
		List<MachineEntity> retList = new ArrayList<MachineEntity>();
		for (MachineEntity machineEntity : machines) {
			retList.add(machineEntity);
			List<MachineEntity> tempList = new ArrayList<MachineEntity>();
			tempList = machineEntity.getConnectedBlocks(ownEntity);
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
	
	protected TileEntity getBlockTileEntity(int x, int y, int z){
		return worldObj.getBlockTileEntity(x, y, z);
	}
}
