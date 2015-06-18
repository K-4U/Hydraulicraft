package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class NEIHydraulicRecipePlugin extends TemplateRecipeHandler {
    @Override
    public String getGuiTexture() {
        return null;
    }

    @Override
    public String getRecipeName() {
        return null;
    }

    public class NEIHydraulicRecipe extends CachedRecipe {
        private List<PositionedStack> input       = new ArrayList<PositionedStack>();
        private List<PositionedStack> output      = new ArrayList<PositionedStack>();
        private List<NEIWidgetTank>   inputFluid  = new ArrayList<NEIWidgetTank>();
        private List<NEIWidgetTank>   outputFluid = new ArrayList<NEIWidgetTank>();

        public NEIHydraulicRecipe addInput(FluidStack fluidStack, int x, int y) {

            // TODO fluid adding

            return this;
        }

        public NEIHydraulicRecipe addInput(PositionedStack[] itemStacks) {
            for (PositionedStack stack : itemStacks)
                input.add(stack);

            return this;
        }

        public NEIHydraulicRecipe addInput(PositionedStack stack) {
            input.add(stack);

            return this;
        }

        public NEIHydraulicRecipe addOutput(PositionedStack stack) {
            output.add(stack);

            return this;
        }

        public NEIHydraulicRecipe addOutput(FluidStack fluidStack, int x, int y) {
            // TODO fluid adding

            return this;
        }

        public NEIHydraulicRecipe addOutput(PositionedStack[] stacks) {
            for (PositionedStack stack : stacks)
                output.add(stack);

            return this;
        }


        @Override
        public PositionedStack getResult() {
            if (output.size() == 1)
                return output.get(0);

            return null;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return input;
        }

        public List<NEIWidgetTank> getOutputFluid() {
            return outputFluid;
        }

        public List<NEIWidgetTank> getInputFluid() {
            return inputFluid;
        }
    }
}
