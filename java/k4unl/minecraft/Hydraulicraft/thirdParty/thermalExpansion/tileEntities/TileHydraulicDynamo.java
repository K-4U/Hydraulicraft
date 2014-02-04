package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
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

public class TileHydraulicDynamo extends TileEntity implements IHydraulicConsumer, IEnergyHandler {
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
		if(!simulate){
			
		}
		return createPower(simulate);
	}
	
	private float createPower(boolean simulate){
		if(getHandler().getPressure() < Constants.MIN_REQUIRED_PRESSURE_DYNAMO){
			isRunning = false;
			getHandler().updateBlock();
			return 0F;
		}
		
		float energyToAdd = ((getHandler().getPressure() / getMaxPressure(getHandler().isOilStored())) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * (getHandler().getPressure() / 1000);
		energyToAdd = storage.receiveEnergy((int)energyToAdd, simulate);
		
        int efficiency = 40;
        float pressureUsage = energyToAdd * (1.0F - (efficiency / 100F)); 
        if(pressureUsage > 0.0F){
        	isRunning = true;
        }else{
        	isRunning = false;
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
			return storage.receiveEnergy(maxReceive, simulate);			
		}else{
			return 0;
		}
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract,
			boolean simulate) {
		if(from.equals(facing)){
			return storage.extractEnergy(maxExtract, simulate);
		}else{
			return 0;
		}
	}

	@Override
	public boolean canInterface(ForgeDirection from) {
		return (from.equals(facing));
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		if(from.equals(facing)){
			return storage.getEnergyStored();
		}else{
			return 0;
		}
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		//if(from.equals(facing)){
			return storage.getMaxEnergyStored();
		//}else{
			//return 0;
		//}
	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing.getOpposite());
	}
}
