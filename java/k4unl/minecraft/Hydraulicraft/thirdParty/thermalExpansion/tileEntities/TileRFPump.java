package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileRFPump extends TileEntity implements IHydraulicGenerator /* TE , IEnergyHandler*/ {
	private int currentBurnTime;
	private int maxBurnTime;
	private boolean isRunning = false;
	private IBaseClass baseHandler;
// TE 	private EnergyStorage energyStorage;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private int RFUsage = 0;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	private int tier = -1;
	
	private PressureNetwork pNetwork;
	/* TE
	private EnergyStorage getEnergyStorage(){
		if(this.energyStorage == null) 
			this.energyStorage = new EnergyStorage((getTier() + 1) * 400000);
		return this.energyStorage;
	}*/
	
	private Object getEnergyStorage(){
		return null;
	}
	
	public TileRFPump(){
	}
	
	@Override
	public void updateEntity(){
		getHandler().updateEntity();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		getHandler().readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		getHandler().writeToNBT(tagCompound);
	}
	
	@Override
	public void workFunction(ForgeDirection from) {
		if(!getHandler().getRedstonePowered()){
			isRunning = false;
			getHandler().updateBlock();
			return;
		}
		//This function gets called every tick.
		boolean needsUpdate = false;
		if(!worldObj.isRemote){
			needsUpdate = true;
			if(Float.compare(getGenerating(ForgeDirection.UP), 0.0F) > 0){
				setPressure(getPressure(getFacing()) + getGenerating(ForgeDirection.UP), getFacing());
				// TE getEnergyStorage().extractEnergy(RFUsage, false);
				isRunning = true;
			}else{
				/*
				if(getHandler().getRedstonePowered()){
					getEnergyStorage().extractEnergy(RFUsage, false);
				}*/
				isRunning = false;
			}
		}
		
		if(needsUpdate){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public int getMaxGenerating(ForgeDirection from) {
		if(!getHandler().isOilStored()){
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_GEN_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_GEN_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_GEN_WATER_TIER_3;
			}			
		}else{
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_GEN_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_GEN_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_GEN_OIL_TIER_3;
			}
		}
		return 0;
	}

	@Override
	public float getGenerating(ForgeDirection from) {
		if(!getHandler().getRedstonePowered() || getFluidInNetwork(from) == 0){
			RFUsage = 0;
			return 0f;
		}
		/* TE 
		RFUsage = getEnergyStorage().extractEnergy(Constants.RF_USAGE_PER_TICK[getTier()], true);
		
		if(getEnergyStorage().getEnergyStored() > Constants.MIN_REQUIRED_RF){
			float gen = RFUsage * Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			gen = gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
			
			if(Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
			}
			if(Float.compare(gen, getMaxGenerating(from)) > 0){
				gen = getMaxGenerating(from);
			}
			
			RFUsage = (int)(gen * (getFluidInNetwork(from) / getFluidCapacity(from)) / Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
			return gen; 
		}else{
			return 0;
		}*/
		return 0f;
	}


    public int getTier(){
    	if(tier == -1)
    		tier = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	return tier;
    }
	

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * (2 * (getTier() + 1));
	}

	@Override
	public void onBlockBreaks() {
		
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_OIL_TIER_3;
			}			
		}else{
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_WATER_TIER_3;
			}	
		}
		return 0;
	}

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));

		networkCapacity = tagCompound.getInteger("networkCapacity");
		fluidInNetwork = tagCompound.getInteger("fluidInNetwork");
		RFUsage = tagCompound.getInteger("RFUsage");
		tier = tagCompound.getInteger("tier");
		
		isRunning = tagCompound.getBoolean("isRunning");
		
		if(tier != -1){
			// TE energyStorage = null;
		}
		// TE getEnergyStorage().readFromNBT(tagCompound);
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("tier", tier);
		
		if(getNetwork(getFacing()) != null){
			tagCompound.setInteger("networkCapacity", getNetwork(getFacing()).getFluidCapacity());
			tagCompound.setInteger("fluidInNetwork", getNetwork(getFacing()).getFluidInNetwork());
		}
		tagCompound.setInteger("RFUsage", RFUsage);
		
		// TE getEnergyStorage().writeToNBT(tagCompound);
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
	public void validate(){
		super.validate();
		getHandler().validate();
	}

	@Override
	public void onPressureChanged(float old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
		
	}
/* TE 
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		if(from.equals(facing.getOpposite())){
			return getEnergyStorage().receiveEnergy(maxReceive, simulate);
		}else{
			return 0;
		}
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		return from.equals(facing.getOpposite());
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return getEnergyStorage().getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return getEnergyStorage().getMaxEnergyStored();
	}
*/
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing);
	}

	public ForgeDirection getFacing() {
		return facing;
	}

	public void setFacing(ForgeDirection rotation) {
		if(!worldObj.isRemote){
			getHandler().updateNetworkOnNextTick(getNetwork(getFacing()).getPressure());
		}
		facing = rotation;
	}
	
	public boolean getIsRunning(){
		return isRunning;
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

	}
	
	
	@Override
	public float getPressure(ForgeDirection from) {
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("RF Pump at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
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
		return dir.equals(getFacing());
	}
	
	@Override
	public void updateNetwork(float oldPressure) {
		PressureNetwork endNetwork = null;

		endNetwork = PressureNetwork.getNetworkInDir(worldObj, xCoord, yCoord, zCoord, getFacing());
			
		if(endNetwork != null){
			pNetwork = endNetwork;
			pNetwork.addMachine(this, oldPressure, getFacing());
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure, getFacing());
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}

	
	public int getRFUsage(){
		return RFUsage;
	}
	
	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(worldObj.isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return fluidInNetwork;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(worldObj.isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return networkCapacity;
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(!worldObj.isRemote){
			if(getNetwork(getFacing()) != null){
				getNetwork(getFacing()).removeMachine(this);
			}
		}
	}
}
