package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.lib.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFluidCrafting extends Slot {
    IFluidInventory fluidMatrixInventory;
    IInventory      resultInventory;
    IFluidRecipe    currentRecipe;

    public SlotFluidCrafting(EntityPlayer player, IFluidInventory craftMatrix, IInventory craftResult, int slotIndex, int x, int y) {
        super(craftResult, slotIndex, x, y);
        this.fluidMatrixInventory = craftMatrix;
        this.resultInventory = craftResult;
    }

    @Override
    protected void onCrafting(ItemStack itemStack) {
        super.onCrafting(itemStack);
        if (currentRecipe == null) {
            Log.warning("Tried crafting NULL recipe?! wat...");
            return;
        }
    }

    public void setRecipe(IFluidRecipe recipe) {
        currentRecipe = recipe;
    }

    public void onMatrixChanged() {
        IFluidRecipe recipe = HydraulicRecipes.getAssemblerRecipe(fluidMatrixInventory);
        currentRecipe = recipe;
        if (recipe != null) {
            resultInventory.setInventorySlotContents(0, recipe.getRecipeOutput());
        }
    }
}
