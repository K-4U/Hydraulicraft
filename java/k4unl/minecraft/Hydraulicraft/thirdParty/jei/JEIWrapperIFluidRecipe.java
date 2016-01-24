package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class JEIWrapperIFluidRecipe implements IRecipeWrapper {
   private List             inputs;
   private List<ItemStack>  outputs;
   private List<FluidStack> fluidInputs;
   private List<FluidStack> fluidOutputs;

   public JEIWrapperIFluidRecipe(IFluidRecipe recipe) {
      this.inputs = JEIPlugin.makeList(recipe.getInputItems());
      this.outputs = new ArrayList<ItemStack>();
      this.outputs.add(recipe.getRecipeOutput());
      this.fluidInputs = recipe.getInputFluids();
      this.fluidOutputs = recipe.getOutputFluids();
   }

   @Override
   public List getInputs() {
      return inputs;
   }

   @Override
   public List getOutputs() {
      return outputs;
   }

   @Override
   public List<FluidStack> getFluidInputs() {
      return fluidInputs;
   }

   @Override
   public List<FluidStack> getFluidOutputs() {
      return fluidOutputs;
   }

   @Override
   public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
      // time?
   }

   @Override
   public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
      // TODO
   }

   @Override
   public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
      // TODO
   }

   @Nullable
   @Override
   public List<String> getTooltipStrings(int mouseX, int mouseY) {
      // TODO
      return null;
   }

   @Override
   public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
      // TODO
      return false;
   }

   public static class JEIWrapperRecipeAssembler extends JEIWrapperIFluidRecipe {
      public JEIWrapperRecipeAssembler(IFluidRecipe recipe) {
         super(recipe);
      }
   }

   public static class JEIWrapperRecipeFilter extends JEIWrapperIFluidRecipe {
      public JEIWrapperRecipeFilter(IFluidRecipe recipe) {
         super(recipe);
      }
   }

   public static class JEIWrappedRecipeCrusher extends JEIWrapperIFluidRecipe {
      public JEIWrappedRecipeCrusher(IFluidRecipe recipe) {
         super(recipe);
      }
   }

   public static class JEIWrappedRecipeWasher extends JEIWrapperIFluidRecipe {
      public JEIWrappedRecipeWasher(IFluidRecipe recipe) {
         super(recipe);
      }
   }
}
