package k4unl.minecraft.Hydraulicraft.containers;

import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileInfiniteSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInfiniteSource extends Container {

	protected TileInfiniteSource tileSource;
	
	
	public ContainerInfiniteSource(InventoryPlayer invPlayer, TileInfiniteSource source){
		tileSource = source;
		
		addSlotToContainer(new Slot(source, 0, 35, 49));
		
		bindPlayerInventory(invPlayer);
		
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileSource.isUseableByPlayer(entityplayer);
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
		return null;
	}
}
