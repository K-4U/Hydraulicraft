package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileHydraulicWaterPump extends TileHydraulicBase implements IHydraulicConsumer, IFluidHandler {

	private float requiredPressure = 5.0F;
	private int fluidHandlersNear = 0;
	private FluidTank tank;

	public TileHydraulicWaterPump(){
		super(PressureTier.LOWPRESSURE, 2);
		super.init(this);
		tank = new FluidTank(new FluidStack(FluidRegistry.WATER, 10), 10);
	}

	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		//See if it's inside the water.
		int waterBlocksAround = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(getBlockLocation().getBlock(getWorldObj(), dir) == Blocks.water){
				waterBlocksAround+=1;
			}
		}
		fluidHandlersNear = 0;
		boolean hasFluidHandlerNear = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
			if(shouldIFillThis instanceof IFluidHandler){
				if(((IFluidHandler)shouldIFillThis).canFill(dir.getOpposite(), FluidRegistry.WATER)){
					hasFluidHandlerNear = true;
					fluidHandlersNear++;
				}
			}
		}
		return getPressure(ForgeDirection.UNKNOWN) > requiredPressure && waterBlocksAround >= 2 && hasFluidHandlerNear;
	}
	
	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun()){
			if(!simulate){
				doPump();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 5F + (getPressure(ForgeDirection.UNKNOWN) * 0.00005F);
		}else{
			return 0F;
		}
	}

	private void doPump() {
		int toFill = HCConfig.getInt("waterPumpPerTick");
		
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity shouldIFillThis = getBlockLocation().getTE(getWorldObj(), dir);
			if(shouldIFillThis instanceof IFluidHandler){
				if(((IFluidHandler)shouldIFillThis).canFill(dir.getOpposite(), FluidRegistry.WATER)){
					if(((IFluidHandler)shouldIFillThis).fill(dir.getOpposite(), new FluidStack(FluidRegistry.WATER, toFill), false) > 0){
						((IFluidHandler)shouldIFillThis).fill(dir.getOpposite(), new FluidStack(FluidRegistry.WATER, toFill), true);
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
	}

	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public void onFluidLevelChanged(int old) {}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean canWork(ForgeDirection dir) {
		if(getNetwork(dir) == null){
			return false;
		}
		return dir.equals(ForgeDirection.UP);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return fluid.equals(FluidRegistry.WATER);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
		return tankInfo;
	}
}
