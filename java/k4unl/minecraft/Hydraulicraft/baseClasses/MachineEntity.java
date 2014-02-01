package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import codechicken.microblock.FaceMicroblock;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.LoadController;

public class MachineEntity implements IBaseClass {
	private boolean _isOilStored = false;
	private int fluidLevelStored = 0;
	private int fluidInSystem = 0;
	private int fluidTotalCapacity = 0;
	private boolean isRedstonePowered = false;
	
	private float bar = 0;
	private int networkCount;
	
	public boolean isMultipart;
	public World tWorld;
	public Location blockLocation;
	public TMultiPart tMp;
	public TileEntity tTarget;
	public IHydraulicMachine target;
	
	public boolean hasOwnFluidTank;
		
	public MachineEntity(TileEntity _target) {
		tTarget = _target;
		target = (IHydraulicMachine) _target;
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		tMp = null;
		tWorld = _target.worldObj;
		blockLocation = new Location(_target.xCoord, _target.yCoord, _target.zCoord);
	}
	
	public MachineEntity(TMultiPart _target) {
		tMp = _target;
		tTarget = null;
		target = (IHydraulicMachine) _target;
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		isMultipart = true;
		tWorld = _target.world();
		blockLocation = new Location(_target.x(), _target.y(), _target.z());
	}
	
	public void redstoneChanged(boolean rsPowered){
		
	}
	
	public void dropItemStackInWorld(ItemStack itemStack){
		if(itemStack != null){
			EntityItem ei = new EntityItem(tWorld);
			ei.setEntityItemStack(itemStack);
			ei.setPosition(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
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
		if(tWorld!= null){
			tWorld.markBlockForUpdate(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
		}
    }

	public void setPressure(float newPressure){
		if(tWorld == null) return;
		if(tWorld.isRemote) return;
		
		int compare = Float.compare(bar, newPressure);
		if((int)getMaxPressure(isOilStored()) < (int)newPressure && getStored() > 0){
			tWorld.createExplosion((Entity)null, blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(),
					1F + ((getMaxPressure(isOilStored()) / newPressure) * 3), true);
		
		//if((int)getMaxPressure(isOilStored()) < (int)newPressure){
			//bar = getMaxPressure(isOilStored());
		}else{
			bar = newPressure;
			if(compare != 0){
				disperse();
			}
		}
		
		
		
		//if(tTarget instanceof IHydraulicTransporter){
			
		//}
        updateBlock();
	}
	
	public float getPressure(){
		if(bar < 0 || bar != bar){
			bar = 0;
		}
		return bar;
	}
	
	
	public float getMaxPressure(boolean isOil){
		if(isMultipart){
			return ((IHydraulicMachine)tMp).getMaxPressure(isOil);
		}else{
			return ((IHydraulicMachine)tTarget).getMaxPressure(isOil);
		}
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block stores
	 * Will be used to calculate the pressure all over the network.
	 */
	public int getStored(){
		if(hasOwnFluidTank){
			if(isMultipart){
				return ((IHydraulicStorageWithTank)tMp).getStored();
			}else{
				return ((IHydraulicStorageWithTank)target).getStored();
			}
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
		if(i < 0)
			i = 0;
		if(hasOwnFluidTank){
			if(isMultipart){
				((IHydraulicStorageWithTank)tMp).setStored(i, isOil);
			}else{
				((IHydraulicStorageWithTank)target).setStored(i, isOil);
			}
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
		return new Packet132TileEntityData(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(), 4, tagCompound);
	}

	
	private List<IHydraulicMachine> getMachine(List<IHydraulicMachine> list, ForgeDirection dir){
		if(tWorld == null) return list;
		
		int x = blockLocation.getX() + dir.offsetX;
		int y = blockLocation.getY() + dir.offsetY;
		int z = blockLocation.getZ() + dir.offsetZ;
		int blockId = tWorld.getBlockId(x, y, z);
		if(blockId == 0){
			return list;
		}
		
		TileEntity t = tWorld.getBlockTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			list.add((IHydraulicMachine)t);
		}else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir)){
				list.add(Multipart.getTransporter((TileMultipart)t));
			}
		}
		return list;
	}
	
	@Override
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList){
		return getConnectedBlocks(mainList, true);
	}
	
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList, boolean chain){
		int x = blockLocation.getX();
		int y = blockLocation.getY();
		int z = blockLocation.getZ();
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(isMultipart){
				TMultiPart mp = tMp.tile().partMap(Functions.getIntDirFromDirection(dir));
				if(!(mp instanceof FaceMicroblock)){
					machines = getMachine(machines, dir);					
				}
			}else{
				machines = getMachine(machines, dir);				
			}
		}

