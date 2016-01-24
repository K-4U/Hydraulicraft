package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

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

      if (recipeWrapper instanceof JEIWrapperIFluidRecipe) {
         List inputs = recipeWrapper.getInputs();

         for (int i = 0; i < inputs.size(); i++)
            itemStacks.init(i, true, getPointForInput(i).x, getPointForInput(i).y);

         // inputs.size() == n+1 array index
         // TODO multiple outputs?
         itemStacks.init(inputs.size(), false, getPointForOutput(0).x, getPointForOutput(0).y);

         for (int i = 0; i < inputs.size(); i++)
            itemStacks.setFromRecipe(i, inputs.get(i));

         itemStacks.setFromRecipe(inputs.size(), recipeWrapper.getOutputs().get(0));
      }
   }

   public abstract Point getPointForInput(int i);

   public abstract Point getPointForOutput(int i);
}
