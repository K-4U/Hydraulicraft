package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileElectricPump extends TileEntity implements IHydraulicGenerator, IEnergySink {
	private boolean isRunning = false;
	private IBaseClass baseHandler;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private boolean isFirst = true;
	private int ic2EnergyStored;
	private float renderingPercentage = 0.0F;
	private float renderingDir = 0.05F;
	private PressureNetwork pNetwork;
	
	private int EUUsage = 0;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	private int tier = -1;

	
	public TileElectricPump(){
		
	}
	
	public float getRenderingPercentage(){
		return renderingPercentage;
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
			EUUsage = 0;
			getHandler().updateBlock();
			return;
		}
		//This function gets called every tick.
		boolean needsUpdate = false;
		needsUpdate = true;
		if(Float.compare(getGenerating(ForgeDirection.UP), 0.0F) > 0){
			renderingPercentage+= renderingDir;
			if(Float.compare(renderingPercentage, 1.0F) >= 0 && renderingDir > 0){
				renderingDir = -renderingDir;
			}else if(Float.compare(renderingPercentage, 0.0F) <= 0 && renderingDir < 0){
				renderingDir = -renderingDir;
			}
			
			setPressure(getPressure(from) + getGenerating(from), getFacing().getOpposite());
			ic2EnergyStored -= getEUUsage();
			isRunning = true;
		}else{
			isRunning = false;
		}
		
		if(needsUpdate){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public int getMaxGenerating(ForgeDirection from) {
		if(!getHandler().isOilStored()){
			return (int)(Constants.EU_USAGE_PER_TICK[getTier()] * Constants.CONVERSION_RATIO_EU_HYDRAULIC * Constants.WATER_CONVERSION_RATIO);		
		}else{
			return (int)(Constants.EU_USAGE_PER_TICK[getTier()] * Constants.CONVERSION_RATIO_EU_HYDRAULIC);
		}
	}

	@Override
	public float getGenerating(ForgeDirection from) {
		if(!getHandler().getRedstonePowered() || getFluidInNetwork(from) == 0 ){
			EUUsage = 0;
			return 0f;
		}
		
		
		
		if(ic2EnergyStored > Constants.MIN_REQUIRED_EU){
			EUUsage = Constants.EU_USAGE_PER_TICK[getTier()] % ic2EnergyStored;
			float gen = getEUUsage() * Constants.CONVERSION_RATIO_EU_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			gen = gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
			
			if(gen > getMaxGenerating(ForgeDirection.UP)){
				gen = getMaxGenerating(ForgeDirection.UP);
			}
			if(Float.compare(gen + getPressure(getFacing().getOpposite()), getMaxPressure(getHandler().isOilStored(), null)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), null) - getPressure(getFacing().getOpposite());
			}
			
			EUUsage = (int)(gen * (getFluidInNetwork(from) / getFluidCapacity(from)) / Constants.CONVERSION_RATIO_EU_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
			return gen;
		}else{
			EUUsage = 0;
		}
		
		return 0F;
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

		isRunning = tagCompound.getBoolean("isRunning");
		ic2EnergyStored = tagCompound.getInteger("ic2EnergyStored");
		
		renderingPercentage = tagCompound.getFloat("renderingPercentage");
		
		networkCapacity = tagCompound.getInteger("networkCapacity");
		fluidInNetwork = tagCompound.getInteger("fluidInNetwork");
		EUUsage = tagCompound.getInteger("EUUsage");
		tier = tagCompound.getInteger("tier");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("ic2EnergyStored", ic2EnergyStored);
		tagCompound.setFloat("renderingPercentage", renderingPercentage);
		
		tagCompound.setInteger("tier", tier);
		
		tagCompound.setInteger("networkCapacity", getNetwork(getFacing()).getFluidCapacity());
		tagCompound.setInteger("fluidInNetwork", getNetwork(getFacing()).getFluidInNetwork());
		tagCompound.setInteger("EUUsage", EUUsage);
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

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing);
	}

	public ForgeDirection getFacing() {
		return facing;
	}

	public void setFacing(ForgeDirection rotation) {
		getHandler().updateNetworkOnNextTick(getPressure(getFacing()));
		facing = rotation;
	}
	
	public boolean getIsRunning(){
		return isRunning;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
		if(direction.equals(facing.getOpposite())){
			return true;
		}
		return false;
	}

	@Override
	public double demandedEnergyUnits() {
		if(ic2EnergyStored < Constants.INTERNAL_EU_STORAGE[getTier()]){
			return Double.MAX_VALUE; 
		}else{
			return 0;
		}
	}

	@Override
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
		if(amount > getMaxSafeInput()){
			if(!worldObj.isRemote) {
				//And, better check if the block actually still exists..
				if(!this.isInvalid()){
	                worldObj.createExplosion(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 0.5F, true);
	                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
				}
            }
            return 0;
		}else{
			//Work
			//Best might be to store it in an internal buffer
			//Then use that buffer to get work done..
			if(ic2EnergyStored < getMaxEUStorage()){
				ic2EnergyStored += amount;
				
				amount = Math.max((ic2EnergyStored - getMaxEUStorage()),0);
				if(ic2EnergyStored > getMaxEUStorage()){
					amount = ic2EnergyStored - getMaxEUStorage();
					ic2EnergyStored = getMaxEUStorage();
				}
				getHandler().updateBlock();
			}
		}
		return amount;
	}

	public int getMaxEUStorage(){
		return Constants.INTERNAL_EU_STORAGE[getTier()];
	}
	
	@Override
	public int getMaxSafeInput() {
		//TODO Add upgrades
		return Constants.MAX_EU[getTier()];
	}

    @Override
    public void invalidate(){
        if(worldObj != null && !worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }
        super.invalidate();
		if(!worldObj.isRemote){
			if(pNetwork != null){
				pNetwork.removeMachine(this);
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

	public int getEUStored() {
		return ic2EnergyStored;
	}


	@Override
	public PressureNetwork getNetwork(ForgeDirection side) {
		if(pNetwork == null && worldObj != null){
			updateNetwork(0);
		}
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
			Log.error("Electric pump at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
			return 0;
		}
		return getNetwork(from).getPressure();
		
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		if(getNetwork(side) != null){
			getNetwork(side).setPressure(newPressure);
		}else{
			Log.error("TileEntity TileElectricPump at " + xCoord + "," + yCoord + "," + zCoord + " has no network!");
		}
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
	
	public int getEUUsage(){
		if(EUUsage > Constants.MAX_EU[getTier()]){
			//EUUsage = Constants.MAX_EU[getTier()];
		}
		return EUUsage;
	}
	
	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(worldObj.isRemote){
			return fluidInNetwork;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(worldObj.isRemote){
			return networkCapacity;
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}
}
