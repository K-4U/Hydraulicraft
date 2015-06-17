package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;
import k4unl.minecraft.Hydraulicraft.client.GUI.GuiAssembler;
import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.LinkedList;

/**
 * @author Koen Beckers (K-4U)
 */
public class NEIAssemblerRecipeManager extends ShapedRecipeHandler {

    private ShapedRecipeHandler.CachedShapedRecipe getShape(IFluidRecipe recipe) {
        ShapedRecipeHandler.CachedShapedRecipe shape = new
          ShapedRecipeHandler.CachedShapedRecipe(0, 0, null, recipe.getRecipeOutput());


        //TODO: Get input items and add them to shape.ingredients as a PositionedStack

        //stack.setMaxSize(2);
        //shape.ingredients.add(stack);
        shape.result.relx = 116;
        shape.result.rely = 24;
        return shape;
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for(IFluidRecipe recipe: HydraulicRecipes.getAssemblerRecipes()) {
            if(NEIClientUtils.areStacksSameTypeCrafting(recipe.getRecipeOutput(), result)) {
                this.arecipes.add(getShape(recipe));
            }
        }
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass(){
        return GuiAssembler.class;
    }

    @Override
    public String getRecipeName(){
        return Localization.getLocalizedName(Names.blockHydraulicCrusher.unlocalized);
    }

    @Override
    public String getGuiTexture(){
        return ModInfo.LID + ":textures/gui/assembler.png";
    }


    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe){
        return false;
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for(CrushingRecipes.CrushingRecipe recipe: CrushingRecipes.crushingRecipes) {
            if(recipe.inputString != ""){
                int[] oreIds = OreDictionary.getOreIDs(ingredient);

                for(int id : oreIds){
                    if(id > 0){
                        String oreName = OreDictionary.getOreName(id);
                        if(recipe.inputString == oreName){
                            //this.arecipes.add(getShape(recipe));
                            break;
                        }
                    }
                }
            }else{
                if(recipe.input != null && ingredient != null) {
                    if (recipe.input.isItemEqual(ingredient)) {
                        //this.arecipes.add(getShape(recipe));
                        break;
                    }
                }
            }
        }
    }



    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals("crushing")){
            for(CrushingRecipes.CrushingRecipe recipe: CrushingRecipes.crushingRecipes){
                //this.arecipes.add(getShape(recipe));
            }
        }else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadTransferRects(){
        transferRects = new LinkedList<RecipeTransferRect>();
        transferRects.add(new RecipeTransferRect(new Rectangle(74, 28, 35, 20), "crushing"));
    }

    @Override
    public void drawExtras(int recipe){
        drawProgressBar(80, 22, 207, 0, 34, 19, 48, 2 | (1 << 3));
    }
}
