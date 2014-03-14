package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.items.Items;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
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
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class Buildcraft {
	public static Block blockHydraulicEngine;
	public static Block blockKineticPump;
	
	public static void init(){
		initBlocks();
		initRecipes();
	}
	
	public static void initBlocks(){
		blockHydraulicEngine = new BlockHydraulicEngine();
		blockKineticPump = new BlockKineticPump();
		
		
		GameRegistry.registerBlock(blockHydraulicEngine, Names.blockHydraulicEngine.unlocalized);
		GameRegistry.registerBlock(blockKineticPump, HandlerKineticPump.class, Names.blockKineticPump[0].unlocalized);
		
		GameRegistry.registerTileEntity(TileHydraulicEngine.class, "tileHydraulicEngine");
		GameRegistry.registerTileEntity(TileKineticPump.class, "tileKineticPump");
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicEngine.class, new RendererHydraulicEngine());
		ClientRegistry.bindTileEntitySpecialRenderer(TileKineticPump.class, new RendererKineticPump());
		
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicEngine.act, new RendererHydraulicEngineItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockKineticPump.act, new RendererKineticPumpItem());
	}
	
	public static void initRecipes(){
		GameRegistry.addRecipe(new ItemStack(Buildcraft.blockHydraulicEngine, 1),
				new Object [] {
					"WWW",
					"-G-",
					"FPK",
					'W', Blocks.hydraulicPressureWall,
					'G', Block.glass,
					'F', Items.itemFrictionPlate,
					'P', Block.pistonBase,
					'K', Items.gasket
				});
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGP",
					"WWW",
					'G', Block.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.Items.gasket,
					'W', Blocks.hydraulicPressureWall,
					'P', Block.pistonBase,
					'L', "ingotLead"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 1), true,
				new Object [] {
					"R-R",
					"KGP",
					"WWW",
					'G', Block.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.Items.gasket,
					'W', Blocks.hydraulicPressureWall,
					'P', Block.pistonBase,
					'R', "ingotCopper"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 2), true,
				new Object [] {
					"R-R",
					"KGP",
					"WWW",
					'G', Block.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.Items.gasket,
					'W', Blocks.hydraulicPressureWall,
					'P', Block.pistonBase,
					'R', "ingotEnrichedCopper"
				}));
		
		
		
	}
}
