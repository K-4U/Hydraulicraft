package k4unl.minecraft.Hydraulicraft.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class SlotArmour extends Slot { // I use BrE -> Armour, not Armor :P
    public SlotArmour(IInventory iInventory, int slotId, int x, int y) {
        super(iInventory, slotId, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack == null)
            return true;

        return stack.getItem() instanceof ItemArmor;
    }
}
