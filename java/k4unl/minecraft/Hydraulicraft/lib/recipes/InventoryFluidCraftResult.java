package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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

    public void save(NBTTagCompound nbt) {
        if (getStackInSlot(0) != null)
            nbt.setTag("output", getStackInSlot(0).writeToNBT(new NBTTagCompound()));
    }

    public void load(NBTTagCompound nbt) {
        setInventorySlotContents(0, null);

        if (nbt.getTag("output") != null && nbt.getTag("output") instanceof NBTTagCompound) {
            setInventorySlotContents(0, ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("output")));
        }
    }

    public boolean canExtract(int slot, ItemStack itemStack) {
        return slot >= 0 && getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(itemStack);

    }
}
