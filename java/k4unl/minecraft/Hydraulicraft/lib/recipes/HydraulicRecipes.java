package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapelessOreRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.api.recipes.IRecipeHandler;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.multipart.MultipartHandler;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
    }


    public static void init() {
        initializeBlockRecipes();
        initializeItemRecipes();
        initializeSmeltingRecipes();
        initializeCrushingRecipes();
        initializeAssemblerRecipes();
        initializeFilterRecipes();
        initializeWashingRecipes();
    }

    private static void initializeWashingRecipes() {
        recipesWasher.add(new FluidShapelessOreRecipe(HCBlocks.blockRefinedLonezium, Ores.oreLonezium).setCraftingTime(5*20));
        recipesWasher.add(new FluidShapelessOreRecipe(HCBlocks.blockRefinedNadsiumBicarbinate, Ores.oreNadsiumBicarbinate).setCraftingTime(20*20));
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

    private static void initializeFilterRecipes() {
        recipesFilter.add(new FluidShapelessOreRecipe(Item.getItemFromBlock(HCBlocks.blockDirtyMineral), Item.getItemFromBlock(Ores.oreBeachium)).addFluidInput(new
                FluidStack(Fluids.fluidOil, 5000)).addFluidOutput(new FluidStack(Fluids.fluidHydraulicOil, 4500)).setPressure(5.0F).setCraftingTime(500));

        recipesFilter.add(new FluidShapelessOreRecipe(Item.getItemFromBlock(HCBlocks.blockDirtyMineral), Item.getItemFromBlock(HCBlocks.blockRefinedNadsiumBicarbinate))
                .addFluidInput(new FluidStack(Fluids.fluidOil, 5000)).addFluidOutput(new FluidStack(Fluids.fluidLubricant, 2500)).setPressure(10.0F).setCraftingTime(500));

        recipesFilter.add(new FluidShapelessOreRecipe(Item.getItemFromBlock(HCBlocks.blockDirtyMineral), Item.getItemFromBlock(HCBlocks
                .blockRefinedLonezium)).addFluidInput(new FluidStack(Fluids.fluidOil, 10000)).addFluidOutput(new FluidStack(Fluids.fluidFluoroCarbonFluid, 2500))
                .setPressure(50.0F).setCraftingTime(500));
    }

    private static void initializeAssemblerRecipes() {
        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterSource, 1, 0), true,
                        new Object[]{
                                "WWW",
                                "ICK",
                                "WWW",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket,
                                'I', HCBlocks.blockInterfaceValve
                        }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 100)).setCraftingTime(1000)
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

        recipesAssembler.add(new FluidShapedOreRecipe(new ItemStack(MultipartHandler.itemPartHose, 1, 2), true,
                        new Object[]{
                                "C-K",
                                "-H-",
                                "K-C",
                                'K', HCItems.gasket,
                                'C', "ingotEnrichedCopper",
                                'H', new ItemStack(MultipartHandler.itemPartHose, 1, 1)
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
    }

    private static void initializeCrushingRecipes() {
        //Yeah, just put them in, right there, yeahhhhhh
        OreDictionary.registerOre("oreIron", Blocks.iron_ore);
        OreDictionary.registerOre("ingotIron", Items.iron_ingot);
        OreDictionary.registerOre("oreGold", Blocks.gold_ore);
        OreDictionary.registerOre("ingotGold", Items.gold_ingot);
        OreDictionary.registerOre("oreNetherQuartz", Blocks.quartz_ore);
        OreDictionary.registerOre("gemDiamond", Items.diamond);
        //Get items from ore dictionary:

        for (String item : Hydraulicraft.crushableItems) {
            String oreName = "ore" + item;
            List<ItemStack> oreStack = OreDictionary.getOres(oreName);

            String ingotName = "ingot" + item;
            List<ItemStack> ingotStack = OreDictionary.getOres(ingotName);

            //Log.info("Found " + oreStack.size() + " ores and " + ingotStack.size() + " ingots for " + item);

            if (oreStack.size() > 0 && ingotStack.size() > 0) {
                int metaId = HCItems.itemChunk.showChunk(item);
                HCItems.itemDust.showDust(item);

                HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(HCItems.itemChunk, 2, metaId), oreName)
                        .setPressure(1.0F).setCraftingTime(200));

                HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(HCItems.itemDust, 1, metaId), ingotName)
                        .setPressure(1.0F).setCraftingTime(200));

                HydraulicRecipes.INSTANCE.addWasherRecipe(new FluidShapelessOreRecipe(new ItemStack(HCItems.itemDust, 1, metaId), new ItemStack
                        (HCItems.itemChunk, 1, metaId)).addFluidInput(new FluidStack(FluidRegistry.WATER, 1000)).setCraftingTime(200));
            }
        }

        //Other mods. Stuff that doesn't follow the ingot stuff.
        if (Loader.isModLoaded("IC2")) {
            registerNonStandardCrushRecipe("oreUranium", "crushedUranium");
        }

        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(Items.quartz, 3), Blocks.quartz_ore).setPressure(1.0F));
        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(Blocks.sand, 2), Blocks.cobblestone).setPressure(0.9F));
        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(Items.dye, 5, 15), Items.bone).setPressure(0.5F));
        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(HCItems.itemDiamondShards, 9), Items.diamond).setPressure(1.2F));
        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(Items.blaze_powder, 5), Items.blaze_rod).setPressure(0.5F));
    }

    private static void registerNonStandardCrushRecipe(String sourceName, String targetName) {
        List<ItemStack> oreStackL = OreDictionary.getOres(sourceName);
        List<ItemStack> targetStackL = OreDictionary.getOres(targetName);
        if (oreStackL.size() == 0 || targetStackL.size() == 0)
            return;

        ItemStack targetStack = targetStackL.get(0);

        HydraulicRecipes.INSTANCE.addCrushingRecipe(new FluidShapelessOreRecipe(targetStack, sourceName).setPressure(1.1F));
    }

    private static void initializeSmeltingRecipes() {
        GameRegistry.addSmelting(Ores.oreCopper, new ItemStack(HCItems.ingotCopper), 0);
        GameRegistry.addSmelting(Ores.oreLead, new ItemStack(HCItems.ingotLead), 0);
    }

    private static void initializeBlockRecipes() {

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockValve, 4, 0), true,
                        new Object[]{
                                "WKW",
                                "K K",
                                "WKW",
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket
                        })
        );


        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockInterfaceValve, 4, 0), true,
                        new Object[]{
                                "WHW",
                                "K K",
                                "WHW",
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket,
                                'H', Blocks.hopper
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterFrame, 18), true,
                        new Object[]{
                                "SSS",
                                "-S-",
                                "SSS",
                                'S', Items.stick
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCore, 2, 0), true,
                        new Object[]{
                                "LSL",
                                "SWS",
                                "LSL",
                                'S', Blocks.stone,
                                'W', HCBlocks.hydraulicPressureWall,
                                'L', "ingotLead"
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCore, 1, 1), true,
                        new Object[]{
                                "CWC",
                                "WBW",
                                "CWC",
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', "ingotCopper",
                                'B', new ItemStack(HCBlocks.blockCore, 1, 0)
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressureWall, 8), true,
                new Object[]{
                        "SSS",
                        "SLS",
                        "SSS",
                        'S', Blocks.stone,
                        'L', "ingotLead"
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPump, 1, 0), true,
                        new Object[]{
                                "PKP",
                                "GCG",
                                "PWP",
                                'P', Blocks.piston,
                                'K', HCItems.gasket,
                                'G', "blockGlass",
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 0)
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPump, 1, 1), true,
                        new Object[]{
                                "PKP",
                                "GCG",
                                "PUP",
                                'P', Blocks.piston,
                                'K', HCItems.gasket,
                                'G', "blockGlass",
                                'U', new ItemStack(HCBlocks.hydraulicPump, 1, 0),
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1)
                        })
        );


        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicFilter, 1), true,
                        new Object[]{
                                "GKG",
                                "KCK",
                                "WIW",
                                'K', HCItems.gasket,
                                'G', "blockGlass",
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'I', HCBlocks.blockInterfaceValve
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MultipartHandler.itemPartHose, 4, 0), true,
                        new Object[]{
                                "LLL",
                                "K-K",
                                "LLL",
                                'K', HCItems.gasket,
                                'L', "ingotLead"
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MultipartHandler.itemPartHose, 1, 1), true,
                new Object[]{
                        "C-C",
                        "KHK",
                        "C-C",
                        'K', HCItems.gasket,
                        'C', "ingotCopper",
                        'H', new ItemStack(MultipartHandler.itemPartHose, 1, 0)
                }));


        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 0), true,
                        new Object[]{
                                "LWL",
                                "KCK",
                                "LWL",
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket,
                                'L', "ingotLead",
                                'C', new ItemStack(HCBlocks.blockCore, 1, 0)
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 1), true,
                        new Object[]{
                                "CWC",
                                "KBK",
                                "CVC",
                                'W', HCBlocks.hydraulicPressureWall,
                                'K', HCItems.gasket,
                                'C', "ingotCopper",
                                'V', new ItemStack(HCBlocks.hydraulicPressurevat, 1, 0),
                                'B', new ItemStack(HCBlocks.blockCore, 1, 1)
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicFrictionIncinerator, 1), true,
                        new Object[]{
                                "GKG",
                                "FCF",
                                "WIW",
                                'W', HCBlocks.hydraulicPressureWall,
                                'G', "blockGlass",
                                'F', HCItems.itemFrictionPlate,
                                'K', HCItems.gasket,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'I', HCBlocks.blockInterfaceValve

                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicCrusher, 1), true,
                        new Object[]{
                                "-K-",
                                "PCP",
                                "WIW",
                                'K', HCItems.gasket,
                                'P', Blocks.piston,
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                                'I', HCBlocks.blockInterfaceValve
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicWasher, 1), true,
                        new Object[]{
                                "GKG",
                                "KCG",
                                "WWW",
                                'K', HCItems.gasket,
                                'G', "blockGlass",
                                'W', HCBlocks.hydraulicPressureWall,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1)
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicLavaPump, 1, 0), true,
                        new Object[]{
                                "GKG",
                                "ICG",
                                "PWP",
                                'P', Blocks.piston,
                                'K', HCItems.gasket,
                                'G', "blockGlass",
                                'W', HCBlocks.hydraulicPressureWall,
                                'I', HCBlocks.blockInterfaceValve,
                                'C', new ItemStack(HCBlocks.blockCore, 1, 0)
                        })
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicLavaPump, 1, 1), true,
                        new Object[]{
                                "GKG",
                                "ICG",
                                "PUP",
                                'P', Blocks.piston,
                                'K', HCItems.gasket,
                                'G', "blockGlass",
                                'I', HCBlocks.blockInterfaceValve,
                                'U', new ItemStack(HCBlocks.hydraulicLavaPump, 1, 0),
                                'C', new ItemStack(HCBlocks.blockCore, 1, 1)
                        })
        );