		List<IHydraulicMachine> callList = new ArrayList<IHydraulicMachine>();
		
		for (IHydraulicMachine machineEntity : machines) {
			if(!mainList.contains(machineEntity)){
				if(isMultipart){
					mainList.add(machineEntity);
					callList.add(machineEntity);
				}
				if(machineEntity instanceof PartHose){
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
		if(isMultipart){
			((IHydraulicMachine)tMp).readNBT(tagCompound);
		}else{
			target.readNBT(tagCompound);
		}
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
		
		if(isMultipart){
			((IHydraulicMachine)tMp).writeNBT(tagCompound);
		}else{
			target.writeNBT(tagCompound);
		}
	}
	
	protected TileEntity getBlockTileEntity(int x, int y, int z){
		return tWorld.getBlockTileEntity(x, y, z);
	}

	public void checkRedstonePower() {
		boolean isIndirectlyPowered = tWorld.isBlockIndirectlyGettingPowered(blockLocation.getX(), blockLocation.getY(), blockLocation.getZ());
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
		//disperse();
	}

	@Override
	public void setIsOilStored(boolean b) {
		_isOilStored = b;
	}
	
	@Override
	public void disperse() {
		//Should get connected blocks and set the pressure there.
		List<IHydraulicMachine> connectedBlocks =  new ArrayList<IHydraulicMachine>();
		connectedBlocks = getConnectedBlocks(connectedBlocks, false);
		
		for (IHydraulicMachine machine : connectedBlocks) {
			machine.getHandler().setPressure(getPressure());
		}
	}
	
	private IHydraulicMachine getLowestPressureMachine(List<IHydraulicMachine> list){
		float foundPressure = Float.NaN;
		IHydraulicMachine foundMachine = null;
		for (IHydraulicMachine machine : list) {
			int compare = Float.compare(foundPressure, machine.getHandler().getPressure());
			if(compare > 0 || foundPressure != foundPressure){
				foundPressure = machine.getHandler().getPressure();
				foundMachine = machine;
			}
		}
		return foundMachine;
	}
	
	private IHydraulicMachine getHighestPressureMachine(List<IHydraulicMachine> list){
		float foundPressure = 0.0F;
		IHydraulicMachine foundMachine = null;
		for (IHydraulicMachine machine : list) {
			int compare = Float.compare(machine.getHandler().getPressure(), foundPressure);
			if(compare > 0){
				foundPressure = machine.getHandler().getPressure();
				foundMachine = machine;
			}
		}
		return foundMachine;
	}

	public void takeHighestPressure() {
		List<IHydraulicMachine> connectedBlocks =  new ArrayList<IHydraulicMachine>();
		connectedBlocks = getConnectedBlocks(connectedBlocks, false);
		
		float foundPressure = 0.0F;
		for (IHydraulicMachine machine : connectedBlocks) {
			int compare = Float.compare(machine.getHandler().getPressure(), foundPressure);
			if(compare > 0){
				foundPressure = machine.getHandler().getPressure();
			}
		}
		setPressure(foundPressure);
	}
	
	@Override
	public void validate(){
		//takeHighestPressure();
	}
}
