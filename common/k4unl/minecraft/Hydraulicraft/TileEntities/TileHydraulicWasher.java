package k4unl.minecraft.Hydraulicraft.TileEntities;

import k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer;
import k4unl.minecraft.Hydraulicraft.items.Items;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileHydraulicWasher extends TileConsumer implements
		ISidedInventory, IFluidHandler {
	private ItemStack inputInventory;
	private ItemStack washingItem;
	private ItemStack targetItem;
	private ItemStack outputInventory;
	private int washingTicks = 0;
	private int maxWashingTicks = 0;
	private float requiredPressure = 5F;
	
	private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
	
	
	public int getWashingTicks(){
		return washingTicks;
	}
	
	public TileHydraulicWasher(){
		
	}

	
	public boolean isWashing() {
		return (washingItem != null && targetItem != null);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		tank.readFromNBT(tagCompound.getCompoundTag("tank"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		if(inputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("inputInventory", inventoryCompound);
		}
		if(outputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			outputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("outputInventory", inventoryCompound);
		}
		
		NBTTagCompound tankCompound = new NBTTagCompound();
		tank.writeToNBT(tankCompound);
		tagCompound.setCompoundTag("tank", tankCompound);
	}
	
	
	@Override
	public float workFunction(boolean simulate) {
		if(canRun() || isWashing()){
			if(!simulate){
				doWash();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 5F + ((getPressure() / 100) * 0.005F);
		}else{
			return 0F;
		}
	}
	
	
	private void doWash(){
		if(isWashing()){
			washingTicks = washingTicks + 1 + (int)((getPressure()/100) * 0.005F);
			if(washingTicks >= maxWashingTicks){
				//washing done!
				if(outputInventory == null){
					outputInventory = targetItem.copy(); 
				}else{
					outputInventory.stackSize++;
				}
				tank.drain(Constants.MIN_REQUIRED_WATER_FOR_WASHER, true);
				
				washingItem = null;
				targetItem = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}else{
			if(canRun()){
				targetItem = Items.itemDust.getWashingRecipe(inputInventory); 
				washingItem = inputInventory.copy();
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				washingTicks = 0;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			//Start washing
			maxWashingTicks = 200;
		}
	}
	
	public ItemStack getWashingItem(){
		return washingItem;
	}
	
	public ItemStack getTargetItem(){
		return targetItem;
	}
	
	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputInventory == null || (getPressure() < requiredPressure) || tank.getFluidAmount() < Constants.MIN_REQUIRED_WATER_FOR_WASHER){
			return false;
		}else{
			//Get smelting result:
			//ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inputInventory);
			ItemStack target = Items.itemDust.getWashingRecipe(inputInventory);
			if(target == null) return false;
			if(outputInventory != null){
				if(!outputInventory.isItemEqual(target)) return false;
				int newItemStackSize = outputInventory.stackSize + inputInventory.stackSize;
				
				return (newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize());
			}else{
				return true;
			}
		}
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		switch(i){
		case 0:
			return inputInventory;
		case 1:
			return outputInventory;
		default:
			return null;
			
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(i == 0){
			ItemStack ret = null;
			if(inputInventory.stackSize < j){
				ret = inputInventory;
				inputInventory = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}else{
				ret = inputInventory.splitStack(j);
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}
		}else{
			ItemStack ret = null;
			if(outputInventory.stackSize < j){
				ret = outputInventory;
				outputInventory = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}else{
				ret = outputInventory.splitStack(j);
				if(outputInventory.stackSize <= 0){
					outputInventory = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}
		}
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
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if(i == 1){
			outputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public String getInvName() {
		// TODO Localization
		return Names.blockHydraulicWasher.localized;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Localization
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ((worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this) && 
				player.getDistanceSq(xCoord, yCoord, zCoord) < 64);
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		if(Config.canBeWashed(itemStack) && i == 0){
			return true;
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
		if(i == 0 && Config.canBeWashed(itemStack)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(i == 1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int getStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 10;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int filled = tank.fill(resource, doFill); 
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
		FluidStack drained = tank.drain(maxDrain, doDrain); 
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
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
		return tankInfo;
		
	}

	@Override
	public int getMaxBar() {
		// TODO Auto-generated method stub
		return 0;
	}




}
