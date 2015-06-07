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
    EntityPlayer    player;

    public SlotFluidCrafting(EntityPlayer player, IFluidInventory craftMatrix, IInventory craftResult, int slotIndex, int x, int y) {
        super(craftResult, slotIndex, x, y);
        this.fluidMatrixInventory = craftMatrix;
        this.resultInventory = craftResult;
        this.player = player;
    }

    @Override
    protected void onCrafting(ItemStack itemStack) {
        super.onCrafting(itemStack);
        if (currentRecipe == null) {
            Log.warning("Tried crafting NULL recipe?! wat...");
            return;
        }

        // copypasta from SlotCrafting :X
        for (int i = 0; i < fluidMatrixInventory.getSizeInventory(); i++) {
            ItemStack stack = fluidMatrixInventory.getStackInSlot(i);
            if (stack != null) {
                fluidMatrixInventory.decrStackSize(i, 1);
                if (stack.getItem().hasContainerItem(stack)) {
                    ItemStack containerStack = stack.getItem().getContainerItem(stack);
                    if (containerStack != null && containerStack.isItemStackDamageable() && containerStack.getItemDamage() > containerStack.getMaxDamage()) {
                        continue;
                    }

                    if (stack.getItem().doesContainerItemLeaveCraftingGrid(stack) || !player.inventory.addItemStackToInventory(containerStack)) {
                        if (fluidMatrixInventory.getStackInSlot(i) == null)
                            fluidMatrixInventory.setInventorySlotContents(i, containerStack);
                        else
                            player.dropPlayerItemWithRandomChoice(containerStack, false);
                    }
                }
            }
        }

        // TODO decrease input and increase output fluids on crafting
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

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        super.onPickupFromSlot(player, stack);

        onCrafting(stack);
    }
}
