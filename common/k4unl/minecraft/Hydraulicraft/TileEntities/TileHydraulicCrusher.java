package k4unl.minecraft.Hydraulicraft.TileEntities;

import java.util.Random;

import k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer;
import k4unl.minecraft.Hydraulicraft.items.Items;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicCrusher extends TileConsumer implements ISidedInventory {
	private ItemStack inputInventory;
	private ItemStack crushingItem;
	private ItemStack targetItem;
	private ItemStack outputInventory;
	private final float requiredPressure = 5F;
	private int crushingTicks = 0;
	private int maxCrushingTicks = 0;
	
	public int getCrushingTicks(){
		return crushingTicks;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("crushingItem");
		crushingItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
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
		if(crushingItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			crushingItem.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("crushingItem", inventoryCompound);
		}
		if(targetItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			targetItem.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("targetItem", inventoryCompound);
		}
		
	}
	
	@Override
	public float workFunction(boolean simulate) {
		if(canRun() || isCrushing()){
			if(!simulate){
				doCrush();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 7F + (getPressure() * 0.00005F);
		}else{
			return 0F;
		}
	}
	
	
	private void doCrush(){
		if(isCrushing()){
			crushingTicks = crushingTicks + 1 + (int)((getPressure()/100) * 0.005F);
			Log.info(crushingTicks+ "");
			if(crushingTicks >= maxCrushingTicks){
				//Crushing done!
				if(outputInventory == null){
					outputInventory = targetItem.copy();
				}else{
					outputInventory.stackSize+=targetItem.stackSize;
				}
				
				if( (new Random()).nextFloat() > 0.95){
					outputInventory.stackSize++;
				}
				crushingItem = null;
				targetItem = null;
			}
		}else{
			if(canRun()){
				targetItem = Items.itemChunk.getCrushingRecipe(inputInventory);
				
				crushingItem = inputInventory.copy();
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				crushingTicks = 0;
			}
			maxCrushingTicks = 200;
		}
	}
	
	public ItemStack getCrushingItem(){
		return crushingItem;
	}
	
	public ItemStack getTargetItem(){
		return targetItem;
	}
	
	public boolean isCrushing(){
		return (crushingItem != null && targetItem != null);
	}
	
	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputInventory == null || (getPressure() < requiredPressure)){
			return false;
		}else{
			//Get crushing result:
			ItemStack target = Items.itemChunk.getCrushingRecipe(inputInventory);
			if(target == null) return false;
			if(outputInventory != null){
				if(!outputInventory.isItemEqual(target)) return false;
				int newItemStackSize = outputInventory.stackSize + target.stackSize + 1; //The random chance..
				boolean ret = (newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize());
				return ret;
			}else{
				return true;
			}
		}
	}
	
	private boolean canCrush(ItemStack inv){
		return Config.canBeCrushed(inv);
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
				if(i == 0){
					inputInventory = null;
				}else{
					outputInventory = null;
				}
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
		if(i == 0){
			inputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if(i == 1){
			outputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else{
			//Err...
		}
	}

	@Override
	public String getInvName() {
		// TODO Localization
		return Names.blockHydraulicCrusher.localized;
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
			if(canCrush(itemStack)){
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
		if(i == 0 && canCrush(itemStack)){
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
