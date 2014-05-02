package k4unl.minecraft.Hydraulicraft.tileEntities.generator;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileHydraulicLavaPump extends TileHydraulicBase implements IHydraulicGenerator, IFluidHandler {
	private FluidTank tank = null;
	private int tier = -1;
	private boolean isRunning = false;
	private int lavaUsage = 0;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	private ForgeDirection facing = ForgeDirection.UNKNOWN;
	
	public TileHydraulicLavaPump(){
		super(PressureTier.HIGHPRESSURE, 1);
		super.init(this);
	}
	
	public TileHydraulicLavaPump(int tier){
		super(PressureTier.fromOrdinal(tier), 2 * (tier + 1));
		super.init(this);
	}
	
	@Override
	public void validate(){
		super.validate();
	}
	
	@Override	
	public void workFunction(ForgeDirection from) {
		if(!from.equals(ForgeDirection.UP)) return;
		
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
				getHandler().setPressure(getPressure(getFacing()) + getGenerating(ForgeDirection.UP), getFacing());
				tank.drain(Constants.MAX_LAVA_USAGE[getTier()], true);
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

	public float getBurningPercentage() {
		return 0;
	}

	public int getLavaUsage(){
		return lavaUsage;
	}
	
	@Override
	public float getGenerating(ForgeDirection from) {
		if(!getRedstonePowered() || getFluidInNetwork(from) == 0 || tank == null || tank.getFluid() == null){
			lavaUsage = 0;
			return 0f;
		}
		
		if(tank.getFluidAmount() > Constants.MAX_LAVA_USAGE[getTier()] * 2){
			
			lavaUsage = tank.drain(Constants.MAX_LAVA_USAGE[getTier()], false).amount;
			
			float gen = lavaUsage * Constants.CONVERSION_RATIO_LAVA_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
			gen = gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
			
			if(Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0){
				//This means the pressure we are generating is too much!
				gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
			}
			if(Float.compare(gen, getMaxGenerating(from)) > 0){
				gen = getMaxGenerating(from);
			}
			
			lavaUsage = (int)(gen * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from)) / Constants.CONVERSION_RATIO_LAVA_HYDRAULIC * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO));
			return gen; 
		}else{
			return 0;
		}
	}
	
	public void setTier(int tier){
		if(tank == null){
			tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier+1)));
		}
		super.setMaxStorage(2 * (tier + 1));
		super.setPressureTier(PressureTier.fromOrdinal(tier));
	}
	
	public int getTier() {
		if(tier == -1 && worldObj != null){
			tier = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier+1)));
			super.setMaxStorage(2 * (tier + 1));
			super.setPressureTier(PressureTier.fromOrdinal(tier));
		}
		
		return tier;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		setTier(tagCompound.getInteger("tier"));
		NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
		if(tankCompound != null){
			tank.readFromNBT(tankCompound);
		}
		
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		
		lavaUsage = tagCompound.getInteger("lavaUsage");
		isRunning = tagCompound.getBoolean("isRunning");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("tier", getTier());
		if(tank != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			tank.writeToNBT(inventoryCompound);
			tagCompound.setTag("tank", inventoryCompound);
		}
		
		tagCompound.setInteger("facing", facing.ordinal());
		
		tagCompound.setInteger("lavaUsage", lavaUsage);
		tagCompound.setBoolean("isRunning", isRunning);
	}
	
	@Override
	public void onFluidLevelChanged(int old) { }

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(worldObj.isRemote) return 0;
		if(resource == null) //What!?
			return 0;
		if(resource.getFluid() == null)
			return 0;
		if(resource.getFluid().getID() != FluidRegistry.LAVA.getID()){
			return 0;
		}
		
		if(tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0){
			if(resource.getFluid().getID() != tank.getFluid().getFluid().getID()){
				return 0;
			}
		}else if(tank == null){
			setTier(getTier());
		}
		
		int filled = tank.fill(resource, doFill);
		return filled;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack drained = tank.drain(maxDrain, doDrain);
		if(tank.getFluidAmount() == 0 && doDrain){
			tank.setFluid(null);
		}
		return drained;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid.getID() == FluidRegistry.LAVA.getID();
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid.getID() == FluidRegistry.LAVA.getID();
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(tank != null){
			FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
			return tankInfo;
		}else{
			return null;
		}
	}
	
	public ForgeDirection getFacing(){
		return facing;		
	}
	
	public void setFacing(ForgeDirection newDir){
		facing = newDir;
		markDirty();
	}
	
}
