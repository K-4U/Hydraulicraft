package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class HydraulicRecipes {
    private static List<IFluidRecipe> recipesCrusher;
    private static List<IFluidRecipe> recipesIncinerator;
    private static List<IFluidRecipe> recipesAssembler;
    private static List<IFluidRecipe> recipesWasher;
    private static List<IFluidRecipe> recipesFilter;

    static {
        recipesCrusher = new ArrayList<IFluidRecipe>();
        recipesIncinerator = new ArrayList<IFluidRecipe>();
        recipesAssembler = new ArrayList<IFluidRecipe>();
        recipesWasher = new ArrayList<IFluidRecipe>();
        recipesFilter = new ArrayList<IFluidRecipe>();


        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(Blocks.dirt), "d  ", " d ", "  d", 'd', Blocks.stone)
                .setCraftingTime(20).addFluidInput(new FluidStack(Fluids.fluidOil, 1000)).setPressure(0.1f));
        recipesAssembler.add(new FluidShapelessOreRecipe(new ItemStack(Blocks.coal_block), Items.coal).addFluidInput(
                new FluidStack(Fluids.fluidOil, 1000)).setCraftingTime(20).setPressure(0.1f));

        recipesFilter.add(new FluidShapelessOreRecipe(Item.getItemFromBlock(HCBlocks.blockDirtyMineral), Item.getItemFromBlock(Ores.oreBeachium)).addFluidInput(new
          FluidStack(Fluids.fluidOil, 10)).addFluidOutput(new FluidStack(Fluids.fluidHydraulicOil, 9)).setPressure(5.0F));

        recipesFilter.add(new FluidShapelessOreRecipe(Item.getItemFromBlock(HCBlocks.blockDirtyMineral), Item.getItemFromBlock(HCBlocks.blockRefinedNadsiumBicarbinate))
          .addFluidInput(new FluidStack(Fluids.fluidOil, 10)).addFluidOutput(new FluidStack(Fluids.fluidLubricant, 5)).setPressure(10.0F));

        recipesFilter.add(new FluidShapelessOreRecipe(Item.getItemFromBlock(HCBlocks.blockDirtyMineral), Item.getItemFromBlock(HCBlocks
          .blockRefinedLonezium)).addFluidInput(new FluidStack(Fluids.fluidOil, 20)).addFluidOutput(new FluidStack(Fluids.fluidFluoroCarbonFluid, 5))
          .setPressure(50.0F));
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

    public static IFluidRecipe getFilterRecipes(IFluidInventory inventory) {
        return getRecipe(recipesFilter, inventory);
    }
}
