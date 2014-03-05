package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorage;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
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
	
	
	private boolean isMultipart;
	private World tWorld;
	private Location blockLocation;
	private TMultiPart tMp;
	private TileEntity tTarget;
	private IHydraulicMachine target;
	
	private boolean hasOwnFluidTank;
	private boolean firstUpdate = true;
	private boolean shouldUpdateNetwork = true;
	private float oldPressure = 0f;
	
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
		tWorld = _target.world();
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
	
	public void updateBlock(){
		if(getWorld() != null && !getWorld().isRemote){
			if(isMultipart && tMp.tile() != null){
		    	MCDataOutput writeStream = tMp.tile().getWriteStream(tMp);
		    	tMp.writeDesc(writeStream);
			}else{
				getWorld().markBlockForUpdate(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
			}
		}
    }

	public void setPressure(float newPressure, ForgeDirection dir){
		if(getWorld() == null) return;
		if(getWorld().isRemote) return;
		
		int compare = Float.compare(getMaxPressure(isOilStored(), dir), newPressure);
		if(compare < 0 && getStored(null) > 0){
			getWorld().createExplosion((Entity)null, getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(),
					1F + ((getMaxPressure(isOilStored(), null) / newPressure) * 3), true);
		
		}else{
			target.setPressure(newPressure, dir);
		}
	}
	
	public float getMaxPressure(boolean isOil, ForgeDirection from){
		if(isMultipart){
			return ((IHydraulicMachine)tMp).getMaxPressure(isOil, from);
		}else{
			return ((IHydraulicMachine)tTarget).getMaxPressure(isOil, from);
		}
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block stores
	 * Will be used to calculate the pressure all over the network.
	 */
	public int getStored(ForgeDirection from){
		if(hasOwnFluidTank){
			if(isMultipart){
				return ((IHydraulicStorageWithTank)tMp).getStored(from);
			}else{
				return ((IHydraulicStorageWithTank)target).getStored(from);
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
		
		_isOilStored = tagCompound.getBoolean("isOilStored");
		fluidInSystem = tagCompound.getInteger("fluidInSystem");
		fluidTotalCapacity = tagCompound.getInteger("fluidTotalCapacity");
		
		oldPressure = tagCompound.getFloat("oldPressure");
		
		if(target.getNetwork(ForgeDirection.UNKNOWN) != null){
			target.getNetwork(ForgeDirection.UNKNOWN).readFromNBT(tagCompound.getCompoundTag("network"));
		}
		
		
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
		
		tagCompound.setBoolean("isOilStored", _isOilStored);
		tagCompound.setInteger("fluidInSystem", fluidInSystem );
		tagCompound.setInteger("fluidTotalCapacity",fluidTotalCapacity);
		
		
		tagCompound.setBoolean("isRedstonePowered", isRedstonePowered);
		
		NBTTagCompound pNetworkCompound = new NBTTagCompound();
		
		if(target.getNetwork(ForgeDirection.UNKNOWN) != null){
			target.getNetwork(ForgeDirection.UNKNOWN).writeToNBT(pNetworkCompound);
			tagCompound.setFloat("oldPressure", target.getNetwork(ForgeDirection.UNKNOWN).getPressure());
		}
		
		tagCompound.setCompoundTag("network", pNetworkCompound);
		
		
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
		if(firstUpdate/* && tWorld!= null && !tWorld.isRemote*/){
			firstUpdate = false;
			target.firstTick();
		}
		if(shouldUpdateNetwork){
			shouldUpdateNetwork = false;
			target.updateNetwork(oldPressure);
		}
		if(tWorld != null){
			if(!tWorld.isRemote){
				
				if(tTarget instanceof IHydraulicConsumer){
					IHydraulicConsumer consumer = (IHydraulicConsumer)tTarget;
			        float less = consumer.workFunction(true, ForgeDirection.UP);
			        if(target.getPressure(ForgeDirection.UNKNOWN) >= less && less > 0){
		                less = consumer.workFunction(false, ForgeDirection.UP);
		                float newPressure = target.getPressure(ForgeDirection.UNKNOWN) - less;
		                updateBlock();
		                target.setPressure(newPressure, ForgeDirection.UNKNOWN);
		                
		                //So.. the water in this block should be going down a bit.
		                if(!isOilStored()){
		                    setStored((int)(getStored(null) - (less * Constants.USING_WATER_PENALTY)), false);
		                }
		            }
		        }else if(tTarget instanceof IHydraulicGenerator){
		        	IHydraulicGenerator gen = (IHydraulicGenerator) tTarget;
		        	gen.workFunction(ForgeDirection.UP);
		        }else if(tTarget instanceof IHydraulicStorage){
		        	if(tTarget.worldObj.getTotalWorldTime() % 40 == 0){
		    			Functions.checkAndFillSideBlocks(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
		    		}
		        }
		        
			}
		}
	}

	@Override
	public void setIsOilStored(boolean b) {
		_isOilStored = b;
	}
	
	private IHydraulicMachine isValidMachine(ForgeDirection dir){
		int x = getBlockLocation().getX() + dir.offsetX;
		int y = getBlockLocation().getY() + dir.offsetY;
		int z = getBlockLocation().getZ() + dir.offsetZ;
		int blockId = getWorld().getBlockId(x, y, z);
		
		TileEntity t = getWorld().getBlockTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			if(((IHydraulicMachine)t).canConnectTo(dir.getOpposite()))
				return (IHydraulicMachine)t;
		}else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir.getOpposite())){
				return Multipart.getTransporter((TileMultipart)t);
			}
		}
		return null;
	}
	
	
	private List<IHydraulicMachine> getConnectedBlocks(){
		int x = getBlockLocation().getX();
		int y = getBlockLocation().getY();
		int z = getBlockLocation().getZ();
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			IHydraulicMachine isValid = null;
			if(isMultipart){
				if(((PartHose)tMp).isConnectedTo(dir)){
					isValid = isValidMachine(dir);					
				}
			}else{
				isValid = isValidMachine(dir);				
			}
			if(isValid != null)
				machines.add(isValid);
		}
		
		return machines;
	}

	@Override
	public void validate(){
		//takeHighestPressure();
	}

	@Override
	public void updateNetworkOnNextTick(float oldPressure) {
		shouldUpdateNetwork = true;
		this.oldPressure = oldPressure;
	}
}
