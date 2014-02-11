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
import codechicken.lib.data.MCDataOutput;
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
	private boolean firstUpdate = true;
		
	public MachineEntity(TileEntity _target) {
		tTarget = _target;
		target = (IHydraulicMachine) _target;
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		tMp = null;
		tWorld = _target.worldObj;
		
	}
	
	public MachineEntity(TMultiPart _target) {
		tMp = _target;
		tTarget = null;
		target = (IHydraulicMachine) _target;
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		isMultipart = true;
		
	}
	
	public Location getBlockLocation(){
		if(blockLocation == null){
			if(isMultipart){
				blockLocation = new Location(tMp.x(), tMp.y(), tMp.z());				
			}else{
				blockLocation = new Location(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
			}
		}
		return blockLocation;
	}
	
	public World getWorld(){
		if(tWorld == null){
			if(isMultipart){
				tWorld = tMp.world();
			}else{
				tWorld = tTarget.worldObj;
			}
		}
		return tWorld;
	}
	
	public void redstoneChanged(boolean rsPowered){
		
	}
	
	public void dropItemStackInWorld(ItemStack itemStack){
		if(itemStack != null){
			EntityItem ei = new EntityItem(getWorld());
			ei.setEntityItemStack(itemStack);
			ei.setPosition(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
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
		if(getWorld() != null && !getWorld().isRemote){
			if(isMultipart){
		    	MCDataOutput writeStream = tMp.tile().getWriteStream(tMp);
		    	tMp.writeDesc(writeStream);
			}else{
				getWorld().markBlockForUpdate(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
			}
		}
    }

	public void setPressure(float newPressure){
		if(getWorld() == null) return;
		if(getWorld().isRemote) return;
		
		int compare = Float.compare(bar, newPressure);
		if((int)getMaxPressure(isOilStored()) < (int)newPressure && getStored() > 0){
			getWorld().createExplosion((Entity)null, getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(),
					1F + ((getMaxPressure(isOilStored()) / newPressure) * 3), true);
		
		//if((int)getMaxPressure(isOilStored()) < (int)newPressure){
			//bar = getMaxPressure(isOilStored());
		}else{
			bar = newPressure;
			if(compare != 0){
				disperse();
			}
		}
		
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
		return new Packet132TileEntityData(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(), 4, tagCompound);
	}

	
	private List<IHydraulicMachine> getMachine(List<IHydraulicMachine> list, ForgeDirection dir){
		if(getWorld() == null) return list;
		
		int x = getBlockLocation().getX() + dir.offsetX;
		int y = getBlockLocation().getY() + dir.offsetY;
		int z = getBlockLocation().getZ() + dir.offsetZ;
		int blockId = getWorld().getBlockId(x, y, z);
		if(blockId == 0){
			return list;
		}
		
		TileEntity t = getWorld().getBlockTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			if(((IHydraulicMachine)t).canConnectTo(dir.getOpposite()) && !list.contains(t))
				list.add((IHydraulicMachine)t);
		}else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir.getOpposite()) && !list.contains(t)){
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
		int x = getBlockLocation().getX();
		int y = getBlockLocation().getY();
		int z = getBlockLocation().getZ();
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(isMultipart){
				if(((PartHose)tMp).isConnectedTo(dir)){
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
					//callList.add(machineEntity);
					//chain = true;
				}
				if(machineEntity instanceof PartHose){
					//mainList.add(machineEntity);
					callList.add(machineEntity);	
					chain = true;
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
		return getWorld().getBlockTileEntity(x, y, z);
	}

	public void checkRedstonePower() {
		boolean isIndirectlyPowered = getWorld().isBlockIndirectlyGettingPowered(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
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
		if(firstUpdate){
			firstUpdate = false;
		}
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
