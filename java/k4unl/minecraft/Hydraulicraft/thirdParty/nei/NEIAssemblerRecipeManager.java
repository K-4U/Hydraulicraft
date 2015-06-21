package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.core.ReflectionManager;
import codechicken.nei.PositionedStack;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiAssembler;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class NEIAssemblerRecipeManager extends NEIHydraulicRecipePlugin {

    @Override
    public CachedRecipe getShape(IFluidRecipe recipe) {
        if (recipe instanceof FluidShapedOreRecipe) {
            return processAssemblerRecipe(recipe);
        } else if (recipe instanceof FluidShapelessOreRecipe) {
            // TODO processAssemblerRecipe SHAPELESS
        }

        return null;
    }


    protected CachedRecipe processAssemblerRecipe(IFluidRecipe recipe) {
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
                    outRecipe.addInput(new PositionedStack(recipe1.getInput()[x * width + y], 48 + y * 18, 6 + x * 18));

        outRecipe.addOutput(new PositionedStack(recipe1.getRecipeOutput(), 126, 24));
        outRecipe.addInput(recipe1.getInputFluids().get(0), 26, 59, 16, 54);

        return outRecipe;
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
        if (outputId.equals("assembling")) {
            for (IFluidRecipe recipe : HydraulicRecipes.getAssemblerRecipes()) {
                this.arecipes.add(getShape(recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadTransferRects() {
        transferRects = new LinkedList<RecipeTransferRect>();
        transferRects.add(new RecipeTransferRect(new Rectangle(104, 27, 18, 11), "assembling"));
    }

    @Override
    public void drawExtras(int recipe) {
        //drawProgressBar(104, 27, 207, 0, 34, 19, 48, 2 | (1 << 3));
        super.drawExtras(recipe);
    }
}
