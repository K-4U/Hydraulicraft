package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IPressureNetwork;
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
	protected EnergyStorage storage = new EnergyStorage(32000);
	
	public TileHydraulicDynamo(){

	}
	
	@Override
	public float getMaxPressure(boolean isOil) {
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
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getConsumerClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		isRunning = tagCompound.getBoolean("isRunning");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		storage.readFromNBT(tagCompound);
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("facing", facing.ordinal());
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
	public float workFunction(boolean simulate) {
		float pressureRequired = createPower(simulate);

		if(simulate == true && storage.getEnergyStored() > 0 && Float.compare(pressureRequired, 0.0F) == 0){
			pressureRequired += 0.1F;
		}
		
		return pressureRequired;
	}
	
	private float createPower(boolean simulate){
		if(getHandler().getPressure() < Constants.MIN_REQUIRED_PRESSURE_DYNAMO || !getHandler().getRedstonePowered()){
			isRunning = false;
			getHandler().updateBlock();
			return 0F;
		}
		
		float energyToAdd = ((getHandler().getPressure() / getMaxPressure(getHandler().isOilStored())) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * Constants.MAX_TRANSFER_RF;
		//energyToAdd *= Constants.CONVERSION_RATIO_HYDRAULIC_RF;
		energyToAdd = storage.receiveEnergy((int)energyToAdd, simulate);
		
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
		TileEntity receiver = worldObj.getBlockTileEntity(xCoord + facing.offsetX, yCoord + facing.offsetY, zCoord + facing.offsetZ);
		if(receiver != null && receiver instanceof IEnergyHandler){
			IEnergyHandler recv = (IEnergyHandler) receiver;
			int energyPushed = recv.receiveEnergy(facing.getOpposite(), storage.extractEnergy(Constants.MAX_TRANSFER_RF, true), true);
			
			if(energyPushed > 0){
				recv.receiveEnergy(facing.getOpposite(), storage.extractEnergy(energyPushed, false), false);
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
		float energyToAdd = ((getHandler().getPressure() / getMaxPressure(getHandler().isOilStored())) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * (getHandler().getPressure() / 1000);
		energyToAdd = storage.receiveEnergy((int)energyToAdd, true);
		return (int)energyToAdd;
	}

	@Override
	public int getMaxEnergyPerTick() {
		float energyToAdd = getMaxPressure(getHandler().isOilStored()) * Constants.CONVERSION_RATIO_HYDRAULIC_RF;
		energyToAdd = storage.receiveEnergy((int)energyToAdd, true);
		return (int)energyToAdd;
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
	public IPressureNetwork getNetwork(ForgeDirection side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetwork(ForgeDirection side, IPressureNetwork toSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void firstTick() {
		// TODO Auto-generated method stub
		
	}

}
