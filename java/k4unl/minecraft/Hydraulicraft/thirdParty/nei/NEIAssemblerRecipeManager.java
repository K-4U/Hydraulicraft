package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiAssembler;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class NEIAssemblerRecipeManager extends NEIHydraulicRecipePlugin {

    public CachedRecipe getShape(IFluidRecipe recipe) {
        if (recipe instanceof FluidShapedOreRecipe) {
            return super.processFluidShapedRecipe(recipe);
        } else if (recipe instanceof FluidShapelessOreRecipe) {
            // TODO Fluid Shapeless recipes in NEI handler?
        }

        return null;
    }

    @Override
    public List<IFluidRecipe> getRecipeCollection() {
        return HydraulicRecipes.getAssemblerRecipes();
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiAssembler.class;
    }

    @Override
    public String getRecipeName() {
        return Localization.getLocalizedName(Names.blockHydraulicAssembler.unlocalized);
    }

    @Override
    public String getGuiTexture() {
        return ModInfo.LID + ":textures/gui/assembler.png";
    }


    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return false;
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crushing")) {
            for (IFluidRecipe recipe : HydraulicRecipes.getAssemblerRecipes()) {
                this.arecipes.add(getShape(recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadTransferRects() {
        transferRects = new LinkedList<RecipeTransferRect>();
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 28, 35, 20), "assembling"));
    }

    @Override
    public void drawExtras(int recipe) {
        drawProgressBar(80, 22, 207, 0, 34, 19, 48, 2 | (1 << 3));
        super.drawExtras(recipe);
    }
}
