package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
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
    private float            pressure;

    public JEIWrapperIFluidRecipe(IFluidRecipe recipe) {

        this.inputs = JEIPlugin.makeList(recipe.getInputItems());
        this.outputs = new ArrayList<ItemStack>();
        this.outputs.add(recipe.getRecipeOutput());
        this.fluidInputs = recipe.getInputFluids();
        this.fluidOutputs = recipe.getOutputFluids();
        this.pressure = recipe.getPressure();
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
        int color = Constants.COLOR_PRESSURE;
        if((mouseX >= 152-6 && mouseX <= 152+16-6) && (mouseY >= 16-14 && mouseY <= 16+54-14)){
            color = Constants.COLOR_PRESSURE | 0x6F000000;
        }
        drawVerticalProgressBar(152-6, 16-14, 54, 16,
                this.pressure,
                this.pressure,
                color);
    }

    public void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color) {

        float perc = value / max;
        int height = (int) (h * perc);
        Gui.drawRect(xOffset, yOffset + (h - height), xOffset + w, yOffset + h, color);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
        // TODO
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if((mouseX >= 152-6 && mouseX <= 152+16-6) && (mouseY >= 16-14 && mouseY <= 16+54-14)){
            List<String> tooltips = new ArrayList<>();
            tooltips.add("Pressure");
            if(((int)pressure / 1000) == 0){
                tooltips.add(EnumChatFormatting.GRAY + "" + (int)pressure + " mBar");
            }else {
                tooltips.add(EnumChatFormatting.GRAY + "" +((int) pressure) / 1000 + " Bar");
            }
            return tooltips;
        }
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

    public static class JEIWrapperRecipeCrusher extends JEIWrapperIFluidRecipe {

        public JEIWrapperRecipeCrusher(IFluidRecipe recipe) {

            super(recipe);
        }
    }

    public static class JEIWrapperRecipeWasher extends JEIWrapperIFluidRecipe {

        public JEIWrapperRecipeWasher(IFluidRecipe recipe) {

            super(recipe);
        }
    }

    public static class JEIWrapperRecipeRecombobulator extends JEIWrapperIFluidRecipe {

        public JEIWrapperRecipeRecombobulator(IFluidRecipe recipe) {

            super(recipe);
        }
    }
}
