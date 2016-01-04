package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft;

import k4unl.minecraft.Hydraulicraft.api.recipes.FluidShapedOreRecipe;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHydraulicBlock;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks.BlockElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks.BlockHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks.HandlerElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class IndustrialCraft implements IThirdParty{
	public static Block blockHydraulicGenerator;
	public static Block blockElectricPump;

    @Override
    public void preInit(){
        initBlocks();
        initRecipes();
    }

    @Override
    public void init(){}

    @Override
    public void postInit(){}

    @SideOnly(Side.CLIENT)
    @Override
    public void clientSide(){
        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicGenerator.class, new RendererHydraulicGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricPump.class, new RendererElectricPump());
	}

	public static void initBlocks(){
		blockHydraulicGenerator = new BlockHydraulicGenerator();
		blockElectricPump = new BlockElectricPump();
		
		
		GameRegistry.registerBlock(blockHydraulicGenerator, HandlerHydraulicBlock.class, Names.blockHydraulicGenerator.unlocalized);
		GameRegistry.registerBlock(blockElectricPump, HandlerElectricPump.class, Names.blockElectricPump[0].unlocalized);
		
		GameRegistry.registerTileEntity(TileHydraulicGenerator.class, "tileHydraulicGenerator");
		GameRegistry.registerTileEntity(TileElectricPump.class, "tileElectricPump");
	}
	
	public static void initRecipes(){
		ItemStack generator = null;//IC2Items.getItem("generator");
		ItemStack battery = null;//IC2Items.getItem("reBattery");

		HydraulicRecipes.INSTANCE.addAssemblerRecipe(new FluidShapedOreRecipe(new ItemStack(blockHydraulicGenerator, 1), true,
            new Object[] {
              "PBP",
              "PGP",
              "WKW",
              'P', Blocks.glass_pane,
              'G', generator,
              'K', HCItems.gasket,
              'W', HCBlocks.hydraulicPressureWall,
              'B', battery
            }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 200))
        );
		
		ItemStack coil = null;//IC2Items.getItem("coil");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectricPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', coil,
					'L', "ingotLead"
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectricPump, 1, 1), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', coil,
					'R', "ingotCopper"
				}));

		HydraulicRecipes.INSTANCE.addAssemblerRecipe(new FluidShapedOreRecipe(new ItemStack(blockElectricPump, 1, 2), true,
          new Object[] {
            "R-R",
            "KGC",
            "WWW",
            'G', Blocks.glass,
            'K', HCItems.gasket,
            'W', HCBlocks.hydraulicPressureWall,
            'C', coil,
            'R', "ingotEnrichedCopper"
          }).addFluidInput(new FluidStack(Fluids.fluidLubricant, 500))
        );
	}

}
