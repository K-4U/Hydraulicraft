package pet.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.lib.Functions;

public abstract class MachineEntity extends TileEntity {
	private boolean _isOilStored = false;
	private int fluidLevelStored = 0;
	
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
	public int getStored(){
		return fluidLevelStored;
	}

	public void setStored(int i, boolean isOil){
		_isOilStored = isOil;
		fluidLevelStored = i;
	}
	
	public boolean isOilStored() {
		return _isOilStored;
	}

	
	private List<MachineEntity> getMachine(List<MachineEntity> list, World w, int x, int y, int z){
		int blockId = w.getBlockId(x, y, z);
		if(blockId == 0){
			return list;
		}
		
		TileEntity t = w.getBlockTileEntity(x, y, z);
		if(t instanceof MachineEntity){
			list.add((MachineEntity)t);
		}
		return list;
	}
	
	public List<MachineEntity> getConnectedBlocks(List<MachineEntity> mainList){
		return getConnectedBlocks(mainList, true);
	}
	
	public List<MachineEntity> getConnectedBlocks(List<MachineEntity> mainList, boolean chain){
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
		

		List<MachineEntity> callList = new ArrayList<MachineEntity>();
		
		for (MachineEntity machineEntity : machines) {
			if(!mainList.contains(machineEntity)){
				mainList.add(machineEntity);
				callList.add(machineEntity);
			}
		}
		if(chain){
			for (MachineEntity machineEntity : callList) {
				List<MachineEntity> tempList = new ArrayList<MachineEntity>();
				tempList = machineEntity.getConnectedBlocks(mainList);
				mainList = Functions.mergeList(tempList, mainList);
			}
		}
		
		return mainList;
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
