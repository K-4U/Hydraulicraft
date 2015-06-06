package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class FluidShapelessOreRecipe extends ShapelessOreRecipe implements IFluidRecipe {
    List<FluidStack> inputFluids;
    List<FluidStack> outputFluids;


    public FluidShapelessOreRecipe(Block result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapelessOreRecipe(Item result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapelessOreRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public FluidShapelessOreRecipe addFluidInput(FluidStack fluidStack) {
        if (inputFluids == null)
            inputFluids = new ArrayList<FluidStack>();

        inputFluids.add(fluidStack);

        return this;
    }

    @Override
    public FluidShapelessOreRecipe addFluidOutput(FluidStack fluidStack) {
        if (outputFluids == null)
            outputFluids = new ArrayList<FluidStack>();

        outputFluids.add(fluidStack);
        return this;
    }

    @Override
    public List<FluidStack> getInputFluids() {
        return inputFluids;
    }

    @Override
    public List<FluidStack> getOutputFluids() {
        return outputFluids;
    }

    @Override
    public boolean matches(IFluidInventory inventory) {
        // TODO actually match recipes
        return true;
    }

    @Override
    public void craft(IFluidInventory inventory) {

    }
}
