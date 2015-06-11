package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IFluidRecipe extends IRecipe {
    /**
     * Adds a required fluid input
     *
     * @param fluidStack Added fluid input
     * @return itself for chaining
     */
    IFluidRecipe addFluidInput(FluidStack fluidStack);

    /**
     * Adds an output fluid
     *
     * @param fluidStack added fluid output
     * @return itself for chaining
     */
    IFluidRecipe addFluidOutput(FluidStack fluidStack);

    /**
     * gets input fluids
     *
     * @return input fluids
     */
    List<FluidStack> getInputFluids();

    /**
     * gets output fluids
     *
     * @return output fluids
     */
    List<FluidStack> getOutputFluids();

    /**
     * checks whether given inventory can craft this thing
     *
     * @param inventory inventory to check in
     * @return whether it is possible to craft with this inventory given result
     */
    boolean matches(IFluidInventory inventory);
}
