package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.item.ItemStack;

public interface IFluidCraftingMachine {
    /**
     * Feedback called from a fluid inventory to notify of a crafting grid change
     */
    void onCraftingMatrixChanged();

    /**
     * When a crafting has an container item that has to leave the grid
     *
     * @param stack stack to spew out (or move to external/internal chest)
     */
    void spawnOverflowItemStack(ItemStack stack);
}
