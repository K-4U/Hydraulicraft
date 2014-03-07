package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiCrusher;
import k4unl.minecraft.Hydraulicraft.client.GUI.IconRenderer;
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


        PositionedStack stack = new PositionedStack(recipe.input, 51, 5);
        stack.setMaxSize(2);
        shape.ingredients.add(stack);
        shape.result.relx = 101;
        shape.result.rely = 45;
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
    
    @Override
    public void drawExtras(int recipeIndex){
    	int ticks = 48;
    	float percentage = cycleticks % ticks / (float)ticks;
    	
    	CachedRecipe recipe = arecipes.get(recipeIndex); 
    	
    	
    	ItemStack washingItem = recipe.getIngredients().get(0).item;
		ItemStack targetItem = recipe.getResult().item;	
		int startY = 5;
		int startX = 51;
		int targetX = 101;
		int targetY = 0;
		int travelPath = 0;
		int xPos = startX;
		int yPos = startY;
		
		if(percentage < 0.25F){
			targetX = 75;
			travelPath = (targetX - startX) * 4;
			xPos = startX + (int) (travelPath * percentage);
		}else if(percentage < 0.75F){
			xPos = 75;
			targetY = 45;
			travelPath = targetY - startY;
			yPos = startY + (int) (travelPath * ((percentage-0.25F)*2));
		}else{
			yPos = 45;
			targetX = 101;
			startX = 75;
			travelPath = targetX - startX;
			xPos = startX + (int) (travelPath * (percentage-0.75F)*4);
		}
		
		IconRenderer.drawMergedIcon(xPos, yPos, 0.0F, washingItem, targetItem, percentage, true);
    }
}
