package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IPressureNetwork;
import k4unl.minecraft.Hydraulicraft.api.IPressureNetwork;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
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
	private IPressureNetwork pNetwork;
	
	public TileHydraulicEngine(){
		powerHandler = new PowerHandler(this, Type.ENGINE);
        powerHandler.configure(1.5F, 300, 10, 1000);
        powerHandler.configurePowerPerdition(1, 100);
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
		powerHandler.readFromNBT(tagCompound);
		isRunning = tagCompound.getBoolean("isRunning");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		powerHandler.writeToNBT(tagCompound);
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("facing", facing.ordinal());
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
		if(!simulate){
			sendPower();
		}
		return createPower(simulate);
	}
	
	private float createPower(boolean simulate){
		if(getPressure(getFacing().getOpposite()) < Constants.MIN_REQUIRED_PRESSURE_ENGINE){
			isRunning = false;
			getHandler().updateBlock();
			return 0F;
		}
		
		float energyToAdd = ((getPressure(getFacing().getOpposite()) / getMaxPressure(getHandler().isOilStored(), null)) * Constants.CONVERSION_RATIO_HYDRAULIC_MJ) * (getPressure(getFacing().getOpposite()) / 1000);
		if(!simulate)
			energy += energyToAdd;
		
        int efficiency = 20;
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

	private float getPowerToExtract() {
		ForgeDirection o = facing;
        TileEntity tile = worldObj.getBlockTileEntity(xCoord + o.offsetX, yCoord + o.offsetY, zCoord + o.offsetZ);
        PowerReceiver receptor = ((IPowerReceptor)tile).getPowerReceiver(o.getOpposite());
        return extractEnergy(receptor.getMinEnergyReceived(), receptor.getMaxEnergyReceived(), false);
	}
	
	public float extractEnergy(float min, float max, boolean doExtract){
        if(energy < min) return 0;

        float actualMax;

        if(max > maxEnergyExtracted()) actualMax = maxEnergyExtracted();
        else actualMax = max;

        if(actualMax < min) return 0;

        float extracted;

        if(energy >= actualMax) {
            extracted = actualMax;
            if(doExtract) energy -= actualMax;
        } else {
            extracted = energy;
            if(doExtract) energy = 0;
        }

        return extracted;
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
	public IPressureNetwork getNetwork(ForgeDirection side) {
		return pNetwork;
	}

	@Override
	public void setNetwork(ForgeDirection side, IPressureNetwork toSet) {
		pNetwork = toSet;
	}

	
	
	@Override
	public void firstTick() {
		IPressureNetwork newNetwork = Functions.getNearestNetwork(worldObj, xCoord, yCoord, zCoord);
		if(newNetwork != null){
			pNetwork = newNetwork;
			pNetwork.addMachine(this);
			//Log.info("Found an existing network (" + newNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(0, this);
			//Log.info("Created a new network @ " + xCoord + "," + yCoord + "," + zCoord);
		}
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
		return dir.equals(facing.getOpposite());
	}
	
}
