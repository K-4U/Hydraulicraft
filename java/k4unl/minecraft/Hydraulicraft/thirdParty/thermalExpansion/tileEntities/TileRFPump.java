package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
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

public class TileRFPump extends TileEntity implements IHydraulicGenerator, IEnergyHandler {
	private int currentBurnTime;
	private int maxBurnTime;
	private boolean isRunning = false;
	private IBaseClass baseHandler;
	private EnergyStorage energyStorage;
	private ForgeDirection facing = ForgeDirection.NORTH;
	
	private PressureNetwork pNetwork;
	
	private EnergyStorage getEnergyStorage(){
		if(this.energyStorage == null) 
			this.energyStorage = new EnergyStorage((getTier() + 1) * 400000);
		return this.energyStorage;
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
				getEnergyStorage().extractEnergy(Constants.RF_USAGE_PER_TICK[getTier()], false);
				isRunning = true;
			}else{
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
		if(!getHandler().getRedstonePowered()) return 0f;
		int extractedEnergy = getEnergyStorage().extractEnergy(Constants.RF_USAGE_PER_TICK[getTier()], true);
		
		if(getEnergyStorage().getEnergyStored() > Constants.MIN_REQUIRED_RF){
			float gen = extractedEnergy * Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			gen = gen * (getHandler().getStored() / getMaxStorage());
			
			if(Float.compare(gen + getPressure(getFacing()), getMaxPressure(getHandler().isOilStored(), null)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), null) - getPressure(getFacing());
			}
			if(Float.compare(gen, getMaxGenerating(from)) > 0){
				gen = getMaxGenerating(from);
			}
			
			return gen; 
		}else{
			return 0;
		}
	}


    public int getTier(){
    	if(worldObj != null)
    		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	return 0;
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
		getEnergyStorage().readFromNBT(tagCompound);
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		getEnergyStorage().writeToNBT(tagCompound);
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
	public void onInventoryChanged() {
		
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

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing);
	}

	public ForgeDirection getFacing() {
		return facing;
	}

	public void setFacing(ForgeDirection rotation) {
		getHandler().updateNetworkOnNextTick(getNetwork(getFacing()).getPressure());
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
			pNetwork.addMachine(this, oldPressure);
			Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure);
			Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}
}
