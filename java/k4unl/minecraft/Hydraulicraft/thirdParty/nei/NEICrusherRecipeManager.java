package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.PositionedStack;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiCrusher;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class NEICrusherRecipeManager extends NEIHydraulicRecipePlugin {


    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrusher.class;
    }

    @Override
    public String getRecipeName() {
        return Localization.getLocalizedName(Names.blockHydraulicCrusher.unlocalized);
    }

    @Override
    public String getGuiTexture() {
        return ModInfo.LID + ":textures/gui/crusher.png";
    }


    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return false;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crushing")) {
            for (IFluidRecipe recipe : HydraulicRecipes.getCrusherRecipes()) {
                this.arecipes.add(getShape(recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadTransferRects() {
        transferRects = new LinkedList<RecipeTransferRect>();
        transferRects.add(new RecipeTransferRect(new Rectangle(79, 28, 35, 20), "crushing"));
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(80, 22, 207, 0, 34, 19, 48, 2 | (1 << 3));
    }

    @Override
    public CachedRecipe getShape(IFluidRecipe recipe) {
        if (recipe instanceof FluidShapedOreRecipe) {
            // TODO processCrusherRecipe SHAPED
        } else if (recipe instanceof FluidShapelessOreRecipe) {
            return processCrusherRecipe((FluidShapelessOreRecipe) recipe);
        }

        return null;
    }

    private CachedRecipe processCrusherRecipe(FluidShapelessOreRecipe recipe) {
        NEIHydraulicRecipe nei = new NEIHydraulicRecipe();
        nei.addInput(new PositionedStack(recipe.getInput().get(0), 42, 24));
        nei.addOutput(new PositionedStack(recipe.getRecipeOutput(), 116, 24));

        return nei;
    }

    @Override
    public List<IFluidRecipe> getRecipeCollection() {
        return HydraulicRecipes.getCrusherRecipes();
    }

}
