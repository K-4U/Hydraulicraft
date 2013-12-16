package pet.minecraft.Hydraulicraft.TileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import pet.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer;
import pet.minecraft.Hydraulicraft.lib.Log;
import pet.minecraft.Hydraulicraft.lib.config.Config;
import pet.minecraft.Hydraulicraft.lib.config.Constants;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class TileHydraulicFrictionIncinerator extends TileConsumer implements ISidedInventory {

	private ItemStack inputInventory;
	private ItemStack smeltingItem;
	private ItemStack targetItem;
	private ItemStack outputInventory;
	private final float requiredPressure = 5F;
	private int smeltingTicks = 0;
	private int maxSmeltingTicks = 0;
	
	public int getSmeltingTicks(){
		return smeltingTicks;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("smeltingItem");
		smeltingItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("targetItem");
		targetItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
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
		if(smeltingItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			smeltingItem.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("smeltingItem", inventoryCompound);
		}
		if(targetItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			targetItem.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("targetItem", inventoryCompound);
		}
		
	}
	
	@Override
	public float workFunction(boolean simulate) {
		if(canRun() || isSmelting()){
			if(!simulate){
				doSmelt();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 5F + (getPressure() * 0.00005F);
		}else{
			return 0F;
		}
	}
	
	
	private void doSmelt(){
		if(isSmelting()){
			smeltingTicks = smeltingTicks + 1 + (int)((getPressure()/100) * 0.00005F);
			Log.info(smeltingTicks+ "");
			if(smeltingTicks >= maxSmeltingTicks){
				//Smelting done!
				if(outputInventory == null){
					outputInventory = targetItem.copy(); 
				}else{
					outputInventory.stackSize++;
				}
				smeltingItem = null;
				targetItem = null;
			}
		}else{
			if(canRun()){
				targetItem = FurnaceRecipes.smelting().getSmeltingResult(inputInventory);
				smeltingItem = inputInventory.copy();
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				smeltingTicks = 0;
			}
			//Start smelting
			maxSmeltingTicks = 200;
			//Take item out of the input slot
			//And store it in the smeltingSlot
		}
	}
	
	public ItemStack getSmeltingItem(){
		return smeltingItem;
	}
	
	public ItemStack getTargetItem(){
		return targetItem;
	}
	
	public boolean isSmelting(){
		return (smeltingItem != null && targetItem != null);
	}
	
	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputInventory == null || (getPressure() < requiredPressure)){
			return false;
		}else{
			//Get smelting result:
			ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inputInventory);
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
	
	private boolean canSmelt(ItemStack inv){
		//Get smelting result:
		ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inv);
		if(target == null) return false;
		return true;
	}

	@Override
	public int getMaxBar() {
		return Constants.MAX_MBAR_OIL_TIER_3;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
		ItemStack inventory = getStackInSlot(i);
		
		ItemStack ret = null;
		if(inventory.stackSize < j){
			ret = inventory;
			inventory = null;
			
		}else{
			ret = inventory.splitStack(j);
			if(inventory.stackSize <= 0){
				inventory = null;
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
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
		if(i == 0 && canSmelt(itemStack)){
			inputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else{
			//Err...
		}
	}

	@Override
	public String getInvName() {
		// TODO Localization
		return Names.blockHydraulicFrictionIncinerator.localized;
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
		if(i == 0){
			if(canSmelt(itemStack)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public void onInventoryChanged(){
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {1, 0};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i == 0 && canSmelt(itemStack)){
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
		return FluidContainerRegistry.BUCKET_VOLUME * 5;
	}


}
