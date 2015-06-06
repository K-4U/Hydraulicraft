package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.inventory.InventoryCraftResult;
import net.minecraftforge.fluids.FluidStack;

public class InventoryFluidCraftResult extends InventoryCraftResult implements IFluidInventory {
    protected SlotFluidCrafting slotFluidCrafting;
    private   IFluidInventory   inventory;

    public InventoryFluidCraftResult(IFluidInventory inventory, SlotFluidCrafting craftingSlot) {
        this.inventory = inventory;
        this.slotFluidCrafting = craftingSlot;
    }

    @Override
    public boolean drainFluid(FluidStack fluidStack, boolean pretend) {
        return inventory.drainFluid(fluidStack, pretend);
    }

    @Override
    public boolean fillFluid(FluidStack fluidStack, boolean pretend) {
        return inventory.fillFluid(fluidStack, pretend);
    }

    public IFluidRecipe getRecipe() {
        return slotFluidCrafting.currentRecipe;
    }

    public void setRecipe(IFluidRecipe recipe) {
        slotFluidCrafting.setRecipe(recipe);
    }
}
