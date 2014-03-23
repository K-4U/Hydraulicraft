package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicGenerator extends TileEntity implements IHydraulicConsumer, IEnergySource {
	private IBaseClass baseHandler;
	private ForgeDirection facing = ForgeDirection.UP;
	private int ic2EnergyStored;
	private int energyToAdd;
	private float pressureRequired;
	
	
	private PressureNetwork pNetwork;
	private List<ForgeDirection> connectedSides;
	
	public TileHydraulicGenerator(){
		connectedSides = new ArrayList<ForgeDirection>();
	}
	
	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
	}

	public ForgeDirection getFacing(){
		return facing;		
	}
	
	public void setFacing(ForgeDirection newDir){
		facing = newDir;
	}
	
	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 20;
	}

	@Override
	public void onBlockBreaks() {
	}

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		ic2EnergyStored = tagCompound.getInteger("ic2EnergyStored");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		energyToAdd = tagCompound.getInteger("energyToAdd");
		pressureRequired = tagCompound.getFloat("pressureRequired");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("ic2EnergyStored", ic2EnergyStored);
		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setInteger("energyToAdd", energyToAdd);
		tagCompound.setFloat("pressureRequired",pressureRequired );
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		getHandler().onDataPacket(net, packet);
		
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
	}

	@Override
	public void onPressureChanged(float old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		return createPower(simulate);
	}
	
	public int getEnergyToAdd(){
		return energyToAdd;
	}
	
	public float createPower(boolean simulate){
		boolean rp = getHandler().getRedstonePowered();
		int pressureReq = Float.compare(getPressure(getFacing().getOpposite()), Constants.MIN_REQUIRED_PRESSURE_ENGINE);
		float energyStored = getEUStored();
		float energyMax = getMaxEUStorage();
		int EUReq = Float.compare(energyStored, energyMax);
		
		if(!rp || pressureReq < 0 || EUReq >= 0){
			energyToAdd = 0;
			pressureRequired = 0F;
			getHandler().updateBlock();
			return 0F;
		}
		
		energyToAdd = (int)(((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), getFacing())) * Constants.CONVERSION_RATIO_HYDRAULIC_EU) * Constants.MAX_TRANSFER_EU);
		if(!simulate){
			energyToAdd = addEnergy(energyToAdd);
		}
		
        int efficiency = 20;
        float pressureUsage = energyToAdd * (1.0F - (efficiency / 100F)); 
        pressureRequired = pressureUsage;
        return pressureUsage;
    }

	public void checkRedstonePower() {
		getHandler().checkRedstonePower();
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		getHandler().readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		getHandler().writeToNBT(tagCompound);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		getHandler().updateEntity();
	}

	@Override
	public void validate() {
		super.validate();
		getHandler().validate();
	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return !side.equals(facing);
	}

	@Override
	public PressureNetwork getNetwork(ForgeDirection side) {
		return pNetwork;
	}

	@Override
	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		pNetwork = toSet;
	}

	
	
	@Override
	public void firstTick() {
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
	}

	@Override
	public float getPressure(ForgeDirection from) {
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("Hydraulic Generator at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
			return 0;
		}
		return getNetwork(from).getPressure();
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		getNetwork(side).setPressure(newPressure);
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}
	
	@Override
	public void updateNetwork(float oldPressure) {
		PressureNetwork newNetwork = null;
		PressureNetwork foundNetwork = null;
		PressureNetwork endNetwork = null;
		//This block can merge networks!
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			foundNetwork = PressureNetwork.getNetworkInDir(worldObj, xCoord, yCoord, zCoord, dir);
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
			
		if(endNetwork != null){
			pNetwork = endNetwork;
			pNetwork.addMachine(this, oldPressure, ForgeDirection.UP);
			Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure, ForgeDirection.UP);
			Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}

	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(worldObj.isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return 0;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(worldObj.isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return 0;
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction) {
		if(!direction.equals(getFacing())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public double getOfferedEnergy() {
		if(worldObj.isRemote){
			//GUI
			if(ic2EnergyStored <= energyToAdd){
				return (double)energyToAdd;
			}else{
				return Math.min(ic2EnergyStored, Constants.MAX_TRANSFER_EU);
			}
		}else{
			return Math.min(ic2EnergyStored, Constants.MAX_TRANSFER_EU);
		}
	}

	@Override
	public void drawEnergy(double amount) {
		ic2EnergyStored -= amount;
		if(ic2EnergyStored < 0){
			ic2EnergyStored = 0;
		}
	}
	
	public int getEUStored() {
		return ic2EnergyStored;
	}
	
	public int getMaxEUStorage(){
		return Constants.INTERNAL_EU_STORAGE[2];
	}
	
	public float getPressureRequired(){
		return pressureRequired; 
	}
	
	@Override
    public void invalidate(){
        if(worldObj != null && !worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
		if(!worldObj.isRemote){
			for(ForgeDirection dir: connectedSides){
				if(getNetwork(dir) != null){
					getNetwork(dir).removeMachine(this);
				}
			}
		}
    }

    @Override
    public void onChunkUnload(){
        if(worldObj != null && !worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.onChunkUnload();
    }
    
	public int addEnergy(int quantity) {
		ic2EnergyStored += quantity;

		if (ic2EnergyStored > getMaxEUStorage()) {
			quantity -= ic2EnergyStored - getMaxEUStorage();
			ic2EnergyStored = getMaxEUStorage();
		} else if (ic2EnergyStored < 0) {
			quantity -= ic2EnergyStored;
			ic2EnergyStored = 0;
		}


		return quantity;
	}
}
