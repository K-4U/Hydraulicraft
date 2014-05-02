package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class TileHydraulicEngine extends TileHydraulicBase implements IHydraulicConsumer, IPowerEmitter, IPowerReceptor {
	private final PowerHandler powerHandler;
	private ForgeDirection facing = ForgeDirection.UP;
	public float energy;
	private boolean isRunning = true;
	private float percentageRun = 0.0F;
	private float direction = 0.005F;
	private float pressureRequired;
	private float energyToAdd = 0F;

	
	public TileHydraulicEngine(){
		super(PressureTier.HIGHPRESSURE, 20);
		super.validateI(this);
		powerHandler = new PowerHandler(this, Type.ENGINE);
		powerHandler.configure(Constants.MJ_USAGE_PER_TICK[2]*2, Constants.MJ_USAGE_PER_TICK[2] * 3, Constants.ACTIVATION_MJ, 3000);
	}
	
	public ForgeDirection getFacing(){
		return facing;		
	}
	
	public void setFacing(ForgeDirection newDir){
		getHandler().updateNetworkOnNextTick(getPressure(getFacing()));
		facing = newDir;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		powerHandler.readFromNBT(tagCompound);
		isRunning = tagCompound.getBoolean("isRunning");
		pressureRequired = tagCompound.getFloat("pressureRequired");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		energyToAdd = tagCompound.getFloat("energyToAdd");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		powerHandler.writeToNBT(tagCompound);
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setFloat("pressureRequired", pressureRequired);
		tagCompound.setFloat("energyToAdd", energyToAdd);
	}

	public float getPercentageOfRender(){
		if(isRunning){
			percentageRun += direction;
		}else if(percentageRun > 0 && Float.compare(direction,0.0F) > 0){
			percentageRun -= direction;
		}else if(percentageRun > 0){
			percentageRun += direction;
		}
		if(Float.compare(percentageRun, 0.0F) <= 0 && Float.compare(direction, 0.0F) < 0){
			direction = 0.005F;
		}else if(Float.compare(percentageRun, 1.0F) >= 0 && Float.compare(direction, 0.0F) > 0){
			direction = -0.005F;
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
	public boolean canEmitPowerFrom(ForgeDirection side) {
		return side.equals(facing);
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		return createPower(simulate);
	}
	
	public float createPower(boolean simulate){
		boolean rp = getHandler().getRedstonePowered();
		int pressureReq = Float.compare(getPressure(getFacing().getOpposite()), Constants.MIN_REQUIRED_PRESSURE_ENGINE);
		float energyStored = (float) getPowerReceiver(getFacing()).getEnergyStored();
		float energyMax = (float) getPowerReceiver(getFacing()).getMaxEnergyStored();
		int MJReq = Float.compare(energyStored, energyMax);
		
		if(!rp || pressureReq < 0 || MJReq >= 0){
			isRunning = false;
			pressureRequired = 0F;
			getHandler().updateBlock();
			return 0F;
		}
		
		energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), getFacing())) * Constants.CONVERSION_RATIO_HYDRAULIC_MJ) * Constants.MAX_TRANSFER_MJ;
		if(!simulate){
			energyToAdd = (float) powerHandler.addEnergy(energyToAdd);
		}
		
        int efficiency = 20;
        float pressureUsage = energyToAdd * (1.0F - (efficiency / 100F)); 
        if(pressureUsage > 0.0F){
        	isRunning = true;
        }else{
        	isRunning = false;
        }
        pressureRequired = pressureUsage;
        return pressureUsage;
    }
	
	public float getEnergyToAdd(){
		return energyToAdd;
	}

	public void checkRedstonePower() {
		getHandler().checkRedstonePower();
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		if(side.equals(facing)){
			return powerHandler.getPowerReceiver();
		}else{
			return null;
		}
	}
	
    private void sendPower() {
    	ForgeDirection o = facing;
    	TileEntity tile = worldObj.getTileEntity(xCoord + o.offsetX, yCoord + o.offsetY, zCoord + o.offsetZ);
        if(isPoweredTile(tile, o)) {
            PowerReceiver receptor = ((IPowerReceptor)tile).getPowerReceiver(o.getOpposite());

            float extracted = getPowerToExtract();
            if(extracted > 0) {
                float needed = (float) receptor.receiveEnergy(PowerHandler.Type.ENGINE, extracted, o.getOpposite());
                extractEnergy(receptor.getMinEnergyReceived(), needed, true);
            }
        }
}

	public float getPowerToExtract() {
		ForgeDirection o = facing;
        TileEntity tile = worldObj.getTileEntity(xCoord + o.offsetX, yCoord + o.offsetY, zCoord + o.offsetZ);
        if(tile instanceof IPowerReceptor){
        	PowerReceiver receptor = ((IPowerReceptor)tile).getPowerReceiver(o.getOpposite());
        	return extractEnergy(receptor.getMinEnergyReceived(), receptor.getMaxEnergyReceived(), false);
        }else{
        	return 0;
        }
	}
	
	public float extractEnergy(double d, double e, boolean doExtract){
        return (float) powerHandler.useEnergy(d, e, doExtract);
    }
	
    private float maxEnergyExtracted(){
        return 10;
    }
	
	@Override
	public void doWork(PowerHandler workProvider) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public World getWorld() {
		return worldObj;
	}


	public static boolean isPoweredTile(TileEntity tile, ForgeDirection side) {
        if (tile instanceof IPowerReceptor)
                return ((IPowerReceptor) tile).getPowerReceiver(side.getOpposite()) != null;

        return false;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		sendPower();
	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing.getOpposite());
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
	
	public float getPressureRequired() {
		return pressureRequired;
	}
}
