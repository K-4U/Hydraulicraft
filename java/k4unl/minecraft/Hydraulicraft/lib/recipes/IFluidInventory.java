package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public interface IFluidInventory extends IInventory {
    /**
     * Drains fluid from OUTPUT (= machine output drain)
     *
     * @param fluidStack fluid stack to drain
     * @param doDrain    pretend to drain (ie check for whether possible)
     * @return how much was drained
     */
    FluidStack drain(FluidStack fluidStack, boolean doDrain);

    /**
     * Drains fluid from INPUT (= when crafting)
     *
     * @param fluidStack fluid stack to drain
     * @param doDrain    pretend to drain
     * @return how much was drained
     */
    FluidStack craftingDrain(FluidStack fluidStack, boolean doDrain);

    /**
     * Fills fluid to INPUT (= machine input drain)
     *
     * @param fluidStack fluid stack to fill
     * @param doDrain    pretend to fill (ie check for whether possible)
     * @return how much was filled
     */
    int fill(FluidStack fluidStack, boolean doDrain);

    /**
     * Fills fluid to OUTPUT (= when crafting)
     *
     * @param fluidStack fluid stack to fill
     * @param doDrain    pretend to fill
     * @return how much was filled
     */
    int craftingFill(FluidStack fluidStack, boolean doDrain);


    /**
     * Gets inventory crafting for checking recipes
     *
     * @return inventory crafting instance with slots
     */
    InventoryCrafting getInventory();

    /**
     * Eats all the fluids in a recipe
     *
     * @param percent how many % to eat (usually relative to recipe's processing time)
     * @param recipe  recipe that is being processed
     */
    void recipeTick(IFluidRecipe recipe);

    /**
     * Drain from inventory
     *
     * @param maxDrain max amount of fluid to drain
     * @param doDrain  actually drain
     * @return drained fluid stack
     */
    FluidStack drain(int maxDrain, boolean doDrain);

    /**
     * Check whether this fluid can be filled in the inventory
     *
     * @param fluid Fulid to fill
     * @return fillable
     */
    boolean canFill(Fluid fluid);

    /**
     * Check whether this fluid can be drained from the inventory
     *
     * @param fluid Fluid to drain
     * @return drainable
     */
    boolean canDrain(Fluid fluid);

    /**
     * Information about the tank(s)
     *
     * @return tank information
     */
    FluidTankInfo[] getTankInfo();
}
