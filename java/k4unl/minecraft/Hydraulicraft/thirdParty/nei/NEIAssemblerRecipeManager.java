package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
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
import k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets.NEIWidgetTank;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.*;
import java.util.LinkedList;

/**
 * @author Koen Beckers (K-4U)
 */
public class NEIAssemblerRecipeManager extends NEIHydraulicRecipePlugin {

    private CachedRecipe getShape(IFluidRecipe recipe) {
        /*ShapedRecipeHandler.CachedShapedRecipe shape = new
                ShapedRecipeHandler.CachedShapedRecipe(0, 0, null, recipe.getRecipeOutput());


        //TODO: Get input items and add them to shape.ingredients as a PositionedStack

        //stack.setMaxSize(2);
        //shape.ingredients.add(stack);
        shape.result.relx = 116;
        shape.result.rely = 24;
        return shape;*/
        if (recipe instanceof FluidShapedOreRecipe) {
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
            outRecipe.addInput(recipe1.getInputFluids().get(0), 42, 42);

            return outRecipe;

        } else if (recipe instanceof FluidShapelessOreRecipe) {
            // TODO Fluid Shapeless recipes in NEI handler?
        }

        return null;
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IFluidRecipe recipe : HydraulicRecipes.getAssemblerRecipes()) {
            if (NEIClientUtils.areStacksSameTypeCrafting(recipe.getRecipeOutput(), result)) {
                this.arecipes.add(getShape(recipe));
            }
        }
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
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IFluidRecipe recipe : HydraulicRecipes.getAssemblerRecipes()) {
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
        CachedRecipe cachedRecipe = arecipes.get(recipe);
        if (cachedRecipe instanceof NEIHydraulicRecipe) {
            NEIHydraulicRecipe hydraulicRecipe = (NEIHydraulicRecipe) cachedRecipe;
            for (NEIWidgetTank tank : hydraulicRecipe.getInputFluid()) {
                tank.render(this);
            }
        }
    }
}
