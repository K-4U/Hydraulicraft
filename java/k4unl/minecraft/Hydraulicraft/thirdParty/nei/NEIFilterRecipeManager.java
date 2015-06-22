package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.PositionedStack;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiFilter;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class NEIFilterRecipeManager extends NEIHydraulicRecipePlugin {

    @Override
    public CachedRecipe getShape(IFluidRecipe recipe) {
        if (recipe instanceof FluidShapedOreRecipe) {
            // TODO Filter shaped recipes?
        } else if (recipe instanceof FluidShapelessOreRecipe) {
            return processFilterRecipe(recipe);
        }

        return null;
    }


    protected CachedRecipe processFilterRecipe(IFluidRecipe recipe) {
        NEIHydraulicRecipe nei = new NEIHydraulicRecipe();

        nei.addInput(recipe.getInputFluids().get(0), 33, 60, 16, 54);
        nei.addInput(recipe.getOutputFluids().get(0), 121, 60, 16, 54);
        nei.addInput(new PositionedStack(recipe.getInputItems()[0], 77, 25));
        nei.addInput(new PositionedStack(recipe.getRecipeOutput(), 77, 44));

        return nei;
    }

    @Override
    public List<IFluidRecipe> getRecipeCollection() {
        return HydraulicRecipes.getFilterRecipes();
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiFilter.class;
    }

    @Override
    public String getRecipeName() {
        return Localization.getLocalizedName(Names.blockHydraulicFilter.unlocalized);
    }

    @Override
    public String getGuiTexture() {
        return ModInfo.LID + ":textures/gui/filter.png";
    }


    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return false;
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("filtering")) {
            for (IFluidRecipe recipe : HydraulicRecipes.getFilterRecipes()) {
                this.arecipes.add(getShape(recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadTransferRects() {
        transferRects = new LinkedList<RecipeTransferRect>();
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 5, 25, 60), "filtering"));
        transferRects.add(new RecipeTransferRect(new Rectangle(90, 5, 25, 60), "filtering"));
    }

    @Override
    public void drawExtras(int recipe) {
        //drawProgressBar(104, 27, 207, 0, 34, 19, 48, 2 | (1 << 3));
        super.drawExtras(recipe);
    }
}
