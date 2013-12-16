package pet.minecraft.Hydraulicraft.TileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidContainerRegistry;
import pet.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer;
import pet.minecraft.Hydraulicraft.lib.config.Constants;

public class TileHydraulicFrictionIncinerator extends TileConsumer implements IInventory {

	private ItemStack inputItem;
	private ItemStack outputItem;
	private final float requiredPressure = 5F;
	
	
	@Override
	public float workFunction(boolean simulate) {
		
		return 5F;
	}
	
	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputItem == null || (getPressure() < requiredPressure)){
			return false;
		}else{
			//Get smelting result:
			ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inputItem);
			if(target == null) return false;
			if(!outputItem.isItemEqual(target)) return false;
		}
	}

	@Override
	public int getMaxBar() {
		return Constants.MAX_MBAR_OIL_TIER_3;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO Auto-generated method stub
		if(i == 0) {
			return inputItem;
		} else if(i == 1) {
			return outputItem;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 5;
	}


}
