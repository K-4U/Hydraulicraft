package k4unl.minecraft.Hydraulicraft.tileEntities;

import codechicken.lib.data.MCDataOutput;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.api.*;
import k4unl.minecraft.Hydraulicraft.blocks.IHydraulicMultiBlock;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork.networkEntry;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHydraulicStorage;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.List;

public class TileHydraulicBase extends TileEntity implements IBaseClass {
    private boolean _isOilStored      = false;
    private int     fluidLevelStored  = 0;
    private boolean isRedstonePowered = false;

    private float pressure = 0F;

    private boolean           isMultipart;
    private World             tWorld;
    private Location          blockLocation;
    private TMultiPart        tMp;
    private TileEntity        tTarget;
    private IHydraulicMachine target;

    private boolean hasOwnFluidTank;
    private boolean firstUpdate = true;
    private float   oldPressure = 0f;

    private boolean shouldUpdateNetwork = true;
    private boolean shouldUpdateFluid   = false;

    private PressureTier pressureTier;
    private int maxStorage = 0;
    protected List<ForgeDirection> connectedSides;
    protected PressureNetwork      pNetwork;
    private   int                  fluidInNetwork;
    private   int                  networkCapacity;


    /**
     * @param _pressureTier The tier of pressure.
     * @param _maxStorage The max ammount of Fluid/10 this machine can store.
     */
    public TileHydraulicBase(PressureTier _pressureTier, int _maxStorage) {

        pressureTier = _pressureTier;
        maxStorage = _maxStorage;
        connectedSides = new ArrayList<ForgeDirection>();
    }

    public void init(TileEntity _target) {

        tTarget = _target;
        target = (IHydraulicMachine) _target;
        if (target instanceof TileHydraulicPressureVat) {
            hasOwnFluidTank = true;
        }
        tWorld = _target.getWorldObj();
    }

    public IBaseClass getHandler() {

        return this;
    }

