package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft;

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
import net.minecraftforge.client.MinecraftForgeClient;
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
		/*GameRegistry.addRecipe(new ItemStack(hydraulicPneumaticCompressor, 1),
				new Object [] {
					"WWW",
					"KCT",
					"WWW",
					'K', Items.gasket,
					'T', new ItemStack(BlockSupplier.getBlock("pressureTube"), 1, 0),
					'W', Blocks.hydraulicPressureWall,
					'C', BlockSupplier.getBlock("airCompressor")
				});*/
	}
}
