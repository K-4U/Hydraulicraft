package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

public class NEICrusherRecipeManager {// extends NEIHydraulicRecipePlugin {
/*

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
            // TODO processRecipe SHAPED
        } else if (recipe instanceof FluidShapelessOreRecipe) {
            return processRecipe(recipe);
        }

        return null;
    }

    @Override
    protected CachedRecipe processRecipe(IFluidRecipe recipe) {
        NEIHydraulicRecipe nei = new NEIHydraulicRecipe();
        nei.addInput(new PositionedStack(recipe.getInputItems()[0], 42, 24));
        nei.addOutput(new PositionedStack(recipe.getRecipeOutput(), 116, 24));

        return nei;
    }

    @Override
    public List<IFluidRecipe> getRecipeCollection() {
        return HydraulicRecipes.getCrusherRecipes();
    }
*/
}
