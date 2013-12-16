package pet.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.lib.Functions;
import pet.minecraft.Hydraulicraft.lib.config.Constants;

public abstract class MachineEntity extends TileEntity {
	private boolean _isOilStored = false;
	private int fluidLevelStored = 0;
	private int bar = 0;
	private int fluidInSystem = 0;
	
	public void setPressure(int newPressure){
		bar = newPressure;
	}
	
	public int getPressure(){
		return bar;
	}
	
	public int getMaxPressure(){
		//All of the blocks will have the max tier of pressure.. For now!
		if(!isOilStored()){
			return Constants.MAX_BAR_WATER_TIER_3;
		}else{
			return Constants.MAX_BAR_OIL_TIER_3;
		}
	}
	
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

	public void setFluidInSystem(int i){
		fluidInSystem = i;
	}
	
	public void setStored(int i, boolean isOil){
		_isOilStored = isOil;
		fluidLevelStored = i;
	}
	
	public boolean isOilStored() {
		return _isOilStored;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		NBTTagCompound tagCompound = packet.data;
		this.readFromNBT(tagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord,yCoord,zCoord,4,tagCompound);
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
		
		fluidLevelStored = tagCompound.getInteger("fluidLevelStored");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("fluidLevelStored",fluidLevelStored);
	}
	
	protected TileEntity getBlockTileEntity(int x, int y, int z){
		return worldObj.getBlockTileEntity(x, y, z);
	}

	
	
}
