package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class TileHydraulicEngine extends TileEntity implements IHydraulicConsumer, IPowerEmitter, IPowerReceptor {
	private IBaseClass baseHandler;
	private final PowerHandler powerHandler;
	private ForgeDirection facing = ForgeDirection.UP;
	public float energy;
	private boolean isRunning = true;
	private float percentageRun = 0.0F;
	private float direction = 0.005F;
	private float pressureRequired;
	private float energyToAdd = 0F;
	private PressureNetwork pNetwork;
	
	public TileHydraulicEngine(){
		powerHandler = new PowerHandler(this, Type.ENGINE);
		powerHandler.configure(Constants.MJ_USAGE_PER_TICK[2]*2, Constants.MJ_USAGE_PER_TICK[2] * 3, Constants.ACTIVATION_MJ, 3000);
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
		getHandler().updateNetworkOnNextTick(getPressure(getFacing()));
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
		powerHandler.readFromNBT(tagCompound);
		isRunning = tagCompound.getBoolean("isRunning");
		pressureRequired = tagCompound.getFloat("pressureRequired");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		energyToAdd = tagCompound.getFloat("energyToAdd");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		powerHandler.writeToNBT(tagCompound);
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setFloat("pressureRequired", pressureRequired);
		tagCompound.setFloat("energyToAdd", energyToAdd);
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
		float energyStored = getPowerReceiver(getFacing()).getEnergyStored();
		float energyMax = getPowerReceiver(getFacing()).getMaxEnergyStored();
		int MJReq = Float.compare(getPowerReceiver(getFacing()).getEnergyStored(), getPowerReceiver(getFacing()).getMaxEnergyStored());
		
		if(!rp || pressureReq < 0 || MJReq >= 0){
			isRunning = false;
			pressureRequired = 0F;
			getHandler().updateBlock();
			return 0F;
		}
		
		energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), getFacing())) * Constants.CONVERSION_RATIO_HYDRAULIC_MJ) * Constants.MAX_TRANSFER_MJ;
		if(!simulate){
			energyToAdd = powerHandler.addEnergy(energyToAdd);
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
    	TileEntity tile = worldObj.getBlockTileEntity(xCoord + o.offsetX, yCoord + o.offsetY, zCoord + o.offsetZ);
        if(isPoweredTile(tile, o)) {
            PowerReceiver receptor = ((IPowerReceptor)tile).getPowerReceiver(o.getOpposite());

            float extracted = getPowerToExtract();
            if(extracted > 0) {
                float needed = receptor.receiveEnergy(PowerHandler.Type.ENGINE, extracted, o.getOpposite());
                extractEnergy(receptor.getMinEnergyReceived(), needed, true);
            }
        }
}

	public float getPowerToExtract() {
		ForgeDirection o = facing;
        TileEntity tile = worldObj.getBlockTileEntity(xCoord + o.offsetX, yCoord + o.offsetY, zCoord + o.offsetZ);
        if(tile instanceof IPowerReceptor){
        	PowerReceiver receptor = ((IPowerReceptor)tile).getPowerReceiver(o.getOpposite());
        	return extractEnergy(receptor.getMinEnergyReceived(), receptor.getMaxEnergyReceived(), false);
        }else{
        	return 0;
        }
	}
	
	public float extractEnergy(float min, float max, boolean doExtract){
        return powerHandler.useEnergy(min, max, doExtract);
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


	public boolean isPoweredTile(TileEntity tile, ForgeDirection side) {
        if (tile instanceof IPowerReceptor)
                return ((IPowerReceptor) tile).getPowerReceiver(side.getOpposite()) != null;

        return false;
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
		sendPower();
	}

	@Override
	public void validate() {
		super.validate();
		getHandler().validate();
	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing.getOpposite());
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
			Log.error("Hydraulic engine at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
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

	public float getPressureRequired() {
		return pressureRequired;
	}

}
