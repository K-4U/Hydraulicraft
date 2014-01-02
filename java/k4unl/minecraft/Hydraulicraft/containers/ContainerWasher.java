package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerWasher extends Container {
	protected TileHydraulicWasher tileWasher;
	
	
	public ContainerWasher(InventoryPlayer invPlayer, TileHydraulicWasher _tileWasher){
		tileWasher = _tileWasher;
		
		addSlotToContainer(new SlotMachineInput(tileWasher, tileWasher, 0, 56, 14));
		addSlotToContainer(new SlotFurnace(invPlayer.player, tileWasher, 1, 106, 54));
		
		addSlotToContainer(new SlotMachineInput(tileWasher, tileWasher, 2, 31, 14));
		addSlotToContainer(new SlotFurnace(invPlayer.player, tileWasher, 3, 31, 54));
		
		bindPlayerInventory(invPlayer);
		
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileWasher.isUseableByPlayer(entityplayer);
	}

	
	protected void bindPlayerInventory(InventoryPlayer invPlayer){
		//Render inventory
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 9; j++){
				addSlotToContainer(new Slot(invPlayer, j + (i * 9) + 9, 8 + (j * 18), 84 + (i *18)));
			}
		}
		
		//Render hotbar
		for(int j = 0; j < 9; j++){
			addSlotToContainer(new Slot(invPlayer, j, 8+(j * 18), 142));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot){
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		
		if(slotObject != null && slotObject.getHasStack()){
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();
			
			
			if(slot == 1){
				if(!mergeItemStack(stackInSlot,  4, 39, true)){
					return null;
				}
			}else{
				if(tileWasher.isItemValidForSlot(0, stackInSlot)){
					if(slot == 0){
						if(!mergeItemStack(stackInSlot, 4, 39, false)){
							return null;
						}
					}else{
						if(!mergeItemStack(stackInSlot, 0, 3, false)){
							return null;
						}
					}
				}
			}
				
			if(stackInSlot.stackSize == 0){
				slotObject.putStack(null);
			}else{
				slotObject.onSlotChanged();
			}
			
			if(stackInSlot.stackSize == stack.stackSize){
				return null;
			}
			
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
		
		
	}

}
