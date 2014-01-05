package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiCrusher;
import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NEIWasherRecipeManager extends ShapedRecipeHandler{
    private ShapedRecipeHandler.CachedShapedRecipe getShape(WashingRecipes.WashingRecipe recipe) {
        ShapedRecipeHandler.CachedShapedRecipe shape = new
                ShapedRecipeHandler.CachedShapedRecipe(0, 0, null, recipe.output);


        PositionedStack stack = new PositionedStack(recipe.input, 51, 3);
        stack.setMaxSize(2);
        shape.ingredients.add(stack);
        shape.result.relx = 101;
        shape.result.rely = 43;
        return shape;
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for(WashingRecipes.WashingRecipe recipe: WashingRecipes.washingRecipes) {
            if(NEIClientUtils.areStacksSameTypeCrafting(recipe.output, result)) {
                this.arecipes.add(getShape(recipe));
            }
        }
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass(){
        return GuiCrusher.class;
    }

    @Override
    public String getRecipeName(){
        return Names.blockHydraulicWasher.localized;
    }

    @Override
    public String getGuiTexture(){
        return ModInfo.LID + ":textures/gui/washer.png";
    }


    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
    {
        return false;
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for(WashingRecipes.WashingRecipe recipe: WashingRecipes.washingRecipes) {
            if(NEIClientUtils.areStacksSameTypeCrafting(recipe.input,
                    ingredient)) {
                this.arecipes.add(getShape(recipe));
                break;
            }
        }
    }



    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("crafting") && getClass() ==
                NEIWasherRecipeManager.class) {
            for(WashingRecipes.WashingRecipe recipe: WashingRecipes.washingRecipes) {
                this.arecipes.add(getShape(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
}
