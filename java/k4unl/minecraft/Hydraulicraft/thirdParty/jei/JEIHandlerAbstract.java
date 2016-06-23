package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class JEIHandlerAbstract<T extends JEIWrapperIFluidRecipe> implements IRecipeHandler<T> {

    public List<T> getRecipes() {

        List<T> recipes = new ArrayList<T>();

        List<IFluidRecipe> recipesAssembler = getRecipeSource();

        for (IFluidRecipe recipe : recipesAssembler) {
            try {
                T jeiRecipe = getRecipeClass().getConstructor(IFluidRecipe.class).newInstance(recipe);
                recipes.add(jeiRecipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return recipes;
    }

    abstract List<IFluidRecipe> getRecipeSource();

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull T recipe) {

        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull T recipe) {

        return true; // TODO?
    }

    public static class JEIHandlerAssembler extends JEIHandlerAbstract<JEIWrapperIFluidRecipe.JEIWrapperRecipeAssembler> {

        @Override
        List<IFluidRecipe> getRecipeSource() {

            return HydraulicRecipes.getAssemblerRecipes();
        }

        @Nonnull
        @Override
        public Class<JEIWrapperIFluidRecipe.JEIWrapperRecipeAssembler> getRecipeClass() {

            return JEIWrapperIFluidRecipe.JEIWrapperRecipeAssembler.class;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid() {

            return JEIPlugin.assemblerRecipe;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid(@Nonnull JEIWrapperIFluidRecipe.JEIWrapperRecipeAssembler recipe) {

            return null;
        }
    }

    public static class JEIHandlerFilter extends JEIHandlerAbstract<JEIWrapperIFluidRecipe.JEIWrapperRecipeFilter> {

        @Override
        List<IFluidRecipe> getRecipeSource() {

            return HydraulicRecipes.getFilterRecipes();
        }

        @Nonnull
        @Override
        public Class<JEIWrapperIFluidRecipe.JEIWrapperRecipeFilter> getRecipeClass() {

            return JEIWrapperIFluidRecipe.JEIWrapperRecipeFilter.class;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid() {

            return JEIPlugin.filterRecipe;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid(@Nonnull JEIWrapperIFluidRecipe.JEIWrapperRecipeFilter recipe) {

            return null;
        }
    }

    public static class JEIHandlerCrusher extends JEIHandlerAbstract<JEIWrapperIFluidRecipe.JEIWrapperRecipeCrusher> {

        @Override
        List<IFluidRecipe> getRecipeSource() {

            return HydraulicRecipes.getCrusherRecipes();
        }

        @Nonnull
        @Override
        public Class<JEIWrapperIFluidRecipe.JEIWrapperRecipeCrusher> getRecipeClass() {

            return JEIWrapperIFluidRecipe.JEIWrapperRecipeCrusher.class;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid() {

            return JEIPlugin.crusherRecipe;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid(@Nonnull JEIWrapperIFluidRecipe.JEIWrapperRecipeCrusher recipe) {

            return null;
        }
    }

    public static class JEIHandlerWasher extends JEIHandlerAbstract<JEIWrapperIFluidRecipe.JEIWrapperRecipeWasher> {

        @Override
        List<IFluidRecipe> getRecipeSource() {

            return HydraulicRecipes.getWasherRecipes();
        }

        @Nonnull
        @Override
        public Class<JEIWrapperIFluidRecipe.JEIWrapperRecipeWasher> getRecipeClass() {

            return JEIWrapperIFluidRecipe.JEIWrapperRecipeWasher.class;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid() {

            return JEIPlugin.washerRecipe;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid(@Nonnull JEIWrapperIFluidRecipe.JEIWrapperRecipeWasher recipe) {

            return null;
        }
    }

    public static class JEIHandlerRecombobulator extends JEIHandlerAbstract<JEIWrapperIFluidRecipe.JEIWrapperRecipeRecombobulator> {

        @Override
        List<IFluidRecipe> getRecipeSource() {

            return HydraulicRecipes.getRecombobulatorRecipes();
        }

        @Nonnull
        @Override
        public Class<JEIWrapperIFluidRecipe.JEIWrapperRecipeRecombobulator> getRecipeClass() {

            return JEIWrapperIFluidRecipe.JEIWrapperRecipeRecombobulator.class;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid() {

            return JEIPlugin.recombobulatorRecipe;
        }

        @Nonnull
        @Override
        public String getRecipeCategoryUid(@Nonnull JEIWrapperIFluidRecipe.JEIWrapperRecipeRecombobulator recipe) {

            return null;
        }
    }
}
