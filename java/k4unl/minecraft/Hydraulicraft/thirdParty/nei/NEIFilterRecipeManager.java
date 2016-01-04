package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

public class NEIFilterRecipeManager {//extends NEIHydraulicRecipePlugin {
/*
    @Override
    public CachedRecipe getShape(IFluidRecipe recipe) {
        if (recipe instanceof FluidShapedOreRecipe) {
            // TODO Filter shaped recipes?
        } else if (recipe instanceof FluidShapelessOreRecipe) {
            return processRecipe(recipe);
        }

        return null;
    }

    @Override
    protected CachedRecipe processRecipe(IFluidRecipe recipe) {
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
        transferRects.add(new RecipeTransferRect(new Rectangle(95, 5, 25, 60), "filtering"));
    }
    */
}
