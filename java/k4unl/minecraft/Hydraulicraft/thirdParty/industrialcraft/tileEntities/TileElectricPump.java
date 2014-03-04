package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.tile.IWrenchable;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IBaseGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IPressureNetwork;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileElectricPump extends TileEntity implements IHydraulicGenerator, IEnergySink {
	private boolean isRunning = false;
	private IBaseGenerator baseHandler;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private boolean isFirst = true;
	private int ic2EnergyStored;
	private float renderingPercentage = 0.0F;
	private float renderingDir = 0.05F;
	private IPressureNetwork pNetwork;
	
	public TileElectricPump(){
		
	}
	
	public float getRenderingPercentage(){
		return renderingPercentage;
	}
	
	@Override
	public void updateEntity(){
		getHandler().updateEntity();
		if(isFirst){
			isFirst = false;
			MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		}
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
	public void workFunction() {
		if(!getHandler().getRedstonePowered()){
			isRunning = false;
			getHandler().updateBlock();
			return;
		}
		//This function gets called every tick.
		boolean needsUpdate = false;
		needsUpdate = true;
		if(Float.compare(getGenerating(), 0.0F) > 0){
			renderingPercentage+= renderingDir;
			if(Float.compare(renderingPercentage, 1.0F) >= 0 && renderingDir > 0){
				renderingDir = -renderingDir;
			}else if(Float.compare(renderingPercentage, 0.0F) <= 0 && renderingDir < 0){
				renderingDir = -renderingDir;
			}
			
			setPressure(getPressure(ForgeDirection.UNKNOWN) + getGenerating(), getFacing().getOpposite());
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
	public int getMaxGenerating() {
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
	public float getGenerating() {
		if(!getHandler().getRedstonePowered()) return 0f;
		if(ic2EnergyStored > getEUUsage()){
			float gen = getEUUsage() * Constants.CONVERSION_RATIO_EU_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			
			if(gen > getMaxGenerating()){
				gen = getMaxGenerating();
			}
			if(Float.compare(gen + getPressure(getFacing().getOpposite()), getMaxPressure(getHandler().isOilStored(), null)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), null) - getPressure(getFacing().getOpposite());
			}
			return gen;
		}
		
		return 0F;
	}


    public int getTier(){
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
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
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getGeneratorClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));

		isRunning = tagCompound.getBoolean("isRunning");
		ic2EnergyStored = tagCompound.getInteger("ic2EnergyStored");
		
		renderingPercentage = tagCompound.getFloat("renderingPercentage");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("ic2EnergyStored", ic2EnergyStored);
		tagCompound.setFloat("renderingPercentage", renderingPercentage);
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
		facing = rotation;
		firstTick();
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
                worldObj.createExplosion(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 0.5F, true);
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
            return 0;
		}else{
			//Work
			//Best might be to store it in an internal buffer
			//Then use that buffer to get work done..
			if(ic2EnergyStored < getMaxEUStorage()){
				ic2EnergyStored += amount;
				amount = Math.max((ic2EnergyStored - getMaxEUStorage()),0);
				getHandler().updateBlock();
			}
		}
		return amount;
	}

	public int getMaxEUStorage(){
		return Constants.INTERNAL_EU_STORAGE[getTier()];
	}
	
	public int getEUUsage(){
		//TODO: Add upgrades
		return Constants.EU_USAGE_PER_TICK[getTier()];
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

}
