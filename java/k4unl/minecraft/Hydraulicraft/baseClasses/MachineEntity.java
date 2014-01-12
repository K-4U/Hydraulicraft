package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileTransporter;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MachineEntity implements IBaseClass {
	private boolean _isOilStored = false;
	private int fluidLevelStored = 0;
	private int fluidInSystem = 0;
	private int fluidTotalCapacity = 0;
	private boolean isRedstonePowered = false;
	
	private float bar = 0;
	private int networkCount;
	
	public TileEntity tTarget;
	public IHydraulicMachine target;
	
	public boolean hasOwnFluidTank;
		
	public MachineEntity(TileEntity _target) {
		tTarget = _target;
		target = (IHydraulicMachine) _target;
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
	}
	
	public void redstoneChanged(boolean rsPowered){
		
	}
	
	public void dropItemStackInWorld(ItemStack itemStack){
		if(itemStack != null){
			EntityItem ei = new EntityItem(tTarget.worldObj);
			ei.setEntityItemStack(itemStack);
			ei.setPosition(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
			tTarget.worldObj.spawnEntityInWorld(ei);
		}
	}
	
	public boolean getRedstonePowered(){
		return isRedstonePowered;
	}
	
	public void setNetworkCount(int newCount){
		networkCount = newCount;
	}
	
	public int getNetworkCount() {
		return networkCount;
	}

	public void updateBlock(){
		tTarget.worldObj.markBlockForUpdate(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
    }

	public void setPressure(float newPressure){
		if((int)getMaxPressure(isOilStored()) < (int)newPressure){
			tTarget.worldObj.createExplosion((Entity)null, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord,
					1F + ((getMaxPressure(isOilStored()) / newPressure) * 3), true);
		}
		if((int)getMaxPressure(isOilStored()) < (int)newPressure){
			bar = getMaxPressure(isOilStored());
		}else{
			bar = newPressure;
		}
        updateBlock();
	}
	
	public float getPressure(){
		return bar;
	}
	
	
	public float getMaxPressure(boolean isOil){
		return ((IHydraulicMachine)tTarget).getMaxPressure(isOil);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block stores
	 * Will be used to calculate the pressure all over the network.
	 */
	public int getStored(){
		if(hasOwnFluidTank){
			return ((IHydraulicStorageWithTank)target).getStored();
		}else{
			return fluidLevelStored;
		}
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
		if(hasOwnFluidTank){
			((IHydraulicStorageWithTank)target).setStored(i, isOil);
		}else{
			_isOilStored = isOil;
			fluidLevelStored = i;
		}
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
		return new Packet132TileEntityData(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord, 4, tagCompound);
	}

	
	private List<IHydraulicMachine> getMachine(List<IHydraulicMachine> list, World w, int x, int y, int z){
		int blockId = w.getBlockId(x, y, z);
		if(blockId == 0){
			return list;
		}
		
		TileEntity t = w.getBlockTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			list.add((IHydraulicMachine)t);
		}
		return list;
	}
	
	@Override
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList){
		return getConnectedBlocks(mainList, true);
	}
	
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList, boolean chain){
		int x = tTarget.xCoord;
		int y = tTarget.yCoord;
		int z = tTarget.zCoord;
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		machines = getMachine(machines, tTarget.worldObj, x-1, y, z);
		machines = getMachine(machines, tTarget.worldObj, x+1, y, z); 
		machines = getMachine(machines, tTarget.worldObj, x, y-1, z);
		machines = getMachine(machines, tTarget.worldObj, x, y+1, z);
		machines = getMachine(machines, tTarget.worldObj, x, y, z-1);
		machines = getMachine(machines, tTarget.worldObj, x, y, z+1);
		

		List<IHydraulicMachine> callList = new ArrayList<IHydraulicMachine>();
		
		for (IHydraulicMachine machineEntity : machines) {
			if(!mainList.contains(machineEntity)){
				if(target instanceof IHydraulicTransporter || machineEntity instanceof IHydraulicTransporter){
					mainList.add(machineEntity);
					callList.add(machineEntity);
				}
			}
		}
		//Only go trough transporter items.
		if(chain){
			for (IHydraulicMachine machineEntity : callList) {
				//if(machineEntity instanceof TileTransporter){
					List<IHydraulicMachine> tempList = new ArrayList<IHydraulicMachine>();
					tempList = machineEntity.getHandler().getConnectedBlocks(mainList);
					mainList = Functions.mergeList(tempList, mainList);
				//}
			}
		}
		
		return mainList;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		fluidLevelStored = tagCompound.getInteger("fluidLevelStored");
		networkCount = tagCompound.getInteger("networkCount");
		
		_isOilStored = tagCompound.getBoolean("isOilStored");
		fluidInSystem = tagCompound.getInteger("fluidInSystem");
		fluidTotalCapacity = tagCompound.getInteger("fluidTotalCapacity");
		
		bar = tagCompound.getFloat("bar");
		
		isRedstonePowered = tagCompound.getBoolean("isRedstonePowered");
		
		target.readNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		tagCompound.setInteger("fluidLevelStored",fluidLevelStored);
		tagCompound.setInteger("networkCount", networkCount);
		
		tagCompound.setBoolean("isOilStored", _isOilStored);
		tagCompound.setInteger("fluidInSystem", fluidInSystem );
		tagCompound.setInteger("fluidTotalCapacity",fluidTotalCapacity);
		
		tagCompound.setFloat("bar", bar);
		
		tagCompound.setBoolean("isRedstonePowered", isRedstonePowered);
		
		target.writeNBT(tagCompound);
	}
	
	protected TileEntity getBlockTileEntity(int x, int y, int z){
		return tTarget.worldObj.getBlockTileEntity(x, y, z);
	}

	public void checkRedstonePower() {
		boolean isIndirectlyPowered = tTarget.worldObj.isBlockIndirectlyGettingPowered(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
		if(isIndirectlyPowered && !isRedstonePowered){
			isRedstonePowered = true;
			this.redstoneChanged(isRedstonePowered);
		}else if(isRedstonePowered && !isIndirectlyPowered){
			isRedstonePowered = false;
			this.redstoneChanged(isRedstonePowered);
		}
	}

	@Override
	public void updateEntity() {
	}

	@Override
	public void setIsOilStored(boolean b) {
		_isOilStored = b;
	}
}
