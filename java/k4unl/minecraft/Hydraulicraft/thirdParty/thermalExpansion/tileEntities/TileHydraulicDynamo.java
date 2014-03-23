package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileHydraulicDynamo extends TileEntity implements IHydraulicConsumer, IEnergyHandler, IEnergyInfo {
	private IBaseClass baseHandler;
	private ForgeDirection facing = ForgeDirection.UP;
	
	private boolean isRunning = true;
	private float percentageRun = 0.0F;
	private float direction = 0.005F;
	protected EnergyStorage storage = new EnergyStorage(32000, Constants.MAX_TRANSFER_RF);
	private int energyGen = 0;
	private float pressureRequired = 0.0F;
	
	private PressureNetwork pNetwork;
	
	public TileHydraulicDynamo(){

	}
	
	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
	}

	public int getGenerating(){
		return energyGen;
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
		isRunning = tagCompound.getBoolean("isRunning");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		storage.readFromNBT(tagCompound);
		energyGen = tagCompound.getInteger("energyGen");
		pressureRequired = tagCompound.getFloat("pressureRequired");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setInteger("energyGen", energyGen);
		tagCompound.setFloat("pressureRequired", pressureRequired);
		storage.writeToNBT(tagCompound);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
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
	
	public float getPercentageOfRender(){
		if(isRunning){
			percentageRun += direction;
		}else if(percentageRun > 0 && Float.compare(direction,0.0F) > 0){
			percentageRun += direction;
		}
		if(Float.compare(percentageRun, 1.0F) >= 0 && Float.compare(direction, 0.0F) > 0){
			//direction = -0.005F;
			percentageRun = 0.0F;
		}
		return percentageRun;
	}
	
	public boolean getIsRunning(){
		return isRunning;
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
	}


	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		pressureRequired = createPower(simulate);

		if(simulate == true && storage.getEnergyStored() > 0 && Float.compare(pressureRequired, 0.0F) == 0){
			pressureRequired += 0.1F;
		}
		
		return pressureRequired;
	}
	
	private float createPower(boolean simulate){
		if(getPressure(getFacing().getOpposite()) < Constants.MIN_REQUIRED_PRESSURE_DYNAMO || !getHandler().getRedstonePowered()){
			isRunning = false;
			energyGen = 0;
			getHandler().updateBlock();
			return 0F;
		}
		
		float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * Constants.MAX_TRANSFER_RF;
		//energyToAdd *= Constants.CONVERSION_RATIO_HYDRAULIC_RF;
		energyToAdd = storage.receiveEnergy((int)energyToAdd, simulate);
		
		if(!simulate){
			energyGen = (int) energyToAdd;
		}
		
        int efficiency = 80;
        float pressureUsage = energyToAdd * (1.0F + (efficiency / 100F)); 
        if(pressureUsage > 0.0F){
        	isRunning = true;
        }else{
        	isRunning = false;
        	getHandler().updateBlock();
        }
        return pressureUsage;
    }

	public float getPressureRequired(){
		return pressureRequired;
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
		
		//PUSH pressure
		//This had me busy for two days.
		if(!worldObj.isRemote){
			TileEntity receiver = worldObj.getBlockTileEntity(xCoord + facing.offsetX, yCoord + facing.offsetY, zCoord + facing.offsetZ);
			if(receiver != null && receiver instanceof IEnergyHandler){
				IEnergyHandler recv = (IEnergyHandler) receiver;
				if(recv.canInterface(getFacing().getOpposite())){
					int extracted = storage.extractEnergy(Constants.MAX_TRANSFER_RF, true);
					int energyPushed = recv.receiveEnergy(facing.getOpposite(), extracted, true);
					
					if(energyPushed > 0){
						recv.receiveEnergy(facing.getOpposite(), storage.extractEnergy(energyPushed, false), false);
					}
				}
			}
		}
	}

	@Override
	public void validate() {
		super.validate();
		getHandler().validate();
	}

	
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive,
			boolean simulate) {
		if(from.equals(facing)){
			return this.storage.receiveEnergy(maxReceive, simulate);			
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
		return (from.equals(facing));
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		if(from.equals(facing) || from.equals(ForgeDirection.UNKNOWN)){
			return this.storage.getMaxEnergyStored();
		}else{
			return 0;
		}
	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing.getOpposite());
	}

	@Override
	public int getEnergyPerTick() {
		float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * (getPressure(ForgeDirection.UNKNOWN) / 1000);
		energyToAdd = storage.receiveEnergy((int)energyToAdd, true);
		return (int)energyToAdd;
	}

	@Override
	public int getMaxEnergyPerTick() {
		/*float energyToAdd = getMaxPressure(getHandler().isOilStored(), null) * Constants.CONVERSION_RATIO_HYDRAULIC_RF;
		energyToAdd = storage.receiveEnergy((int)energyToAdd, true);
		return (int)energyToAdd;*/
		return storage.getMaxExtract();
	}

	@Override
	public int getEnergy() {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergy() {
		return this.storage.getMaxEnergyStored();
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
			Log.error("Hydraulic Dynamo at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
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
		return dir.equals(facing.getOpposite());
	}
	
	@Override
	public void updateNetwork(float oldPressure) {
		PressureNetwork endNetwork = null;

		endNetwork = PressureNetwork.getNetworkInDir(worldObj, xCoord, yCoord, zCoord, getFacing().getOpposite());
			
		if(endNetwork != null){
			pNetwork = endNetwork;
			pNetwork.addMachine(this, oldPressure, getFacing().getOpposite());
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure, getFacing().getOpposite());
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
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
	public void invalidate(){
		super.invalidate();
		if(!worldObj.isRemote){
			if(getNetwork(getFacing().getOpposite()) != null){
				getNetwork(getFacing().getOpposite()).removeMachine(this);
			}
		}
	}
}
