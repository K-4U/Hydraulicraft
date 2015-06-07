package k4unl.minecraft.Hydraulicraft.slots;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCrusher;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMachineInput extends Slot {
	private IHydraulicMachine ent;
	
	public SlotMachineInput(IInventory inv, IHydraulicMachine machine, int par2, int par3,
			int par4) {
		super(inv, par2, par3, par4);
		ent = machine;
	}
	
    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
	@Override
    public boolean isItemValid(ItemStack itemStack){
		if(ent instanceof TileHydraulicFrictionIncinerator){
			return ((TileHydraulicFrictionIncinerator)ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}else if(ent instanceof TileHydraulicPump){
			return ((TileHydraulicPump) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}else if(ent instanceof TileHydraulicCrusher){
			return ((TileHydraulicCrusher) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}else if(ent instanceof TileHydraulicFilter){
			return ((TileHydraulicFilter) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}else if(ent instanceof TileHydraulicWasher){
			return ((TileHydraulicWasher) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}else if(ent instanceof TileHydraulicPressureVat){
			return ((TileHydraulicPressureVat) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}else if(ent instanceof TileHydraulicHarvester){
			return ((TileHydraulicHarvester) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
		}
		return true;
    }
}
