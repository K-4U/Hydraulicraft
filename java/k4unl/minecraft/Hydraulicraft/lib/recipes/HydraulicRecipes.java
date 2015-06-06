package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HydraulicRecipes {
    private static List<IFluidRecipe> recipesCrusher;
    private static List<IFluidRecipe> recipesIncinerator;
    private static List<IFluidRecipe> recipesAssembler;
    private static List<IFluidRecipe> recipesWasher;

    static {
        recipesCrusher = new ArrayList<IFluidRecipe>();
        recipesIncinerator = new ArrayList<IFluidRecipe>();
        recipesAssembler = new ArrayList<IFluidRecipe>();
        recipesWasher = new ArrayList<IFluidRecipe>();

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(Blocks.dirt), "d  ", " d ", "  d", 'd', Blocks.stone));
        recipesAssembler.add(new FluidShapelessOreRecipe(new ItemStack(Blocks.coal_block), Items.coal));
    }

    private static IFluidRecipe getRecipe(List<IFluidRecipe> list, IFluidInventory inventory) {
        for (IFluidRecipe recipe : list) {
            if (recipe.matches(inventory))
                return recipe;
        }

        return null;
    }

    public static IFluidRecipe getCrusherRecipe(IFluidInventory inventory) {
        return getRecipe(recipesCrusher, inventory);
    }

    public static IFluidRecipe getIncineratorRecipe(IFluidInventory inventory) {
        return getRecipe(recipesIncinerator, inventory);
    }

    public static IFluidRecipe getAssemblerRecipe(IFluidInventory inventory) {
        return getRecipe(recipesAssembler, inventory);
    }

    public static IFluidRecipe getWasherRecipes(IFluidInventory inventory) {
        return getRecipe(recipesWasher, inventory);
    }
}
