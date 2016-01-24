package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class JEIWrapperAssembler implements IRecipeWrapper {
   private List             inputs;
   private List<ItemStack>  outputs;
   private List<FluidStack> fluidInputs;
   private List<FluidStack> fluidOutputs;

   public JEIWrapperAssembler(List inputs, ItemStack output, List<FluidStack> fluidInputs, List<FluidStack> fluidOutputs) {
      this.inputs = inputs;
      this.outputs = new ArrayList<ItemStack>();
      this.outputs.add(output);
      this.fluidInputs = fluidInputs;
      this.fluidOutputs = fluidOutputs;
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
}
