package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPortalBase extends Container {

	protected TilePortalBase base;
	
	public ContainerPortalBase(InventoryPlayer invPlayer, TilePortalBase _base){
		base = _base;
		addSlotToContainer(new Slot(_base, 0, 31, 42));
		
		bindPlayerInventory(invPlayer);
	}
	
	protected void bindPlayerInventory(InventoryPlayer invPlayer){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				addSlotToContainer(new Slot(invPlayer, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
			}
		}
		
		for(int j = 0; j < 9; j++){
			addSlotToContainer(new Slot(invPlayer, j, 8 + (j * 18), 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}
	
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot){
		return null;
	}

}
