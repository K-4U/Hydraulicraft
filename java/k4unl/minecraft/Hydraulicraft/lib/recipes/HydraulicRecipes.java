package k4unl.minecraft.Hydraulicraft.lib.recipes;

import java.util.ArrayList;
import java.util.List;

public class HydraulicRecipes {
    private static List<HydraulicUnshapedRecipe> recipesCrusher;
    private static List<HydraulicUnshapedRecipe> recipesIncinerator;
    private static List<HydraulicUnshapedRecipe> recipesAssembler;
    private static List<HydraulicUnshapedRecipe> recipesCharger;
    private static List<HydraulicUnshapedRecipe> recipesPiston;
    private static List<HydraulicUnshapedRecipe> recipesWasher;

    static {
        recipesCrusher = new ArrayList<HydraulicUnshapedRecipe>();
        recipesIncinerator = new ArrayList<HydraulicUnshapedRecipe>();
        recipesAssembler = new ArrayList<HydraulicUnshapedRecipe>();
        recipesCharger = new ArrayList<HydraulicUnshapedRecipe>();
        recipesPiston = new ArrayList<HydraulicUnshapedRecipe>();
        recipesWasher = new ArrayList<HydraulicUnshapedRecipe>();
    }

    public void addCrusherRecipe(Object[] input, Object[] output) {
        recipesCrusher.add(new HydraulicUnshapedRecipe(input, output));
    }

    public void addIncineratorRecipe(Object[] input, Object[] output) {
        recipesIncinerator.add(new HydraulicUnshapedRecipe(input, output));
    }

    public void addAssemblerRecipe(Object[] input, Object[] output) {
        recipesCharger.add(new HydraulicUnshapedRecipe(input, output));
    }

    private HydraulicUnshapedRecipe getRecipe(List<HydraulicUnshapedRecipe> list, Object[] inputs) {
        for (HydraulicUnshapedRecipe recipe : list) {
            if (recipe.matches(inputs))
                return recipe;
        }

        return null;
    }

    public HydraulicUnshapedRecipe getCrusherRecipe(Object[] input) {
        return getRecipe(recipesCrusher, input);
    }

    public HydraulicUnshapedRecipe getIncineratorRecipe(Object[] input) {
        return getRecipe(recipesIncinerator, input);
    }

    public HydraulicUnshapedRecipe getAssemblerRecipe(Object[] input) {
        return getRecipe(recipesAssembler, input);
    }
}
