package k4unl.minecraft.Hydraulicraft.thirdParty.rf;

import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.client.models.Models;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.blocks.BlockHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.blocks.BlockRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.blocks.HandlerRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.client.renderers.RendererHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.client.renderers.RendererRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.TileRFPump;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RF implements IThirdParty {

    public static Block blockHydraulicDynamo;
    public static Block blockRFPump;

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

        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRFPump.class, new RendererRFPump());

        //Models.loadBlockModel(blockHydraulicDynamo);
        for (int i = 0; i < 3; i++) {
            Models.loadBlockModel(blockRFPump, i, ((HydraulicTieredBlockBase)blockRFPump).getUnlocalizedName(i));
        }
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockHydraulicDynamo), new RendererHydraulicDynamoItem());
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockRFPump), new RendererRFPumpItem());
    }

    public static void initBlocks() {

        blockHydraulicDynamo = new BlockHydraulicDynamo();
        blockRFPump = new BlockRFPump();

        //GameRegistry.registerBlock(blockHydraulicDynamo, HandlerHydraulicBlock.class, Names.blockHydraulicDynamo.unlocalized);
        GameRegistry.registerBlock(blockRFPump, HandlerRFPump.class, Names.blockRFPump[0].unlocalized);

        //GameRegistry.registerTileEntity(TileHydraulicDynamo.class, "tileHydraulicDynamo");
        GameRegistry.registerTileEntity(TileRFPump.class, "tileRFPump");
    }

    public static void initRecipes() {

        ItemStack powerTransmissionCoil = new ItemStack(GameRegistry.findItem("ThermalExpansion", "powerCoilSilver"), 1);
        if (powerTransmissionCoil.getItem() == null) {
            powerTransmissionCoil = new ItemStack(Items.REDSTONE);
        }

        HydraulicRecipes.INSTANCE.addAssemblerRecipe(new FluidShapedOreRecipe(new ItemStack(blockHydraulicDynamo, 1), true,
                new Object[]{
                        "-C-",
                        "FIK",
                        "IRI",
                        'F', HCItems.itemFrictionPlate,
                        'C', powerTransmissionCoil,
                        'K', HCItems.gasket,
                        'I', "ingotCopper",
                        'R', Items.REDSTONE
                }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 500))
        );

        ItemStack powerReceptionCoil = new ItemStack(GameRegistry.findItem("ThermalExpansion", "powerCoilGold"), 1);
        if (powerReceptionCoil.getItem() == null) {
            powerReceptionCoil = new ItemStack(Items.REDSTONE);
        }
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 0), true,
                new Object[]{
                        "L-L",
                        "KGC",
                        "WWW",
                        'G', Blocks.GLASS,
                        'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', powerReceptionCoil,
                        'L', "ingotLead"
                }));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 1), true,
                new Object[]{
                        "R-R",
                        "KGC",
                        "WWW",
                        'G', Blocks.GLASS,
                        'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', powerReceptionCoil,
                        'R', "ingotCopper"
                }));

        HydraulicRecipes.INSTANCE.addAssemblerRecipe(new FluidShapedOreRecipe(new ItemStack(blockRFPump, 1, 2), true,
                new Object[]{
                        "R-R",
                        "KGC",
                        "WWW",
                        'G', Blocks.GLASS,
                        'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', powerReceptionCoil,
                        'R', "ingotEnrichedCopper"
                }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 500))
        );

    }
}
