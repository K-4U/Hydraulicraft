package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.TileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorage;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork.networkEntry;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

public class MachineEntity implements IBaseClass {
	private boolean _isOilStored = false;
	private int fluidLevelStored = 0;
	private boolean isRedstonePowered = false;
	
	private float pressure = 0F;
	
	/* FMP private boolean isMultipart; */
	private World tWorld;
	private Location blockLocation;
	/* FMP private TMultiPart tMp; */
	private TileEntity tTarget;
	private IHydraulicMachine target;
	
	private boolean hasOwnFluidTank;
	private boolean firstUpdate = true;
	private float oldPressure = 0f;
	
	private boolean shouldUpdateNetwork = true;
	private boolean shouldUpdateFluid = false;
	
	public MachineEntity(TileEntity _target) {
		tTarget = _target;
		target = (IHydraulicMachine) _target;
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		/* FMP tMp = null;*/
		tWorld = _target.getWorldObj();
		
	}
	/* FMP 
	public MachineEntity(TMultiPart _target) {
		tMp = _target;
		tTarget = null;
		target = (IHydraulicMachine) _target;
		tWorld = _target.world();
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		isMultipart = true;
		
	}*/
	
	public Location getBlockLocation(){
		if(blockLocation == null){
			/* FMP if(isMultipart){
				if(tMp.tile() != null){
					blockLocation = new Location(tMp.x(), tMp.y(), tMp.z());
				}
			}else{ */
				blockLocation = new Location(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
			//}
		}
		return blockLocation;
	}
	
	public World getWorld(){
		if(tWorld == null){
			/* FMP if(isMultipart){
				tWorld = tMp.world();
			}else{ */
				tWorld = tTarget.getWorldObj();
			//}
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
			getWorld().spawnEntityInWorld(ei);
		}
	}
	
	public boolean getRedstonePowered(){
		return isRedstonePowered;
	}
	
	public void updateBlock(){
		if(getWorld() != null && !getWorld().isRemote){
			/* FMP if(isMultipart && tMp.tile() != null){
		    	MCDataOutput writeStream = tMp.tile().getWriteStream(tMp);
		    	tMp.writeDesc(writeStream);
			}else{*/
				getWorld().markBlockForUpdate(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
			//}
		}
    }

	public void checkPressure(ForgeDirection dir){
		if(getWorld() == null) return;
		if(getWorld().isRemote) return;
		
		float newPressure = target.getPressure(dir);
		int compare = Float.compare(getMaxPressure(isOilStored(), dir), newPressure);
		if(compare < 0 && getStored() > 0){
			getWorld().createExplosion((Entity)null, getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(),
					0.6F + ((getMaxPressure(isOilStored(), null) / newPressure)), true);
			if(isOilStored()){
				getWorld().setBlock(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(), Fluids.fluidOilBlock);
			}else{
				getWorld().setBlock(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(), FluidRegistry.WATER.getBlock());
			}
		}
	}
	
	public float getMaxPressure(boolean isOil, ForgeDirection from){
		/* FMP if(isMultipart){
			return ((IHydraulicMachine)tMp).getMaxPressure(isOil, from);
		}else{*/
			return ((IHydraulicMachine)tTarget).getMaxPressure(isOil, from);
		//}
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 15-12-2013
	 * Will return how much liquid this block stores
	 * Will be used to calculate the pressure all over the network.
	 */
	public int getStored(){
		if(hasOwnFluidTank){
			/* FMP if(isMultipart){
				return ((IHydraulicStorageWithTank)tMp).getStored();
			}else{*/
				return ((IHydraulicStorageWithTank)target).getStored();
			//}
		}else{
			return fluidLevelStored;
		}
	}

	public void setStored(int i, boolean isOil, boolean doNotify){
		if(tWorld == null) return;
		if(tWorld.isRemote) return;
		if(i < 0)
			i = 0;
		_isOilStored = isOil;
		if(hasOwnFluidTank){
			/* FMP if(isMultipart){
				((IHydraulicStorageWithTank)tMp).setStored(i, isOil);
			}else{ */
				((IHydraulicStorageWithTank)target).setStored(i, isOil);
			//}
		}else{
			target.onFluidLevelChanged(fluidLevelStored);
			if(fluidLevelStored != i && doNotify == true){
				//if new = 0 that probably means me setting it like that..
				//shouldUpdateFluid = true;
			}
			fluidLevelStored = i;
		}
		updateBlock();
	}
	
	public boolean isOilStored() {
		return _isOilStored;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet){
		NBTTagCompound tagCompound = packet.func_148857_g();
		this.readFromNBT(tagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(), 4, tagCompound);
	}

	private IHydraulicMachine getMachine(ForgeDirection dir){
		if(getWorld() == null) return null;
		
		int x = getBlockLocation().getX() + dir.offsetX;
		int y = getBlockLocation().getY() + dir.offsetY;
		int z = getBlockLocation().getZ() + dir.offsetZ;
		Block block = getWorld().getBlock(x, y, z);
		if(block.blockRegistry.getNameForObject(block).equals("air")){
			return null;
		}
		
		TileEntity t = getWorld().getTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			if(((IHydraulicMachine)t).canConnectTo(dir.getOpposite()))
				return (IHydraulicMachine)t;
		}/* FMP else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir.getOpposite())){
				return Multipart.getTransporter((TileMultipart)t);
			}
		}*/
		return null;
	}
	
	private List<IHydraulicMachine> getMachineList(List<IHydraulicMachine> list, ForgeDirection dir){
		if(getWorld() == null) return list;
		
		int x = getBlockLocation().getX() + dir.offsetX;
		int y = getBlockLocation().getY() + dir.offsetY;
		int z = getBlockLocation().getZ() + dir.offsetZ;
		Block block = getWorld().getBlock(x, y, z);
		if(block.blockRegistry.getNameForObject(block).equals("air")){
			return null;
		}
		
		TileEntity t = getWorld().getTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			if(((IHydraulicMachine)t).canConnectTo(dir.getOpposite()) && !list.contains(t))
				list.add((IHydraulicMachine)t);
		}/* FMP else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir.getOpposite()) && !list.contains(t)){
				list.add(Multipart.getTransporter((TileMultipart)t));
			}
		}*/
		return list;
	}
	
	@Override
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList){
		if(target.getNetwork(ForgeDirection.UP) == null){
			return mainList;
		}
		
		List<networkEntry> entryList = new ArrayList<networkEntry>();
		entryList = target.getNetwork(ForgeDirection.UP).getMachines();
		
		for (networkEntry entry : entryList) {
			Location loc = entry.getLocation();
			TileEntity ent = getWorld().getTileEntity(loc.getX(), loc.getY(), loc.getZ());
			if(ent instanceof IHydraulicMachine){
				IHydraulicMachine machine = (IHydraulicMachine) ent;
				mainList.add(machine);
			}
		}
		
		return mainList;
	}
	
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList, boolean chain){
		int x = getBlockLocation().getX();
		int y = getBlockLocation().getY();
		int z = getBlockLocation().getZ();
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			/* FMP if(isMultipart){
				if(((PartHose)tMp).isConnectedTo(dir)){
					machines = getMachineList(machines, dir);
					
				}
			}else{*/
				machines = getMachineList(machines, dir);				
			//}
		}

		List<IHydraulicMachine> callList = new ArrayList<IHydraulicMachine>();
		
		for (IHydraulicMachine machineEntity : machines) {
			if(!mainList.contains(machineEntity)){
				/* FMP if(isMultipart){
					mainList.add(machineEntity);
					//callList.add(machineEntity);
					//chain = true;
				}*/
				if(machineEntity instanceof IHydraulicTransporter){
					mainList.add(machineEntity);
					callList.add(machineEntity);	
					chain = true;
				}
				if(machineEntity instanceof TileHydraulicValve){
					IHydraulicMachine target =((TileHydraulicValve)machineEntity).getTarget(); 
					if(target != null){
						mainList.add(target);
						if(target instanceof IMachineMultiBlock){
							List<TileHydraulicValve> valves = ((IMachineMultiBlock)target).getValves();
							for(TileHydraulicValve valve : valves){
								if(!valve.equals(machineEntity)){
									callList.add(valve);
								}
							}
							chain = true;
						}
					}
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
		
		oldPressure = tagCompound.getFloat("oldPressure");
		
		if(getWorld() != null && !getWorld().isRemote){
			shouldUpdateNetwork = tagCompound.getBoolean("shouldUpdateNetwork");
			if(shouldUpdateNetwork){
				updateNetworkOnNextTick(oldPressure);
			}
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
				if(target.getNetwork(dir) != null){
					target.getNetwork(dir).readFromNBT(tagCompound.getCompoundTag("network" + dir.ordinal()));
				}else{
					//updateNetworkOnNextTick(oldPressure);
				}
			}
		}else if(getWorld() != null && getWorld().isRemote){
			pressure = tagCompound.getFloat("pressure");
		}

		isRedstonePowered = tagCompound.getBoolean("isRedstonePowered");
		/*if(isMultipart){
			((IHydraulicMachine)tMp).readNBT(tagCompound);
		}else{*/
			target.readNBT(tagCompound);
		//}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		tagCompound.setInteger("fluidLevelStored",fluidLevelStored);
		
		tagCompound.setBoolean("isOilStored", _isOilStored);
		
		tagCompound.setBoolean("isRedstonePowered", isRedstonePowered);
		
		if(getWorld() != null && !getWorld().isRemote){
			tagCompound.setBoolean("shouldUpdateNetwork", shouldUpdateNetwork);
		
			if(target.getNetwork(ForgeDirection.UP) != null){
				tagCompound.setFloat("oldPressure", target.getNetwork(ForgeDirection.UP).getPressure());
				tagCompound.setFloat("pressure", target.getNetwork(ForgeDirection.UP).getPressure());
			}
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
				if(target.getNetwork(dir) != null){
					NBTTagCompound pNetworkCompound = new NBTTagCompound();
					target.getNetwork(dir).writeToNBT(pNetworkCompound);
					tagCompound.setTag("network" + dir.ordinal(), pNetworkCompound);
				}
			}
			
		}
		
		/* FMP if(isMultipart){
			((IHydraulicMachine)tMp).writeNBT(tagCompound);
		}else{*/
			target.writeNBT(tagCompound);
		//}
	}
	
	protected TileEntity getTileEntity(int x, int y, int z){
		return getWorld().getTileEntity(x, y, z);
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
		if(firstUpdate && tWorld!= null && !tWorld.isRemote){
			firstUpdate = false;
			shouldUpdateNetwork = true;
			target.firstTick();
			checkRedstonePower();
		}
		if(getWorld() != null){
			if(!getWorld().isRemote){
				if(shouldUpdateNetwork){
					shouldUpdateNetwork = false;
					target.updateNetwork(oldPressure);
				}
				if(shouldUpdateFluid && getWorld().getTotalWorldTime() % 5 == 0 && target.getNetwork(ForgeDirection.UNKNOWN) != null){
					shouldUpdateFluid = false;
					target.getNetwork(ForgeDirection.UNKNOWN).updateFluid(target);
				}
				
				if(getWorld().getTotalWorldTime() % 2 == 0){
					updateBlock();
				}
				
				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
					if(target.getNetwork(dir) != null){
						if(getWorld().getTotalWorldTime() % 2 == 0 && Config.get("explosions")){
							checkPressure(dir);
						}
						
						if(tTarget instanceof IHydraulicConsumer){
							IHydraulicConsumer consumer = (IHydraulicConsumer)tTarget;
							if(consumer.canWork(dir)){
						        float less = consumer.workFunction(true, dir);
						        if(target.getPressure(dir) >= less && less > 0){
					                less = consumer.workFunction(false, dir);
					                float newPressure = target.getPressure(dir) - less;
					                updateBlock();
					                target.setPressure(newPressure, dir);
					                
					                //So.. the water in this block should be going down a bit.
					                if(!isOilStored()){
					                    setStored((int)(getStored() - (less * Constants.USING_WATER_PENALTY)), false, true);
					                    shouldUpdateFluid = true;
					                }
					            }
							}
				        }else if(tTarget instanceof IHydraulicGenerator){
				        	IHydraulicGenerator gen = (IHydraulicGenerator) tTarget;
				        	if(gen.canWork(dir)){
				        		gen.workFunction(dir);
				        	}
				        }else if(tTarget instanceof IHydraulicStorage){
				        	if(getWorld().getTotalWorldTime() % 40 == 0 && dir.equals(ForgeDirection.UP)){
				    			//Functions.checkAndFillSideBlocks(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
				    		}
				        }
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
		//Block block = getWorld().getBlock(x, y, z);
		
		TileEntity t = getWorld().getTileEntity(x, y, z);
		if(t instanceof IHydraulicMachine){
			if(((IHydraulicMachine)t).canConnectTo(dir.getOpposite()))
				return (IHydraulicMachine)t;
		} /* FMP else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir.getOpposite())){
				return Multipart.getTransporter((TileMultipart)t);
			}
		}*/
		return null;
	}
	
	
	private List<IHydraulicMachine> getConnectedBlocks(){
		/*int x = getBlockLocation().getX();
		int y = getBlockLocation().getY();
		int z = getBlockLocation().getZ();*/
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			IHydraulicMachine isValid = null;
			/* FMP if(isMultipart){
				if(((PartHose)tMp).isConnectedTo(dir)){
					isValid = isValidMachine(dir);					
				}
			}else{*/
				isValid = isValidMachine(dir);
			//}
			if(isValid != null){
				if(isValid instanceof TileHydraulicValve){
					machines.add(((TileHydraulicValve)isValid).getTarget());
				}
				machines.add(isValid);
			}
		}
		
		return machines;
	}

	@Override
	public void validate(){
		
	}

	@Override
	public void updateNetworkOnNextTick(float oldPressure) {
		if(getWorld() != null && !getWorld().isRemote){
			shouldUpdateNetwork = true;
			this.oldPressure = oldPressure;
			updateBlock();
		}
	}

	@Override
	public void updateFluidOnNextTick() {
		if(!getWorld().isRemote){
			shouldUpdateFluid = true;
		}
	}

	@Override
	public float getPressure() {
		return pressure;
	}
}