/*        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MultipartHandler.itemPartValve, 2, 0), true,
                new Object[]{
                        "   ",
                        "HLH",
                        "   ",
                        'H', new ItemStack(MultipartHandler.itemPartHose, 1, 0),
                        'L', Blocks.lever
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MultipartHandler.itemPartValve, 2, 1), true,
                new Object[]{
                        "   ",
                        "HLH",
                        "   ",
                        'H', new ItemStack(MultipartHandler.itemPartHose, 1, 1),
                        'L', Blocks.lever
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MultipartHandler.itemPartValve, 2, 2), true,
                new Object[]{
                        "   ",
                        "HLH",
                        "   ",
                        'H', new ItemStack(MultipartHandler.itemPartHose, 1, 2),
                        'L', Blocks.lever
                }));
*/
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockHydraulicFluidPump, 1), true,
                new Object[]{
                        " P ",
                        "GCG",
                        "WWW",
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 0),
                        'P', Blocks.piston,
                        'G', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressureGlass, 8), true,
                new Object[]{
                        "GWG",
                        "GLG",
                        "GWG",
                        'W', HCBlocks.hydraulicPressureWall,
                        'L', "ingotLead",
                        'G', "blockGlass"
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCopper, 1), true,
                new Object[]{
                        "CCC",
                        "CCC",
                        "CCC",
                        'C', "ingotCopper"
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockLead, 1), true,
                new Object[]{
                        "LLL",
                        "LLL",
                        "LLL",
                        'L', "ingotLead"
                }));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.ingotCopper, 9), "blockCopper"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.ingotLead, 9), "blockLead"));


        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.portalBase, 1), true,
                new Object[]{
                        "III",
                        "ECE",
                        "IGI",
                        'I', Items.iron_ingot,
                        'E', Items.ender_pearl,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 2),
                        'G', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.portalFrame, 6), true,
                new Object[]{
                        "III",
                        "IEI",
                        "III",
                        'I', Items.iron_ingot,
                        'E', Items.ender_pearl
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockAssembler, 1), true,
                new Object[]{
                        "WGW",
                        "STI",
                        "WCW",
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                        'G', "blockGlass",
                        'T', Blocks.crafting_table,
                        'I', HCBlocks.blockInterfaceValve,
                        'S', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockJarDirt, 1), true,
                new Object[]{
                        "-S-",
                        "GDG",
                        "GGG",
                        'G', "blockGlass",
                        'D', Blocks.dirt,
                        'S', "slabWood"
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCharger, 1), true,
                new Object[] {
                        "WAW",
                        "GCI",
                        "WWW",
                        'W', HCBlocks.hydraulicPressureWall,
                        'A', HCItems.itemCannister,
                        'G', HCItems.gasket,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                        'I', HCBlocks.blockInterfaceValve
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicFiller, 1), true,
                new Object[] {
                        "GSG",
                        "SCI",
                        "WBW",
                        'G', "blockGlass",
                        'S', HCItems.gasket,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                        'I', HCBlocks.blockInterfaceValve,
                        'W', HCBlocks.hydraulicPressureWall,
                        'B', Items.bucket
                }));


    }

    private static void initializeItemRecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemFrictionPlate, 1),
                "-SS",
                "S-S",
                "SS-",
                'S', Blocks.stone
        ));


        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.itemEnrichedCopperDust, 2), "diamondShard", "dustCopper"));


        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.gasket, 4),
                "P P",
                " B ",
                "P P",
                'P', Items.paper,
                'B', Blocks.iron_bars

        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemLamp, 1), true,
                new Object[]{
                        " G ",
                        "GTG",
                        " G ",
                        'G', Blocks.glass,
                        'T', Blocks.torch
                }));

        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.itemMiningHelmet, 1), HCItems.itemLamp, Items.iron_helmet));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemIPCard, 1), true,
                new Object[]{
                        "ROO",
                        "RWI",
                        "RII",
                        'I', Items.iron_ingot,
                        'R', Items.redstone,
                        'W', new ItemStack(Blocks.wool, 1, 15),
                        'O', new ItemStack(Blocks.wool, 1, 14)
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemEnderLolly, 4), true,
                new Object[]{
                        "E",
                        "S",
                        'E', Items.ender_pearl,
                        'S', Items.stick
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemCannister, 1), true,
                new Object[]{
                        "LHL",
                        "L L",
                        "LLL",
                        'L', "ingotLead",
                        'H', MultipartHandler.itemPartHose
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemhydraulicWrench, 1), true,
                new Object[]{
                        "C C",
                        " C ",
                        " A ",
                        'C', "ingotCopper",
                        'A', HCItems.itemCannister
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemDivingHelmet, 1), true,
                new Object[]{
                        "LLL",
                        "LPL",
                        "LGL",
                        'L', "ingotLead",
                        'P', "paneGlass",
                        'G', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemDivingController, 1), true,
                new Object[] {
                        "L L",
                        "AGA",
                        "GLG",
                        'L', "ingotLead",
                        'A', HCItems.itemCannister,
                        'G', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemDivingLegs, 1), true,
                new Object[] {
                        "GLG",
                        "L L",
                        "G G",
                        'L', "ingotLead",
                        'G', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemDivingBoots, 1), true,
                new Object[] {
                        "G G",
                        "L L",
                        'L', "ingotLead",
                        'G', HCItems.gasket
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemPressureGauge, 1), true,
                new Object[] {
                        "LLL",
                        "GCW",
                        "LLL",
                        'C', "ingotCopper",
                        'L', "ingotLead",
                        'G', "paneGlass",
                        'W', Items.redstone
                }));
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
