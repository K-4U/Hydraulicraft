package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
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


        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.blockValve, 4, 0), true,
            new Object[] {
              "WKW",
              "K K",
              "WKW",
              'W', HCBlocks.hydraulicPressureWall,
              'K', HCItems.gasket
            }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 500)).setPressure(2.0F).setCraftingTime(200)
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterSource, 1, 0), true,
            new Object[] {
              "WWW",
              "ICK",
              "WWW",
              'C', new ItemStack(HCBlocks.blockCore, 1, 1),
              'W', HCBlocks.hydraulicPressureWall,
              'K', HCItems.gasket,
              'I', HCBlocks.blockInterfaceValve
            }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 100))
        );

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

    public static IFluidRecipe getWasherRecipe(IFluidInventory inventory) {
        return getRecipe(recipesWasher, inventory);
    }

    public static IFluidRecipe getFilterRecipe(IFluidInventory inventory) {
        return getRecipe(recipesFilter, inventory);
    }

    public static List<IFluidRecipe> getCrusherRecipes(){
        return recipesCrusher;
    }

    public static List<IFluidRecipe> getIncineratorRecipes(){
        return recipesIncinerator;
    }

    public static List<IFluidRecipe> getAssemblerRecipes(){
        return recipesAssembler;
    }

    public static List<IFluidRecipe> getWasherRecipes(){
        return recipesWasher;
    }

    public static List<IFluidRecipe> getFilterRecipes(){
        return recipesFilter;
    }

}
