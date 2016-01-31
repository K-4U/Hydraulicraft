package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public abstract class JEICategoryAbstract implements IRecipeCategory {
   private IDrawable background;

   public JEICategoryAbstract(IGuiHelper helper) {
      ResourceLocation location = new ResourceLocation("hydcraft", "textures/gui/" + getBackgroundTextureName() + ".png");
      background = helper.createDrawable(location, getBackgroundU(), getBackgroundV(), getBackgroundWidth(), getBackgroundHeight());
   }

   public int getBackgroundHeight() {
      return 57;
   }

   public int getBackgroundWidth() {
      return 169;
   }

   public int getBackgroundV() {
      return 14;
   }

   public int getBackgroundU() {
      return 6;
   }

   public abstract String getBackgroundTextureName();

   @Nonnull
   @Override
   public IDrawable getBackground() {
      return background;
   }

   @Override
   public void drawExtras(Minecraft minecraft) {
      // TODO draw fluid graph
   }

   @Override
   public void drawAnimations(Minecraft minecraft) {
      // TODO animations
   }

   @Override
   public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
      IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
      IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

      if (recipeWrapper instanceof JEIWrapperIFluidRecipe) {
         List inputs = recipeWrapper.getInputs();
         List<FluidStack> inFluids = recipeWrapper.getFluidInputs();
         List<FluidStack> outFluids = recipeWrapper.getFluidOutputs();

         for (int i = 0; i < inputs.size(); i++)
            itemStacks.init(i, true, getPointForInput(i).x, getPointForInput(i).y);

         // inputs.size() == n+1 array index
         // TODO multiple outputs?
         itemStacks.init(inputs.size(), false, getPointForOutput(0).x, getPointForOutput(0).y);

         for (int i = 0; i < inputs.size(); i++)
            itemStacks.setFromRecipe(i, inputs.get(i));

         itemStacks.setFromRecipe(inputs.size(), recipeWrapper.getOutputs().get(0));

         if (inFluids != null)
            for (int i = 0; i < inFluids.size(); i++) {
               fluidStacks.init(i, true, getRectangleForFluidInput(i).x, getRectangleForFluidInput(i).y, getRectangleForFluidInput(i).width, getRectangleForFluidInput(i).height,
                       inFluids.get(i).amount, true, null); // TODO overlay
               fluidStacks.set(i, inFluids.get(i));
            }

         if (outFluids != null)
            for (int i = 0; i < outFluids.size(); i++) {
               fluidStacks.init((inFluids != null ? inFluids.size() : 0) + i, false, getRectangleForFluidOutput(i).x, getRectangleForFluidOutput(i).y, getRectangleForFluidOutput(i).width,
                       getRectangleForFluidOutput(i).height, outFluids.get(i).amount, true, null); // TODO overlay
               fluidStacks.set((inFluids != null ? inFluids.size() : 0) + i, outFluids.get(i));
            }
      }
   }

   public abstract Rectangle getRectangleForFluidOutput(int i);

   public abstract Rectangle getRectangleForFluidInput(int i);

   public abstract Point getPointForInput(int i);

   public abstract Point getPointForOutput(int i);
}
