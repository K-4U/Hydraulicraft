package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHydraulicBlock;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks.BlockHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Pneumaticraft implements IThirdParty {

    public static Block hydraulicPneumaticCompressor;

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

        initBlocks();
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

        hydraulicPneumaticCompressor = new BlockHydraulicPneumaticCompressor();
        GameRegistry.registerBlock(hydraulicPneumaticCompressor, HandlerHydraulicBlock.class, Names.blockHydraulicPneumaticCompressor.unlocalized);
        //LanguageRegistry.addName(hydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor.localized);

        GameRegistry.registerTileEntity(TileHydraulicPneumaticCompressor.class, "tileHydraulicPneumaticCompressor");
    }

    public static void initRecipes() {

        GameRegistry.addRecipe(new ItemStack(hydraulicPneumaticCompressor, 1),
                new Object[]{
                        "WWW",
                        "KCT",
                        "WWW",
                        'K', HCItems.gasket,
                        'T', new ItemStack(Items.redstone/*BlockSupplier.getBlock("pressureTube")*/, 1, 0),
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', Items.redstone //BlockSupplier.getBlock("airCompressor")
                });
    }
}
