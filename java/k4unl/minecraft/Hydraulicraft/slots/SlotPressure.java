package k4unl.minecraft.Hydraulicraft.slots;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotPressure extends Slot {

    public SlotPressure(IInventory iinventory, int slotId, int x, int y) {

        super(iinventory, slotId, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {

        if (itemStack == null)
            return true;

        return itemStack.getItem() instanceof IPressurizableItem;
    }
}
