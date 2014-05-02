package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.slots.SlotMachineInput;
import k4unl.minecraft.Hydraulicraft.slots.SlotMachineOutput;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHarvester extends Container {
	protected TileHydraulicHarvester tileHarvester;
	
	
	public ContainerHarvester(InventoryPlayer invPlayer, TileHydraulicHarvester harvester){
		tileHarvester = harvester;
		
		//First inputs!
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				addSlotToContainer(new SlotMachineInput(harvester, harvester, (i*3) + j, 27 + (i * 18), 17 + (j*18)));
			}
		}
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				addSlotToContainer(new SlotMachineOutput(harvester, 9 + (i*3) + j, 96 + (i * 18), 17 + (j*18)));
			}
		}
		
		bindPlayerInventory(invPlayer);
		
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileHarvester.isUseableByPlayer(entityplayer);
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
			
			//From output slot to player inventory
			
			if(slot == 1){
				if(!mergeItemStack(stackInSlot,  2, 37, true)){
					return null;
				}
			}else{
				if(tileHarvester.isItemValidForSlot(0, stackInSlot)){
					if(slot == 0){
						if(!mergeItemStack(stackInSlot, 2, 37, false)){
							return null;
						}
					}else{
						if(!mergeItemStack(stackInSlot, 0, 1, false)){
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
