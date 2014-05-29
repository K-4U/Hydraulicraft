package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileHydraulicDynamo extends TileHydraulicBase implements IHydraulicConsumer, IEnergyHandler, IEnergyInfo, ICustomNetwork {
	private ForgeDirection facing = ForgeDirection.UP;
	
	private boolean isRunning = true;
	private float percentageRun = 0.0F;
	private float direction = 0.005F;
	protected EnergyStorage storage = new EnergyStorage(32000, Constants.MAX_TRANSFER_RF);
	private int energyGen = 0;
	private float pressureRequired = 0.0F;
	
	public TileHydraulicDynamo(){
		super(PressureTier.HIGHPRESSURE, 20);
		super.init(this);
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
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		isRunning = tagCompound.getBoolean("isRunning");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		//storage.readFromNBT(tagCompound);
		energyGen = tagCompound.getInteger("energyGen");
		pressureRequired = tagCompound.getFloat("pressureRequired");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setInteger("energyGen", energyGen);
		tagCompound.setFloat("pressureRequired", pressureRequired);
		//storage.writeToNBT(tagCompound);
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
	public void onFluidLevelChanged(int old) {	}


	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		pressureRequired = createPower(simulate);

		if(simulate == true && storage.getEnergyStored() > 0 && Float.compare(pressureRequired, 0.0F) == 0){
			pressureRequired += 0.1F;
		}
		
		return pressureRequired;
	}
	
	private float createPower(boolean simulate){
		if(getPressure(getFacing().getOpposite()) < Constants.MIN_REQUIRED_PRESSURE_DYNAMO || !getRedstonePowered()){
			isRunning = false;
			energyGen = 0;
			getHandler().updateBlock();
			return 0F;
		}
		
		float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * Constants.MAX_TRANSFER_RF;
		//energyToAdd *= Constants.CONVERSION_RATIO_HYDRAULIC_RF;
		//energyToAdd = storage.receiveEnergy((int)energyToAdd, simulate);
		
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


	@Override
	public void updateEntity() {
		super.updateEntity();
		
		//PUSH pressure
		//This had me busy for two days.
		
		if(!worldObj.isRemote){
			TileEntity receiver = worldObj.getTileEntity(xCoord + facing.offsetX, yCoord + facing.offsetY, zCoord + facing.offsetZ);
			if(receiver != null && receiver instanceof IEnergyHandler){
				IEnergyHandler recv = (IEnergyHandler) receiver;
				if(recv.canConnectEnergy(getFacing().getOpposite())){
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
	public boolean canConnectEnergy(ForgeDirection from) {
		// TODO Auto-generated method stub
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
	public int getInfoEnergyPerTick() {
		float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_RF) * (getPressure(ForgeDirection.UNKNOWN) / 1000);
		energyToAdd = storage.receiveEnergy((int)energyToAdd, true);
		return (int)energyToAdd;
	}

	@Override
	public int getInfoMaxEnergyPerTick() {
		return storage.getMaxExtract();
	}

	@Override
	public int getInfoEnergy() {
		return this.storage.getEnergyStored();
	}

	@Override
	public int getInfoMaxEnergy() {
		return this.storage.getMaxEnergyStored();
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

}
