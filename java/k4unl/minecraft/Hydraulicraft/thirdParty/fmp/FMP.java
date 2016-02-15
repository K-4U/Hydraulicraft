package k4unl.minecraft.Hydraulicraft.thirdParty.fmp;

import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHydraulicBlock;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.blocks.BlockHydraulicSaw;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities.TileHydraulicSaw;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class FMP implements IThirdParty {

    public static Block hydraulicSaw;

    @Override
    public void preInit() {

        initBlocks();
    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

        initRecipes();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void clientSide() {

    }

    public static void initBlocks() {

        hydraulicSaw = new BlockHydraulicSaw();

        GameRegistry.registerBlock(hydraulicSaw, HandlerHydraulicBlock.class, Names.blockHydraulicSaw.unlocalized);
        GameRegistry.registerTileEntity(TileHydraulicSaw.class, "tileHydraulicSaw");

        FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(HCBlocks.hydraulicPressureWall, 1));
        FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(HCBlocks.hydraulicPressureGlass, 1));
    }

    public static void initRecipes() {
        //FMP
        Item saw = GameRegistry.findItem("ForgeMicroblock", "sawIron");
        HydraulicRecipes.INSTANCE.addAssemblerRecipe(new FluidShapedOreRecipe(hydraulicSaw, true,
                new Object[]{
                        "GSG",
                        "KCI",
                        "WWW",
                        'G', Blocks.glass,
                        'K', HCItems.gasket,
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 1),
                        'I', HCBlocks.blockInterfaceValve,
                        'S', saw
                }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 200))
        );

    }

}
