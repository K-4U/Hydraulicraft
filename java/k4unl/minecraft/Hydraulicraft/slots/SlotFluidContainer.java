package k4unl.minecraft.Hydraulicraft.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotFluidContainer extends Slot {

    public SlotFluidContainer(IInventory iinventory, int slotId, int x, int y) {

        super(iinventory, slotId, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {

        if (itemStack == null)
            return true;

        return itemStack.getItem() instanceof IFluidContainerItem || FluidContainerRegistry.isContainer(itemStack);
    }
}
