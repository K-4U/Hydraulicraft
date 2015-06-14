package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public interface IFluidInventory extends IInventory {
    /**
     * Drains fluid when crafting
     *
     * @param fluidStack fluid stack to drain
     * @param doDrain    pretend to drain (ie check for whether possible)
     * @return how much was filled
     */
    FluidStack drain(FluidStack fluidStack, boolean doDrain);

    FluidStack craftingDrain(FluidStack fluidStack, boolean doDrain);

    /**
     * Fills fluid when crafting
     *
     * @param fluidStack fluid stack to fill
     * @param doDrain    pretend to fill (ie check for whether possible)
     * @return how much was filled
     */
    int fill(FluidStack fluidStack, boolean doDrain);

    int craftingFill(FluidStack fluidStack, boolean doDrain);


    /**
     * Gets inventory crafting for checking recipes
     *
     * @return inventory crafting instance with slots
     */
    InventoryCrafting getInventoryCrafting();

    void eatFluids(IFluidRecipe recipe, float percent);

    FluidStack drain(int maxDrain, boolean doDrain);


    boolean canFill(Fluid fluid);

    boolean canDrain(Fluid fluid);

    FluidTankInfo[] getTankInfo();
}
