package k4unl.minecraft.Hydraulicraft.lib;


import net.minecraft.item.ItemStack;

import java.util.LinkedList;

@Deprecated
public class WashingRecipes {

    public static LinkedList<WashingRecipe> washingRecipes = new
            LinkedList<WashingRecipe>();

    @Deprecated
    public static void addWashingRecipe(WashingRecipe toAdd) {
        washingRecipes.add(toAdd);
    }

    @Deprecated
    public static ItemStack getWashingRecipeOutput(ItemStack itemStack) {
        for (WashingRecipe rec : washingRecipes) {
            if (rec.input.isItemEqual(itemStack)) {
                return rec.output.copy();
            }
        }
        return null;
    }

    @Deprecated
    public static WashingRecipe getWashingRecipe(ItemStack itemStack) {
        for (WashingRecipe rec : washingRecipes) {
            if (rec.input.isItemEqual(itemStack)) {
                return rec;
            }
        }
        return null;
    }

    @Deprecated
    public static class WashingRecipe {
        public final ItemStack input;
        public final ItemStack output;
        public final float     pressure;

        public WashingRecipe(ItemStack inp, float press, ItemStack outp) {
            input = inp;
            output = outp;
            pressure = press;
        }

        public ItemStack getOutput() {
            return output.copy();
        }
    }


}
