package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.BlockHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.BlockKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.HandlerKineticPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.tileEntities.TileKineticPump;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
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
		
	}
	
	public static void initRecipes(){
		GameRegistry.addRecipe(new ItemStack(Buildcraft.blockHydraulicEngine, 1),
				new Object [] {
					"WWW",
					"-G-",
					"FPK",
					'W', HCBlocks.hydraulicPressureWall,
					'G', Blocks.glass,
					'F', HydraulicraftItems.itemFrictionPlate,
					'P', Blocks.piston,
					'K', HydraulicraftItems.gasket
				});
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockKineticPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGP",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems.gasket,
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
					'K', k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems.gasket,
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
					'K', k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'P', Blocks.piston,
					'R', "ingotEnrichedCopper"
				}));
		
		
		
	}

	public static void initRenderers() {
		
	}
}
