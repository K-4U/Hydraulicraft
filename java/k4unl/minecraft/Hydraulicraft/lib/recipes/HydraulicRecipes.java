package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IRecipeHandler;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class HydraulicRecipes implements IRecipeHandler {
    public static HydraulicRecipes INSTANCE = new HydraulicRecipes();

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

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterSource, 1, 0), true,
                        new Object[]{
                                "WWW",
                                "ICK",
                                "WWW",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket,
                                'I', HCBlocks.blockInterfaceValve
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 100))
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.blockCore, 1, 2), true,
                        new Object[]{
                                "EWE",
                                "WBW",
                                "EWE",
                                'W', HCBlocks.hydraulicPressureWall,
                                'E', "ingotEnrichedCopper",
                                'B', new ItemStack(HCBlocks.blockCore, 1, 1)
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 750)).setCraftingTime(400)
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPump, 1, 2), true,
                        new Object[]{
                                "PKP",
                                "GCG",
                                "PUP",
                                'P', Blocks.piston,
                                'K', HCItems.gasket,
                                'G', Blocks.glass,
                                'U', new ItemStack(HCBlocks.hydraulicPump, 1, 1),
                                'C', new ItemStack(HCBlocks.blockCore, 1, 2)
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 250))
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 1, 2), true,
                        new Object[]{
                                "C-K",
                                "-H-",
                                "K-C",
                                'K', HCItems.gasket,
                                'C', "ingotEnrichedCopper",
                                'H', new ItemStack(Multipart.itemPartHose, 1, 1)
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 10)).setCraftingTime(100)
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 2), true,
                        new Object[]{
                                "WCW",
                                "KBK",
                                "WVW",
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket,
                                'C', "ingotEnrichedCopper",
                                'V', new ItemStack(HCBlocks.hydraulicPressurevat, 1, 1),
                                'B', new ItemStack(HCBlocks.blockCore, 1, 2)
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 1000))
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCItems.itemMovingPane, 2), true,
                        new Object[]{
                                "GGG",
                                "GCG",
                                "WKW",
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 2),
                                'K', HCItems.gasket,
                                'G', Blocks.glass_pane
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 10))
        );

        ItemStack cropsTrolly = Hydraulicraft.trolleyRegistrar.getTrolleyItem("crops");
        cropsTrolly.stackSize = 4;

        recipesAssembler.add(new FluidShapedOreRecipe(cropsTrolly, true,
                        new Object[]{
                                " P ",
                                "WCW",
                                " H ",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'W', HCBlocks.hydraulicPressureWall,
                                'H', Items.golden_hoe,
                                'P', HCBlocks.hydraulicPiston
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 40))
        );

        ItemStack sugarTrolly = Hydraulicraft.trolleyRegistrar.getTrolleyItem("sugarCane");
        sugarTrolly.stackSize = 4;

        recipesAssembler.add(new FluidShapedOreRecipe(sugarTrolly, true,
                        new Object[]{
                                " P ",
                                "WCW",
                                " S ",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'W', HCBlocks.hydraulicPressureWall,
                                'S', Items.shears,
                                'P', HCBlocks.hydraulicPiston
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 40))
        );

        ItemStack cactusTrolly = Hydraulicraft.trolleyRegistrar.getTrolleyItem("cactus");
        cactusTrolly.stackSize = 4;

        recipesAssembler.add(new FluidShapedOreRecipe(cactusTrolly, true,
                        new Object[]{
                                " P ",
                                "WCW",
                                " S ",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'W', HCBlocks.hydraulicPressureWall,
                                'S', Items.golden_sword,
                                'P', HCBlocks.hydraulicPiston
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 40))
        );

        ItemStack netherWartTrolly = Hydraulicraft.trolleyRegistrar.getTrolleyItem("netherWart");
        netherWartTrolly.stackSize = 2;

        recipesAssembler.add(new FluidShapedOreRecipe(netherWartTrolly, true,
                        new Object[]{
                                " P ",
                                "WCW",
                                " S ",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'W', HCBlocks.hydraulicPressureWall,
                                'S', Items.ghast_tear,
                                'P', HCBlocks.hydraulicPiston
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 40))
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicLavaPump, 1, 2), true,
                        new Object[]{
                                "GKG",
                                "ICG",
                                "PUP",
                                'P', Blocks.piston,
                                'K', HCItems.gasket,
                                'G', Blocks.glass,
                                'I', HCBlocks.blockInterfaceValve,
                                'U', new ItemStack(HCBlocks.hydraulicLavaPump, 1, 1),
                                'C', new ItemStack(HCBlocks.blockCore, 1, 2)
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 100))
        );

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPiston, 1, 0), true,
                        new Object[]{
                                "III",
                                "WCW",
                                "WKW",
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'K', HCItems.gasket,
                                'R', Items.redstone,
                                'I', "ingotIron"
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 300))
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

    public static List<IFluidRecipe> getCrusherRecipes() {
        return recipesCrusher;
    }

    public static List<IFluidRecipe> getIncineratorRecipes() {
        return recipesIncinerator;
    }

    public static List<IFluidRecipe> getAssemblerRecipes() {
        return recipesAssembler;
    }

    public static List<IFluidRecipe> getWasherRecipes() {
        return recipesWasher;
    }

    public static List<IFluidRecipe> getFilterRecipes() {
        return recipesFilter;
    }

    public void addCrushingRecipe(IFluidRecipe toAdd) {
        recipesCrusher.add(toAdd);
    }

    public void addAssemblerRecipe(IFluidRecipe toAdd) {
        recipesAssembler.add(toAdd);
    }

    public void addWasherRecipe(IFluidRecipe toAdd) {
        recipesWasher.add(toAdd);
    }

    public void addFilterRecipe(IFluidRecipe toAdd) {
        recipesFilter.add(toAdd);
    }

}
