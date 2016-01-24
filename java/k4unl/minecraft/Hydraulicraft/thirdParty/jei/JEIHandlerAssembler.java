package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class JEIHandlerAssembler implements IRecipeHandler<JEIWrapperAssembler> {
   public static List<JEIWrapperAssembler> getRecipes() {
      List<JEIWrapperAssembler> recipes = new ArrayList<JEIWrapperAssembler>();

      List<IFluidRecipe> recipesAssembler = HydraulicRecipes.getAssemblerRecipes();

      for (IFluidRecipe recipe : recipesAssembler) {
         JEIWrapperAssembler jeiRecipe = new JEIWrapperAssembler(
                 JEIPlugin.makeList(recipe.getInputItems()), // cannot cast directly
                 recipe.getRecipeOutput(), recipe.getInputFluids(), recipe.getOutputFluids());

         recipes.add(jeiRecipe);
      }

      return recipes;
   }

   @Nonnull
   @Override
   public Class<JEIWrapperAssembler> getRecipeClass() {
      return JEIWrapperAssembler.class;
   }

   @Nonnull
   @Override
   public String getRecipeCategoryUid() {
      return JEIPlugin.assemblerRecipe;
   }

   @Nonnull
   @Override
   public IRecipeWrapper getRecipeWrapper(@Nonnull JEIWrapperAssembler recipe) {
      return recipe;
   }

   @Override
   public boolean isRecipeValid(@Nonnull JEIWrapperAssembler recipe) {
      return true; // TODO?
   }
}
