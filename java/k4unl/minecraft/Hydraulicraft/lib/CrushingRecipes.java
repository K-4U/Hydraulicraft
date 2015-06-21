package k4unl.minecraft.Hydraulicraft.lib;


import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.LinkedList;

@Deprecated
public class CrushingRecipes {

    public static LinkedList<CrushingRecipe> crushingRecipes = new
            LinkedList<CrushingRecipe>();

    @Deprecated
    public static void addCrushingRecipe(CrushingRecipe toAdd) {
        crushingRecipes.add(toAdd);
    }

    @Deprecated
    public static ItemStack getCrushingRecipeOutput(ItemStack itemStack) {
        //TODO: CHANGE ME
        String oreDictName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        for (CrushingRecipe rec : crushingRecipes) {
            if (rec.input != null) {
                if (rec.input.isItemEqual(itemStack)) {
                    return rec.output.copy();
                }
            }
        }
        return getCrushingRecipeOutput(oreDictName);
    }

    @Deprecated
    public static CrushingRecipe getCrushingRecipe(ItemStack itemStack) {
        //TODO: Change me
        String oreDictName = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));
        for (CrushingRecipe rec : crushingRecipes) {
            if (rec.input != null) {
                if (rec.input.isItemEqual(itemStack)) {
                    return rec;
                }
            }
        }
        return getCrushingRecipe(oreDictName);
    }

    @Deprecated
    public static CrushingRecipe getCrushingRecipe(String oreDictName) {
        for (CrushingRecipe rec : crushingRecipes) {
            if (rec.inputString.equals(oreDictName)) {
                return rec;
            }
        }
        return null;
    }

    @Deprecated
    public static ItemStack getCrushingRecipeOutput(String oreDictName) {
        for (CrushingRecipe rec : crushingRecipes) {
            if (rec.inputString.equals(oreDictName)) {
                return rec.output.copy();
            }
        }
        return null;
    }

    public static class CrushingRecipe {
        public final String    inputString;
        public final ItemStack input;
        public final ItemStack output;
        public final float     pressureRatio;

        public CrushingRecipe(String inp, float press, ItemStack outp) {
            inputString = inp;
            output = outp;
            pressureRatio = press;
            input = null;
        }

        public CrushingRecipe(ItemStack inp, float press, ItemStack outp) {
            inputString = "";
            output = outp;
            pressureRatio = press;
            input = inp;
        }

        public ItemStack getOutput() {
            return output.copy();
        }
    }

}
