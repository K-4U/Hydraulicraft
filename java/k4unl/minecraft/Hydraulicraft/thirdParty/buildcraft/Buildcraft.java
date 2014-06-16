package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.BlockHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.BlockKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.HandlerKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererHydraulicEngineItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.client.renderers.RendererKineticPumpItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Buildcraft implements IThirdParty{
	public static Block blockHydraulicEngine;
	public static Block blockKineticPump;

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
        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicEngine.class, new RendererHydraulicEngine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileKineticPump.class, new RendererKineticPump());
        
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockHydraulicEngine), new RendererHydraulicEngineItem());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockKineticPump), new RendererKineticPumpItem());
    }
	
	public static void initBlocks(){
		blockHydraulicEngine = new BlockHydraulicEngine();
		blockKineticPump = new BlockKineticPump();
		
		
		GameRegistry.registerBlock(blockHydraulicEngine, ItemBlock.class, Names.blockHydraulicEngine.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockKineticPump, HandlerKineticPump.class, Names.blockKineticPump[0].unlocalized, ModInfo.ID);
		
		GameRegistry.registerTileEntity(TileHydraulicEngine.class, "tileHydraulicEngine");
		GameRegistry.registerTileEntity(TileKineticPump.class, "tileKineticPump");
		
	}
	
	public static void initRecipes(){
		GameRegistry.addRecipe(new ItemStack(Buildcraft.blockHydraulicEngine, 1),
				new Object [] {
					"WWW",
					"-G-",
					"FPK",
					'W', HCBlocks.hydraulicPressureWall,
					'G', Blocks.glass,
					'F', HCItems.itemFrictionPlate,
					'P', Blocks.piston,
					'K', HCItems.gasket
				});
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGP",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'P', Blocks.piston,
					'L', "ingotLead"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 1), true,
				new Object [] {
					"R-R",
					"KGP",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'P', Blocks.piston,
					'R', "ingotCopper"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 2), true,
				new Object [] {
					"R-R",
					"KGP",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'P', Blocks.piston,
					'R', "ingotEnrichedCopper"
				}));
		
		
		
	}
}
