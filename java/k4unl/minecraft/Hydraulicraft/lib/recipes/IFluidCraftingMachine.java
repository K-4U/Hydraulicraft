package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.item.ItemStack;

public interface IFluidCraftingMachine {
    void onCraftingMatrixChanged();

    void spawnOverflowItemStack(ItemStack stack);
}
