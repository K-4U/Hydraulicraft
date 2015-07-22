package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.PositionedStack;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiWasher;
import k4unl.minecraft.Hydraulicraft.client.GUI.IconRenderer;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;


public class NEIWasherRecipeManager extends NEIHydraulicRecipePlugin {

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiWasher.class;
    }

    @Override
    public String getRecipeName() {
        return Localization.getLocalizedName(Names.blockHydraulicWasher.unlocalized);
    }

    @Override
    public String getGuiTexture() {
        return ModInfo.LID + ":textures/gui/washer.png";
    }


    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return false;
    }

    @Override
    public void loadTransferRects() {
        transferRects = new LinkedList<RecipeTransferRect>();
        transferRects.add(new RecipeTransferRect(new Rectangle(27, 30, 20, 35), "washing"));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("washing")) {
            for (IFluidRecipe recipe : HydraulicRecipes.getWasherRecipes()) {
                this.arecipes.add(getShape(recipe));
            }
        } else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void drawExtras(int recipeIndex) {
        int ticks = 48;
        float percentage = cycleticks % ticks / (float) ticks;

        CachedRecipe recipe = arecipes.get(recipeIndex);


        ItemStack washingItem = recipe.getIngredients().get(0).item;
        ItemStack targetItem = recipe.getResult().item;
        int startY = 5;
        int startX = 51;
        int targetX;
        int targetY;
        int travelPath;
        int xPos;
        int yPos = startY;

        if (percentage < 0.25F) {
            targetX = 75;
            travelPath = (targetX - startX) * 4;
            xPos = startX + (int) (travelPath * percentage);
        } else if (percentage < 0.75F) {
            xPos = 75;
            targetY = 45;
            travelPath = targetY - startY;
            yPos = startY + (int) (travelPath * ((percentage - 0.25F) * 2));
        } else {
            yPos = 45;
            targetX = 101;
            startX = 75;
            travelPath = targetX - startX;
            xPos = startX + (int) (travelPath * (percentage - 0.75F) * 4);
        }

        IconRenderer.drawMergedIcon(xPos, yPos, 0.0F, washingItem, targetItem, percentage, true);
    }

    @Override
    public CachedRecipe getShape(IFluidRecipe recipe) {
        return processRecipe(recipe);
    }

    @Override
    protected CachedRecipe processRecipe(IFluidRecipe recipe) {
        NEIHydraulicRecipe nei = new NEIHydraulicRecipe();
        nei.addInput(new PositionedStack(recipe.getInputItems()[0], 52, 6));
        nei.addOutput(new PositionedStack(recipe.getRecipeOutput(), 101, 46));
        return nei;
    }

    @Override
    public List<IFluidRecipe> getRecipeCollection() {
        return HydraulicRecipes.getWasherRecipes();
    }
}
