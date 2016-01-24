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
import java.util.List;

public class JEICategoryAssembler implements IRecipeCategory {
   private static final int offsetLeft = 46;
   private static final int offsetTop  = 2;

   private static final int outputLeft = 124;
   private static final int outputTop  = 20;

   private IDrawable background;

   public JEICategoryAssembler(IGuiHelper helper) {
      ResourceLocation location = new ResourceLocation("hydcraft", "textures/gui/assembler.png");
      background = helper.createDrawable(location, 6, 14, 169, 57);
   }

   @Nonnull
   @Override
   public String getUid() {
      return JEIPlugin.assemblerRecipe;
   }

   @Nonnull
   @Override
   public String getTitle() {
      return "Assembler"; // TODO localize
   }

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

      for (int i = 0; i < 9; i++)
         itemStacks.init(i, true, i % 3 * 18 + offsetLeft, i / 3 * 18 + offsetTop);

      itemStacks.init(9, false, outputLeft, outputTop);

      if (recipeWrapper instanceof JEIWrapperAssembler) {
         List inputs = recipeWrapper.getInputs();
         for (int i = 0; i < 9; i++)
            itemStacks.setFromRecipe(i, inputs.get(i));

         itemStacks.setFromRecipe(9, recipeWrapper.getOutputs().get(0));
      }
   }
}
