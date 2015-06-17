package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import net.minecraft.inventory.InventoryCraftResult;

public class InventoryFluidCraftResult extends InventoryCraftResult {
    IFluidCraftingMachine machine;
    IFluidRecipe          recipe;

    public InventoryFluidCraftResult(IFluidCraftingMachine baseEntity) {

        super();
        this.machine = baseEntity;
    }

    public void setRecipe(IFluidRecipe recipe) {

        this.recipe = recipe;
        if (recipe == null)
            setInventorySlotContents(0, null);
        else
            setInventorySlotContents(0, recipe.getRecipeOutput());
    }
}
