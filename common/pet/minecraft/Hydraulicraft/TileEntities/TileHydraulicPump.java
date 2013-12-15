package pet.minecraft.Hydraulicraft.TileEntities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import pet.minecraft.Hydraulicraft.baseClasses.entities.TileGenerator;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class TileHydraulicPump extends TileGenerator implements IInventory {
	private ItemStack inventory;
	
	public TileHydraulicPump(){
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inventory");
		inventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		if(inventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("inventory", inventoryCompound);
		}
	}
	
	@Override
	public void workFunction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxGenerating() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBar() {
		// TODO Auto-generated method stub
		return 0;
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
		return Names.blockHydraulicPump.localized;
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
		return FluidContainerRegistry.BUCKET_VOLUME * 2;
	}

	
}
