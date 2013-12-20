package k4unl.minecraft.Hydraulicraft.TileEntities;

import k4unl.minecraft.Hydraulicraft.baseClasses.entities.TileGenerator;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicPump extends TileGenerator implements IInventory {
	private ItemStack inventory;
	private int currentBurnTime;
	private int maxBurnTime;
	
	
	public TileHydraulicPump(){
	}
	
	@Override
	public void updateEntity(){
		//This function gets called every tick.
		//It should check how much coal is left
		//How long that stuff burns
		//And how long it has left to burn.
		boolean needsUpdate = false;
		boolean isBurning = (currentBurnTime > 0);
		if(isBurning){
			currentBurnTime --;
			needsUpdate = true;
		}
		if(!worldObj.isRemote){
			if(currentBurnTime == 0 && TileEntityFurnace.isItemFuel(inventory) && getPressure() <= getMaxPressure()){
				//Put new item in
				currentBurnTime = maxBurnTime = TileEntityFurnace.getItemBurnTime(inventory);
				if(inventory != null){
					inventory.stackSize--;
					if(inventory.stackSize <= 0){
						inventory = null;
					}
					this.onInventoryChanged();
					needsUpdate = true;
				}
			}
		}
		
		if(isBurning){			
			generate();
		}
		if(needsUpdate){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	private void generate(){
		//Set own pressure
		if((getPressure() + getGenerating()) < getMaxPressure()){
			setPressure(getPressure() + getGenerating());
		
			Functions.checkSidesSetPressure(worldObj, xCoord, yCoord, zCoord, getPressure());
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inventory");
		inventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		currentBurnTime = tagCompound.getInteger("currentBurnTime");
		maxBurnTime = tagCompound.getInteger("maxBurnTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		if(inventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("inventory", inventoryCompound);
		}
		tagCompound.setInteger("currentBurnTime",currentBurnTime);
		tagCompound.setInteger("maxBurnTime",maxBurnTime);
	}
	
	@Override
	public void workFunction() {
		
	}

	@Override
	public int getMaxGenerating() {
		if(!isOilStored()){
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
		if(maxBurnTime > 0){
			return ((float)currentBurnTime / (float)maxBurnTime);
		}else{
			return 0;
		}
	}
	
	public boolean getIsBurning() {
		return (currentBurnTime > 0);
	}

	@Override
	public float getGenerating() {
		float multiplier = 0;
		if(getIsBurning()){
			if(isOilStored()){
				multiplier = ((float)maxBurnTime / (float)Constants.BURNING_TIME_DIVIDER_OIL);
			}else{
				multiplier = ((float)maxBurnTime / (float)Constants.BURNING_TIME_DIVIDER_WATER);
			}
			int maxFluid = getStorage();
			float perc = (float)getStored() / (float)maxFluid;
			float result =(multiplier * perc);
			if(result > getMaxGenerating())
				result = getMaxGenerating();
			return result;
		}else{
			return 0;
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(i == 0){
			return inventory;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(i < 1){
			ItemStack ret = null;
			if(inventory.stackSize < j){
				ret = inventory;
				inventory = null;
				
			}else{
				ret = inventory.splitStack(j);
				if(inventory.stackSize == 0){
					inventory = null;
				}
			}
			
			return ret;
			
		}else{
			return null;
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
		if(i < 1){
			inventory = itemStack;
		}
	}

	@Override
	public String getInvName() {
		// TODO Localization
		return Names.blockHydraulicPump[getTier()].localized;
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
		if(i < 1){
			if(TileEntityFurnace.isItemFuel(itemStack)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public int getStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * (2 * (getTier() + 1));
	}

}
