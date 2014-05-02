package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class TileKineticPump extends TileHydraulicBase implements IHydraulicGenerator, IPowerReceptor {
	private boolean isRunning = false;
	private PowerHandler powerHandler;
	private int MJPower;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private int tier = -1;
	private float MJUsage = 0;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	public TileKineticPump(int _tier){
		super(PressureTier.fromOrdinal(_tier), 2 * (_tier + 1));
		super.validateI(this);
		tier = _tier;
		
	}
	
	private PowerHandler getPowerHandler(){
		if(powerHandler == null){
			powerHandler = new PowerHandler(this, Type.MACHINE);
			powerHandler.configure(Constants.MJ_USAGE_PER_TICK[getTier()]*2, Constants.MJ_USAGE_PER_TICK[getTier()] * 3, Constants.ACTIVATION_MJ, (getTier()+1) * 1000);
		}
		return powerHandler;
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
		needsUpdate = true;
		if(Float.compare(getGenerating(ForgeDirection.UP), 0.0F) > 0){
			setPressure(getPressure(ForgeDirection.UNKNOWN) + getGenerating(ForgeDirection.UP), getFacing());
			getPowerHandler().useEnergy(0, MJUsage, true);
			//MJPower -= Constants.MJ_USAGE_PER_TICK[getTier()];
			//getEnergyStorage().extractEnergy(getMaxGenerating(), false);
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
			MJUsage = 0; 
			return 0f;
		}

		MJUsage = (float) getPowerHandler().useEnergy(0, Constants.MJ_USAGE_PER_TICK[getTier()], false);
		//Log.info("PHL: " + getPowerHandler().getEnergyStored() + " EE: " + extractedEnergy);
		
		if(getPowerHandler().getEnergyStored() > Constants.MJ_USAGE_PER_TICK[getTier()] * 2){
			float gen = MJUsage * Constants.CONVERSION_RATIO_MJ_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			gen = gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
			//gen = gen * (gen / getMaxGenerating());
			if(gen > getMaxGenerating(ForgeDirection.UP)){
				gen = getMaxGenerating(ForgeDirection.UP);
			}
			
			if(Float.compare(gen + getPressure(ForgeDirection.UNKNOWN), getMaxPressure(getHandler().isOilStored(), null)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), null) - getPressure(ForgeDirection.UNKNOWN);
			}
			MJUsage = gen * (getFluidInNetwork(from) / getFluidCapacity(from)) / Constants.CONVERSION_RATIO_MJ_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			
			return gen; 
		}else{
			return 0F;
		}
	}


    public int getTier(){
    	if(tier == -1){
    		tier = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    	}
        return tier;
    }
	

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		MJUsage = tagCompound.getFloat("MJUsage");
		tier = tagCompound.getInteger("tier");
		isRunning = tagCompound.getBoolean("isRunning");
		getPowerHandler().readFromNBT(tagCompound, "powerHandler");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setBoolean("isRunning", isRunning);
		tagCompound.setInteger("tier", tier);
		tagCompound.setFloat("MJUsage", MJUsage);
		
		getPowerHandler().writeToNBT(tagCompound, "powerHandler");
		//tagCompound.setInteger("MJPower", MJPower);
	}

	@Override
	public void onFluidLevelChanged(int old) {	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return side.equals(getFacing());
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
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		if(side.equals(facing.getOpposite())){
			return getPowerHandler().getPowerReceiver();
		}else{
			return null;
		}
	}

	@Override
	public void doWork(PowerHandler workProvider) { }

	@Override
	public World getWorld() {
		return worldObj;
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
	
	public float getMJUsage() {
		return MJUsage;
	}
}
