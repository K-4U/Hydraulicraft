package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileElectricPump extends TileHydraulicBase implements IHydraulicGenerator, IEnergySink {
	private boolean isRunning = false;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private boolean isFirst = true;
	private int ic2EnergyStored;
	private float renderingPercentage = 0.0F;
	private float renderingDir = 0.05F;
	
	private int EUUsage = 0;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	private int tier = -1;

	public TileElectricPump(){
		super(PressureTier.HIGHPRESSURE, 1);
		super.init(this);
	}
	
	public TileElectricPump(int _tier){
		super(PressureTier.fromOrdinal(_tier), 2 * (_tier+1));
		super.init(this);
	}
	
	public float getRenderingPercentage(){
		return renderingPercentage;
	}
	
	@Override
	public void workFunction(ForgeDirection from) {
		if(!getRedstonePowered()){
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
			ic2EnergyStored = ic2EnergyStored - getEUUsage();
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
		if(!getRedstonePowered() || getFluidInNetwork(from) == 0 ){
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
	public void readFromNBT(NBTTagCompound tagCompound) {
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
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("ic2EnergyStored", ic2EnergyStored);
		tagCompound.setFloat("renderingPercentage", renderingPercentage);
		
		tagCompound.setInteger("tier", tier);
		
		tagCompound.setInteger("EUUsage", EUUsage);
	}

	@Override
	public void onFluidLevelChanged(int old) { }

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

	public int getMaxEUStorage(){
		return Constants.INTERNAL_EU_STORAGE[getTier()];
	}

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
	public void firstTick() {
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
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
	public double getDemandedEnergy() {
		if(ic2EnergyStored < Constants.INTERNAL_EU_STORAGE[getTier()]){
			return Double.MAX_VALUE;
		}else{
			return 0;
		}
	}

	@Override
	public int getSinkTier() {
		return 1;
	}

	@Override
	public double injectEnergy(ForgeDirection forgeDirection, double amount, double v2) {
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
}
