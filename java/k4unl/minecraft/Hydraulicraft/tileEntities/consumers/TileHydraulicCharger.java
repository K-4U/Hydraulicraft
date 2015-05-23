package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileHydraulicCharger extends TileHydraulicBase implements IInventory, IHydraulicConsumer {
	private ItemStack chargingItem;
	
	
	public TileHydraulicCharger(PressureTier tier) {
		super(tier, 1);
		super.init(this);
	}


	@Override
	public void onFluidLevelChanged(int old) {}


	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}


	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		return 0;
	}


	@Override
	public boolean canWork(ForgeDirection dir) {
		return true;
	}


	@Override
	public int getSizeInventory() {
		return 1;
	}


	@Override
	public ItemStack getStackInSlot(int var1) {
		if(var1 == 0){
			return chargingItem;
		}
		return null;
	}


	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if(var1 == 0){
			ItemStack tStack = chargingItem.copy();
			chargingItem = null;
			return tStack;
		}
		return null;
	}


	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return chargingItem;
	}


	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		chargingItem = var2;
	}


	@Override
	public String getInventoryName() {
		return null;
	}


	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}


	@Override
	public int getInventoryStackLimit() {
		return 1;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return false;
	}


	@Override
	public void openInventory() { }


	@Override
	public void closeInventory() {	}


	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return var2.getItem() instanceof IPressurizableItem;
	}
}
