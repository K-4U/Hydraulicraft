package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.WidgetBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;

public abstract class NEIHydraulicRecipePlugin extends TemplateRecipeHandler {
    @Override
    public String getGuiTexture() {
        return null;
    }

    @Override
    public String getRecipeName() {
        return null;
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        CachedRecipe cachedRecipe = arecipes.get(recipe);

        if (cachedRecipe instanceof NEIHydraulicRecipe) { // better safe than sorry
            NEIHydraulicRecipe hydraulicRecipe = (NEIHydraulicRecipe) cachedRecipe;
            for (WidgetBase widget : hydraulicRecipe.getWidgets()) {
                widget.handletooltip(gui, currenttip, recipe);
            }
        }
        return super.handleTooltip(gui, currenttip, recipe);
    }

    @Override
    public void drawExtras(int recipe) {
        CachedRecipe cachedRecipe = arecipes.get(recipe);
        if (cachedRecipe instanceof NEIHydraulicRecipe) {
            NEIHydraulicRecipe hydraulicRecipe = (NEIHydraulicRecipe) cachedRecipe;
            for (WidgetBase widget : hydraulicRecipe.getWidgets()) {
                widget.render();
            }
        }
        super.drawExtras(recipe);
    }

    public abstract CachedRecipe getShape(IFluidRecipe recipe);

    public abstract List<IFluidRecipe> getRecipeCollection();

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IFluidRecipe recipe : getRecipeCollection()) {
            for (Object item : recipe.getInputItems()) {
                if (item instanceof ItemStack) {
                    if (ingredient.isItemEqual((ItemStack) item)) {
                        this.arecipes.add(getShape(recipe));
                        break;
                    }
                } else if (item instanceof String) {
                    int[] oreIds = OreDictionary.getOreIDs(ingredient);
                    for (int id : oreIds) {
                        String oreName = OreDictionary.getOreName(id);
                        if (oreName.equals(item)) {
                            this.arecipes.add(getShape(recipe));
                            break;
                        }
                    }
                }
            }
        }
    }

    protected CachedRecipe processFluidShapedRecipe(IFluidRecipe recipe) {
        int width, height;
        try {
            width = ((Integer) ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4)).intValue();
            height = ((Integer) ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5)).intValue();
        } catch (IllegalAccessException e) {
            Log.error("Error accessing recipe size!");
            return null;
        }

        FluidShapedOreRecipe recipe1 = (FluidShapedOreRecipe) recipe;
        NEIHydraulicRecipe outRecipe = new NEIHydraulicRecipe();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (recipe1.getInput()[x * width + y] != null)
                    outRecipe.addInput(new PositionedStack(recipe1.getInput()[x * width + y], 48 + x * 18, 6 + y * 18));

        outRecipe.addOutput(new PositionedStack(recipe1.getRecipeOutput(), 126, 24));
        outRecipe.addInput(recipe1.getInputFluids().get(0), 26, 59, 16, 54);

        return outRecipe;
    }


    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IFluidRecipe recipe : getRecipeCollection()) {
            if (NEIClientUtils.areStacksSameTypeCrafting(recipe.getRecipeOutput(), result)) { // TODO check for ChancedStack
                this.arecipes.add(getShape(recipe));
            }
        }
    }

    public class NEIHydraulicRecipe extends CachedRecipe {
        private List<PositionedStack> input   = new ArrayList<PositionedStack>();
        private List<PositionedStack> output  = new ArrayList<PositionedStack>();
        private List<WidgetBase>      widgets = new ArrayList<WidgetBase>();

        public NEIHydraulicRecipe addInput(FluidStack fluidStack, int x, int y, int width, int height) {

            NEIWidgetTank tank = new NEIWidgetTank(fluidStack, x, y, width, height);
            widgets.add(tank);
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

        public NEIHydraulicRecipe addWidget(WidgetBase widgetBase) {
            this.widgets.add(widgetBase);

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

        public List<WidgetBase> getWidgets() {
            return widgets;
        }
    }
}
