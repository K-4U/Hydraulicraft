package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileRFPump extends TileHydraulicBase implements IHydraulicGenerator /* TE , IEnergyHandler*/ {
	private int currentBurnTime;
	private int maxBurnTime;
	private boolean isRunning = false;
// TE 	private EnergyStorage energyStorage;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private int RFUsage = 0;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	private int tier = -1;
	/* TE
	private EnergyStorage getEnergyStorage(){
		if(this.energyStorage == null) 
			this.energyStorage = new EnergyStorage((getTier() + 1) * 400000);
		return this.energyStorage;
	}*/
	
	private Object getEnergyStorage(){
		return null;
	}
	
	public TileRFPump(int _tier){
		super(PressureTier.fromOrdinal(_tier), 2 * (_tier + 1));
		super.validateI(this);
	}
	
	
	@Override
	public void workFunction(ForgeDirection from) {
		if(!getRedstonePowered()){
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
				// TE getEnergyStorage().extractEnergy(RFUsage, false);
				isRunning = true;
			}else{
				/*
				if(getHandler().getRedstonePowered()){
					getEnergyStorage().extractEnergy(RFUsage, false);
				}*/
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
		if(!getRedstonePowered() || getFluidInNetwork(from) == 0){
			RFUsage = 0;
			return 0f;
		}
		/* TE 
		RFUsage = getEnergyStorage().extractEnergy(Constants.RF_USAGE_PER_TICK[getTier()], true);
		
		if(getEnergyStorage().getEnergyStored() > Constants.MIN_REQUIRED_RF){
			float gen = RFUsage * Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			gen = gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
			
			if(Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
			}
			if(Float.compare(gen, getMaxGenerating(from)) > 0){
				gen = getMaxGenerating(from);
			}
			
			RFUsage = (int)(gen * (getFluidInNetwork(from) / getFluidCapacity(from)) / Constants.CONVERSION_RATIO_RF_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
			return gen; 
		}else{
			return 0;
		}*/
		return 0f;
	}


    public int getTier(){
    	if(tier == -1)
    		tier = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	return tier;
    }

	@Override
	public void onBlockBreaks() {
		
	}

	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));

		networkCapacity = tagCompound.getInteger("networkCapacity");
		fluidInNetwork = tagCompound.getInteger("fluidInNetwork");
		RFUsage = tagCompound.getInteger("RFUsage");
		tier = tagCompound.getInteger("tier");
		
		isRunning = tagCompound.getBoolean("isRunning");
		
		if(tier != -1){
			// TE energyStorage = null;
		}
		// TE getEnergyStorage().readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("tier", tier);
		
		if(getNetwork(getFacing()) != null){
			tagCompound.setInteger("networkCapacity", getNetwork(getFacing()).getFluidCapacity());
			tagCompound.setInteger("fluidInNetwork", getNetwork(getFacing()).getFluidInNetwork());
		}
		tagCompound.setInteger("RFUsage", RFUsage);
		
		// TE getEnergyStorage().writeToNBT(tagCompound);
	}

	@Override
	public void onFluidLevelChanged(int old) {	}
/* TE 
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
*/
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(facing);
	}

	public ForgeDirection getFacing() {
		return facing;
	}

	public void setFacing(ForgeDirection rotation) {
		if(!worldObj.isRemote){
			getHandler().updateNetworkOnNextTick(getNetwork(getFacing()).getPressure());
		}
		facing = rotation;
	}
	
	public boolean getIsRunning(){
		return isRunning;
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

	
	public int getRFUsage(){
		return RFUsage;
	}
}