    public Location getBlockLocation() {

        if (blockLocation == null) {
            if (isMultipart) {
                if (tMp.tile() != null) {
                    blockLocation = new Location(tMp.x(), tMp.y(), tMp.z());
                }
            } else {
                blockLocation = new Location(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
            }
        }
        return blockLocation;
    }

    public World getWorld() {

        if (tWorld == null) {
            if (isMultipart) {
                tWorld = tMp.world();
            } else {
                if (tTarget != null) {
                    tWorld = tTarget.getWorldObj();
                }
            }
        }
        return tWorld;
    }

    public void redstoneChanged(boolean rsPowered) {

    }

    public void dropItemStackInWorld(ItemStack itemStack) {

        if (itemStack != null) {
            EntityItem ei = new EntityItem(getWorld());
            ei.setEntityItemStack(itemStack);
            ei.setPosition(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
            getWorld().spawnEntityInWorld(ei);
        }
    }

    public boolean getRedstonePowered() {

        return getIsRedstonePowered();
    }

    public void updateBlock() {

        if (getWorld() != null && !getWorld().isRemote) {
            if (isMultipart && tMp.tile() != null) {
                MCDataOutput writeStream = tMp.tile().getWriteStream(tMp);
                tMp.writeDesc(writeStream);
            } else {
                getWorld().markBlockForUpdate(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
            }
        }
    }

    public void checkPressure(ForgeDirection dir) {

        if (getWorld() == null) return;
        if (getWorld().isRemote) return;

        float newPressure = getPressure(dir);
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
		if(isOil){
			switch(pressureTier){
			case LOWPRESSURE:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case MEDIUMPRESSURE:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case HIGHPRESSURE:
				return Constants.MAX_MBAR_OIL_TIER_3;
			case INVALID:
				return 0; //BOOM! hehehe
			}			
		}else{
			switch(pressureTier){
			case LOWPRESSURE:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case MEDIUMPRESSURE:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case HIGHPRESSURE:
				return Constants.MAX_MBAR_WATER_TIER_3;
			case INVALID:
				return 0; //BOOM! hehehe
			}	
		}
		return 0;
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

	public void setStored(int i, boolean isOil, boolean doNotify){
		if(getWorld() == null) return;
		if(getWorld().isRemote) return;
		if(i < 0)
			i = 0;
		_isOilStored = isOil;
		if(hasOwnFluidTank){
			if(isMultipart){
				((IHydraulicStorageWithTank)tMp).setStored(i, isOil);
			}else{
				((IHydraulicStorageWithTank)target).setStored(i, isOil);
			}
		}else{
			target.onFluidLevelChanged(fluidLevelStored);
			
			if(fluidLevelStored != i && doNotify == true){
				//if new = 0 that probably means me setting it like that..
				//shouldUpdateFluid = true;
			}
			fluidLevelStored = i;
		}
		markDirty();
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
		if(block instanceof BlockAir){
			return null;
		}
		
		TileEntity t = getWorld().getTileEntity(x, y, z);
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
	
	private List<IHydraulicMachine> getMachineList(List<IHydraulicMachine> list, ForgeDirection dir){
		if(getWorld() == null) return list;
		
		int x = getBlockLocation().getX() + dir.offsetX;
		int y = getBlockLocation().getY() + dir.offsetY;
		int z = getBlockLocation().getZ() + dir.offsetZ;
		Block block = getWorld().getBlock(x, y, z);
		if(block instanceof BlockAir){
			return null;
		}
		
		TileEntity t = getWorld().getTileEntity(x, y, z);
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
	
	public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList){
		if(getNetwork(ForgeDirection.UP) == null){
			return mainList;
		}
		
		List<networkEntry> entryList = new ArrayList<networkEntry>();
		entryList = getNetwork(ForgeDirection.UP).getMachines();
		
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
		List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(isMultipart){
				if(((PartHose)tMp).isConnectedTo(dir)){
					machines = getMachineList(machines, dir);
					
				}
			}else{
				machines = getMachineList(machines, dir);				
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
				if(machineEntity instanceof IHydraulicTransporter){
					mainList.add(machineEntity);
					callList.add(machineEntity);	
					chain = true;
				}
				if(machineEntity instanceof TileHydraulicValve){
					IHydraulicMachine target =((TileHydraulicValve)machineEntity).getTarget(); 
					if(target != null){
						mainList.add(target);
						if(target instanceof IHydraulicMultiBlock){
							List<TileHydraulicValve> valves = ((IHydraulicMultiBlock)target).getValves();
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
					tempList = ((TileHydraulicBase)machineEntity.getHandler()).getConnectedBlocks(mainList);
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
		_isOilStored = tagCompound.getBoolean("isOilStored");
		oldPressure = tagCompound.getFloat("oldPressure");
		pressureTier = PressureTier.fromOrdinal(tagCompound.getInteger("pressureTier"));
		maxStorage = tagCompound.getInteger("maxStorage");
		networkCapacity = tagCompound.getInteger("networkCapacity");
		fluidInNetwork = tagCompound.getInteger("fluidInNetwork");
		
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()){
			shouldUpdateNetwork = tagCompound.getBoolean("shouldUpdateNetwork");
			if(shouldUpdateNetwork){
				updateNetworkOnNextTick(oldPressure);
			}
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
				if(getNetwork(dir) != null){
					getNetwork(dir).readFromNBT(tagCompound.getCompoundTag("network" + dir.ordinal()));
				}else{
					//updateNetworkOnNextTick(oldPressure);
				}
			}
		}else if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
			pressure = tagCompound.getFloat("pressure");
		}

		setRedstonePowered(tagCompound.getBoolean("isRedstonePowered"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		if(!isMultipart && getClass() != TileHydraulicBase.class){
			super.writeToNBT(tagCompound);
		}
		tagCompound.setInteger("fluidLevelStored",fluidLevelStored);
		tagCompound.setBoolean("isOilStored", _isOilStored);
		tagCompound.setBoolean("isRedstonePowered", getIsRedstonePowered());
		tagCompound.setInteger("pressureTier", pressureTier.ordinal());
		tagCompound.setInteger("maxStorage", maxStorage);
		
		
		if(getWorld() != null && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			tagCompound.setBoolean("shouldUpdateNetwork", shouldUpdateNetwork);
		
			if(getNetwork(ForgeDirection.UP) != null){
				tagCompound.setFloat("oldPressure", getNetwork(ForgeDirection.UP).getPressure());
				tagCompound.setFloat("pressure", getNetwork(ForgeDirection.UP).getPressure());
			}
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
				if(getNetwork(dir) != null){
					NBTTagCompound pNetworkCompound = new NBTTagCompound();
					getNetwork(dir).writeToNBT(pNetworkCompound);
					tagCompound.setTag("network" + dir.ordinal(), pNetworkCompound);
				}
			}
            if(pNetwork != null){
                tagCompound.setInteger("networkCapacity", getNetwork(ForgeDirection.UP).getFluidCapacity());
                tagCompound.setInteger("fluidInNetwork", getNetwork(ForgeDirection.UP).getFluidInNetwork());
            }
		}
	}
	
	protected TileEntity getTileEntity(int x, int y, int z){
		return getWorld().getTileEntity(x, y, z);
	}

	public void checkRedstonePower() {
		boolean isIndirectlyPowered = getWorld().isBlockIndirectlyGettingPowered(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
		if(isIndirectlyPowered && !getIsRedstonePowered()){
			setRedstonePowered(true);
			this.redstoneChanged(getIsRedstonePowered());
		}else if(getIsRedstonePowered() && !isIndirectlyPowered){
			setRedstonePowered(false);
			this.redstoneChanged(getIsRedstonePowered());
		}
	}

	@Override
	public void updateEntity() {
		if(firstUpdate && tWorld!= null && !tWorld.isRemote){
			firstUpdate = false;
			shouldUpdateNetwork = true;
			firstTick();
			checkRedstonePower();
		}
		if(getWorld() != null){
			if(!getWorld().isRemote){
				if(shouldUpdateNetwork){
					shouldUpdateNetwork = false;
					if(tMp instanceof ICustomNetwork || tTarget instanceof ICustomNetwork){
						if(isMultipart){
							((ICustomNetwork)tMp).updateNetwork(oldPressure);
						}else{
							((ICustomNetwork)tTarget).updateNetwork(oldPressure);
						}
					}else{
						updateNetwork(oldPressure);
					}
				}
				if(shouldUpdateFluid && getWorld().getTotalWorldTime() % 5 == 0 && getNetwork(ForgeDirection.UNKNOWN) != null){
					shouldUpdateFluid = false;
					getNetwork(ForgeDirection.UNKNOWN).updateFluid(target);
				}
				
				if(getWorld().getTotalWorldTime() % 10 == 0){
					updateBlock();
				}
				
				for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
					if(getNetwork(dir) != null){
						if(getWorld().getTotalWorldTime() % 2 == 0 && HCConfig.INSTANCE.getBool("explosions")){
							checkPressure(dir);
						}
						
						if(tTarget instanceof IHydraulicConsumer){
							IHydraulicConsumer consumer = (IHydraulicConsumer)tTarget;
							if(consumer.canWork(dir)){
						        float less = consumer.workFunction(true, dir);
						        if(getPressure(dir) >= less && less > 0){
					                less = consumer.workFunction(false, dir);
					                float newPressure = getPressure(dir) - less;
					                updateBlock();
					                setPressure(newPressure, dir);
					                
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
	public void setPressure(float newPressure, ForgeDirection dir) {
		getNetwork(dir).setPressure(newPressure);
	}

	@Override
	public void setIsOilStored(boolean b) {
		_isOilStored = b;
		markDirty();
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
		} else if(t instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)t)){
			if(Multipart.getTransporter((TileMultipart)t).isConnectedTo(dir.getOpposite())){
				return Multipart.getTransporter((TileMultipart)t);
			}
		}
		return null;
	}
	
	
	private List<IHydraulicMachine> getConnectedBlocks(){
		/*int x = getBlockLocation().getX();
		int y = getBlockLocation().getY();
		int z = getBlockLocation().getZ();*/
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
		super.validate();
	}
	
	@Override
	public void validateI(){
		validate();
	}

	@Override
	public void updateNetworkOnNextTick(float oldPressure) {
		if(getWorld() != null && !getWorld().isRemote){
			shouldUpdateNetwork = true;
			this.pNetwork = null;
			this.oldPressure = oldPressure;
			//markDirty();
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
	public float getPressure(ForgeDirection dir) {
		if(getWorld().isRemote){
			return pressure;
		}
		if(getNetwork(dir) == null){
			Log.error("Machine at " + getBlockLocation().printCoords() + " has no pressure network!");
			return 0;
		}
		return getNetwork(dir).getPressure();
	}

	@Override
	public int getMaxStorage() {
		return HCConfig.INSTANCE.getInt("maxFluidMultiplier") * maxStorage;
	}
	
	public void firstTick(){
		
	}
	
	public int getFluidInNetwork(ForgeDirection from) {
		if(getWorld().isRemote){
			return fluidInNetwork;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	public int getFluidCapacity(ForgeDirection from) {
		if(getWorld().isRemote){
			if(networkCapacity > 0){
				return networkCapacity;
			}else{
				return getMaxStorage();
			}
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}

	public void updateNetwork(float oldPressure) {
		PressureNetwork newNetwork = null;
		PressureNetwork foundNetwork = null;
		PressureNetwork endNetwork = null;
		//This block can merge networks!
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(target.canConnectTo(dir)){
				foundNetwork = PressureNetwork.getNetworkInDir(getWorld(), getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(), dir);
				if(foundNetwork != null){
					if(endNetwork == null){
						endNetwork = foundNetwork;
					}else{
						newNetwork = foundNetwork;
					}
					connectedSides.add(dir);
				}
				
				if(newNetwork != null && endNetwork != null){
					//Hmm.. More networks!? What's this!?
					endNetwork.mergeNetwork(newNetwork);
					newNetwork = null;
				}
			}
		}
			
		if(endNetwork != null){
			pNetwork = endNetwork;
			pNetwork.addMachine(target, oldPressure, ForgeDirection.UP);
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(target, oldPressure, ForgeDirection.UP);
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}

	public PressureNetwork getNetwork(ForgeDirection side) {
		return pNetwork;
	}

	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		pNetwork = toSet;
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			for(ForgeDirection dir: connectedSides){
				if(getNetwork(dir) != null){
					getNetwork(dir).removeMachine(target);
				}
			}
		}
	}

	public void onBlockBreaks() {
		
	}

	@Override
	public void setPressureTier(PressureTier newTier) {
		pressureTier = newTier;		
		markDirty();
	}
	
	@Override
	public PressureTier getPressureTier() {
		return pressureTier;		
	}

	@Override
	public void setMaxStorage(int maxFluid) {
		maxStorage = maxFluid;
		markDirty();
	}

	@Override
	public void readFromNBTI(NBTTagCompound tagCompound) {
		readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBTI(NBTTagCompound tagCompound) {
		writeToNBT(tagCompound);
	}

	@Override
	public void onDataPacketI(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
		onDataPacket(net, packet);
	}

	@Override
	public Packet getDescriptionPacketI() {
		return getDescriptionPacket();
	}

	@Override
	public void updateEntityI() {
		updateEntity();
	}

	@Override
	public void invalidateI() {
		invalidate();
	}
	
	@Override
	public void onChunkUnload(){
		markDirty();
    }

	@Override
	public void addPressureWithRatio(float pressureToAdd, ForgeDirection from) {
		float gen = pressureToAdd * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
		gen = gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
		
		if(Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0){
			//This means the pressure we are generating is too much!
			gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
		}
		
		setPressure(getPressure(from)+gen, from);
	}

	@Override
	public void init(TMultiPart _target) {
		tMp = _target;
		tTarget = null;
		target = (IHydraulicMachine) _target;
		tWorld = _target.world();
		if(target instanceof TileHydraulicPressureVat){
			hasOwnFluidTank = true;
		}
		isMultipart = true;		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
	}

	public boolean getIsRedstonePowered() {
		return isRedstonePowered;
	}

	public void setRedstonePowered(boolean isRedstonePowered) {
		this.isRedstonePowered = isRedstonePowered;
	}
}
