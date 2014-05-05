package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileHydraulicWaterPump extends TileHydraulicBase implements IHydraulicConsumer {

	private float requiredPressure = 5.0F;

	public TileHydraulicWaterPump(){
		super(PressureTier.HIGHPRESSURE, 6);
		super.init(this);
	}

	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		//See if it's inside the water.
		if(isOilStored() == true) return false;
		int fluidCapacity = getFluidCapacity(ForgeDirection.UNKNOWN);
		int fluidInNetwork = getFluidInNetwork(ForgeDirection.UNKNOWN);
		if(getFluidCapacity(ForgeDirection.UNKNOWN) <= getFluidInNetwork(ForgeDirection.UNKNOWN)) return false;
		int waterBlocksAround = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			if(getWorldObj().getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ) == Blocks.water){
				waterBlocksAround+=1;
			}
		}
		return getPressure(ForgeDirection.UNKNOWN) > requiredPressure && waterBlocksAround > 2;
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
			return 5F + (getPressure(ForgeDirection.UNKNOWN) * 0.0005F);
		}else{
			return 0F;
		}
	}

	private void doPump() {
		int currentlyStored = getStored();
		currentlyStored += 10;
		if(getPressureTier().equals(PressureTier.MEDIUMPRESSURE)){
			currentlyStored+=50;
		}
		if(getPressureTier().equals(PressureTier.HIGHPRESSURE)){
			currentlyStored+=25;
		}
		setStored(currentlyStored, false, false);
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
}
