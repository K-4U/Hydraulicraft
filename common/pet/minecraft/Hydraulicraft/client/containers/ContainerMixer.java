package pet.minecraft.Hydraulicraft.client.containers;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicMixer;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import pet.minecraft.Hydraulicraft.slots.SlotMachineInput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerMixer extends Container {

	protected TileHydraulicMixer tileMixer;
	
	
	public ContainerMixer(InventoryPlayer invPlayer, TileHydraulicMixer mixer){
		tileMixer = mixer;
		
		addSlotToContainer(new SlotMachineInput(mixer, mixer, 0, 64, 15));
		
		bindPlayerInventory(invPlayer);
		
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileMixer.isUseableByPlayer(entityplayer);
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
			
			
			if(TileEntityFurnace.isItemFuel(stackInSlot)){
				//Places from entity to player
				if(slot < 1){
					if(!mergeItemStack(stackInSlot,  0, 35, true)){
						return null;
					}
				}else if(!mergeItemStack(stackInSlot, 0, 1, false)){
					return null;
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
