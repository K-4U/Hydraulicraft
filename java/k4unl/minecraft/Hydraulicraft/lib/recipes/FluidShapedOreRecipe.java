package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class FluidShapedOreRecipe extends ShapedOreRecipe implements IFluidRecipe {
    List<FluidStack> inputFluids;
    List<FluidStack> outputFluids;

    public FluidShapedOreRecipe(Block result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapedOreRecipe(Item result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapedOreRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapedOreRecipe addFluidInput(FluidStack fluidStack) {
        if (inputFluids == null)
            inputFluids = new ArrayList<FluidStack>();

        inputFluids.add(fluidStack);

        return this;
    }

    public FluidShapedOreRecipe addFluidOutput(FluidStack fluidStack) {
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
        if (!super.matches(inventory.getInventoryCrafting(), null))
            return false;

        int fluidsMatched = 0;
        for (FluidStack fluid : getInputFluids()) {
            if (inventory.drainFluid(fluid, true))
                fluidsMatched++;
        }

        if (fluidsMatched != getInputFluids().size())
            return false;

        fluidsMatched = 0;
        for (FluidStack fluidStack : getOutputFluids()) {
            if (inventory.fillFluid(fluidStack, true))
                fluidsMatched++;
        }

        return fluidsMatched == getOutputFluids().size();
    }
}
