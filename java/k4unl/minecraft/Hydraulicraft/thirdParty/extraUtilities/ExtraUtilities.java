package k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ExtraUtilities implements IThirdParty {

    public static Block enderLily;
    public static Block enderCore;

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

        initBlocks();
        initRecipes();
    }

    @Override
    public void clientSide() {

    }

    public static void initBlocks() {

        enderLily = GameRegistry.findBlock("ExtraUtilities", "plant/ender_lilly");
        enderCore = GameRegistry.findBlock("ExtraUtilities", "decorativeBlock1");
        Hydraulicraft.trolleyRegistrar.registerTrolley(new TrolleyEnderlily());
    }

    public static void initRecipes() {

        //TODO: Think of a better recipe. Drop the ender pearl.
        ItemStack enderTrolly = Hydraulicraft.trolleyRegistrar.getTrolleyItem("enderLily");
        enderTrolly.stackSize = 2;

        HydraulicRecipes.INSTANCE.addAssemblerRecipe(new FluidShapedOreRecipe(enderTrolly, true,
                new Object[]{
                        "-P-",
                        "WCW",
                        "-H-",
                        'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                        'W', HCBlocks.hydraulicPressureWall,
                        'H', Items.ENDER_EYE,
                        'P', HCBlocks.hydraulicPiston
                }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 20))
        );

    }
}
