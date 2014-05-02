package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
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

public class TileHydraulicMixer extends TileHydraulicBase implements
		ISidedInventory, IFluidHandler, IHydraulicConsumer {

	private ItemStack inputInventory;
	//private ItemStack outputInventory;
	
	private boolean isWorking = false;
	private int maxTicks = 500;
	private int ticksDone = 0;
	private float requiredPressure = 5.0F;
	
	private FluidTank inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
	private FluidTank outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 8);

	public TileHydraulicMixer(){
		super(PressureTier.HIGHPRESSURE, 6);
		super.validateI(this);
	}

	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputInventory == null || (getPressure(ForgeDirection.UNKNOWN) < requiredPressure) || inputTank == null || inputTank.getFluid() == null){
			return false;
		}else{
			if(outputTank.getFluidAmount() + Constants.OIL_FOR_ONE_SEED < outputTank.getCapacity()){
				if(inputInventory.getItem().equals(Items.wheat_seeds)){
					if(inputTank.getFluid().isFluidEqual(new FluidStack(FluidRegistry.WATER.getID(),0)) && inputTank.getFluidAmount() > Constants.WATER_FOR_ONE_SEED){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun() || isWorking){
			if(!simulate){
				doConvert();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 5F + (getPressure(ForgeDirection.UNKNOWN) * 0.00005F);
		}else{
			return 0F;
		}
	}
	
	public void doConvert(){
		if(isWorking){
			ticksDone = ticksDone + 1 + (int)((getPressure(ForgeDirection.UNKNOWN)/100) * 0.00005F);
			//Log.info(ticksDone+ "");
			if(ticksDone >= maxTicks){
				if(outputTank.getFluidAmount() <= 0){
					outputTank.setFluid(new FluidStack(Fluids.fluidOil, Constants.OIL_FOR_ONE_SEED));
				}else{
					outputTank.getFluid().amount+=Constants.OIL_FOR_ONE_SEED;
				}
				isWorking = false;
			}
		}else{
			if(canRun()){
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				
				inputTank.drain(Constants.WATER_FOR_ONE_SEED, true);
				
				ticksDone = 0;
				isWorking = true;
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		switch(i){
		case 0:
			return inputInventory;
		default:
			return null;
			
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack inventory = getStackInSlot(i);
		
		ItemStack ret = null;
		if(inventory.stackSize < j){
			ret = inventory;
			inventory = null;
			
		}else{
			ret = inventory.splitStack(j);
			if(inventory.stackSize <= 0){
				if(i == 0){
					inputInventory = null;
				}
			}
		}
		
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack stack = getStackInSlot(i);
		if(stack != null){
			setInventorySlotContents(i, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		if(i == 0){
			inputInventory = itemStack;
		}else{
			//Err...
			
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this) && 
				player.getDistanceSq(xCoord, yCoord, zCoord) < 64);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		if(i == 0){
			if(itemStack.getItem().equals(Items.wheat_seeds)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {1, 0};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i == 0 && itemStack.getItem().equals(Items.wheat_seeds)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int filled = inputTank.fill(resource, doFill); 
		if(doFill && filled > 10){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		return filled;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack drained = outputTank.drain(maxDrain, doDrain); 
		if(doDrain && drained.amount > 0){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		return drained;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(fluid.equals(FluidRegistry.WATER)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(fluid.equals(Fluids.fluidOil)){
			return true;			
		}else{
			return false;
		}
		
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] tankInfo = {new FluidTankInfo(inputTank), new FluidTankInfo(outputTank)};
		return tankInfo;
	}


	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
//		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inputTank.readFromNBT(tagCompound.getCompoundTag("inputTank"));
		outputTank.readFromNBT(tagCompound.getCompoundTag("outputTank"));
		
		ticksDone = tagCompound.getInteger("ticksDone");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if(inputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inputInventory.writeToNBT(inventoryCompound);
			tagCompound.setTag("inputInventory", inventoryCompound);
		}
		/*if(outputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			outputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("outputInventory", inventoryCompound);
		}*/
		
		NBTTagCompound tankCompound = new NBTTagCompound();
		inputTank.writeToNBT(tankCompound);
		tagCompound.setTag("inputTank", tankCompound);
		
		tankCompound = new NBTTagCompound();
		outputTank.writeToNBT(tankCompound);
		tagCompound.setTag("outputTank", tankCompound);
		
		tagCompound.setInteger("ticksDone", ticksDone);
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
	
	public float getScaledMixTime() {
		if(maxTicks > 0){
			return (float)ticksDone / (float)maxTicks;			
		}else{
			return 0;
		}
		
	}

	@Override
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicMixer.unlocalized);
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBlockBreaks() {
		dropItemStackInWorld(inputInventory);
	}
}
