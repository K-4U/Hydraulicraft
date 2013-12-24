package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileTransporter;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class MachineEntity extends TileEntity {
	private boolean _isOilStored = false;
	private int fluidLevelStored = 0;
	private int fluidInSystem = 0;
	private int fluidTotalCapacity = 0;
	
	private float bar = 0;
	private int networkCount;
	
	
	public void setNetworkCount(int newCount){
		networkCount = newCount;
	}
	
	public int getNetworkCount() {
		return networkCount;
	}

	public void updateBlock(){
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

	public void setPressure(float newPressure){
		if((int)getMaxPressure() < (int)newPressure){
			worldObj.createExplosion((Entity)null, xCoord, yCoord, zCoord, 1F + ((getMaxPressure() / newPressure) * 3), true);
		}
		if((int)getMaxPressure() < (int)newPressure){
			bar = getMaxPressure();
		}else{
			bar = newPressure;
		}
        updateBlock();
	}
	
	public float getPressure(){
		return bar;
	}
	
	
	public float getMaxPressure(){
		//All of the blocks will have the max tier of pressure.. For now!
		if(!isOilStored()){
			return Constants.MAX_MBAR_WATER_TIER_3;
		}else{
			return Constants.MAX_MBAR_OIL_TIER_3;
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


	public void setTotalFluidCapacity(int totalFluidCapacity) {
		fluidTotalCapacity = totalFluidCapacity;
        updateBlock();
	}
	
	public void setFluidInSystem(int i){
		fluidInSystem = i;
        updateBlock();
	}
	
	public int getTotalFluidCapacity() {
		return fluidTotalCapacity;
	}
	
	public int getFluidInSystem(){
		return fluidInSystem;
	}
	
	public void setStored(int i, boolean isOil){
		_isOilStored = isOil;
		fluidLevelStored = i;
        updateBlock();
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
				if(this instanceof TileTransporter || machineEntity instanceof TileTransporter){
					mainList.add(machineEntity);
					callList.add(machineEntity);
				}
			}
		}
		//Only go trough transporter items.
		if(chain){
			for (MachineEntity machineEntity : callList) {
				//if(machineEntity instanceof TileTransporter){
					List<MachineEntity> tempList = new ArrayList<MachineEntity>();
					tempList = machineEntity.getConnectedBlocks(mainList);
					mainList = Functions.mergeList(tempList, mainList);
				//}
			}
		}
		
		return mainList;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		fluidLevelStored = tagCompound.getInteger("fluidLevelStored");
		networkCount = tagCompound.getInteger("networkCount");
		
		_isOilStored = tagCompound.getBoolean("isOilStored");
		fluidInSystem = tagCompound.getInteger("fluidInSystem");
		fluidTotalCapacity = tagCompound.getInteger("fluidTotalCapacity");
		
		bar = tagCompound.getFloat("bar");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("fluidLevelStored",fluidLevelStored);
		tagCompound.setInteger("networkCount", networkCount);
		
		tagCompound.setBoolean("isOilStored", _isOilStored);
		tagCompound.setInteger("fluidInSystem", fluidInSystem );
		tagCompound.setInteger("fluidTotalCapacity",fluidTotalCapacity);
		
		tagCompound.setFloat("bar", bar);
	}
	
	protected TileEntity getBlockTileEntity(int x, int y, int z){
		return worldObj.getBlockTileEntity(x, y, z);
	}



	
	
}
