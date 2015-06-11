package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.item.ItemStack;

public class Helpers {
    public static boolean canMergeStacks(ItemStack a, ItemStack b) {
        if (a == null || b == null)
            return true;

        if (a.getItem() == b.getItem() && a.getMaxStackSize() >= a.stackSize + b.stackSize)
            return true;

        return false;
    }

    public static ItemStack mergeStacks(ItemStack a, ItemStack b) {
        if (a == null && b != null)
            return b.copy();
        else if (b == null && a != null)
            return a.copy();
        else if (a == null && b == null)
            return null;

        a.stackSize += b.stackSize;
        return a;
    }
}
