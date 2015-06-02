package k4unl.minecraft.Hydraulicraft.slots;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMachineInput extends Slot {
    private IHydraulicMachine ent;

    public SlotMachineInput(IInventory inv, IHydraulicMachine machine, int index, int x,
                            int y) {
        super(inv, index, x, y);
        ent = machine;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemStack) {
        if (ent instanceof IInventory) {
            return ((IInventory) ent).isItemValidForSlot(this.getSlotIndex(), itemStack);
        }

        return false;
    }
}
