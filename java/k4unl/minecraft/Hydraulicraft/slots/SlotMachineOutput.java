package k4unl.minecraft.Hydraulicraft.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMachineOutput extends Slot {
	
	public SlotMachineOutput(IInventory inv, int par2, int par3,
			int par4) {
		super(inv, par2, par3, par4);
	}
	
    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
	@Override
    public boolean isItemValid(ItemStack itemStack){
		return false;
    }
}
