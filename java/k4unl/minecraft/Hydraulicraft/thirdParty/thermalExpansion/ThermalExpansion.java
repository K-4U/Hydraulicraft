package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion;

import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.HandlerRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamoItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.block.Block;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ThermalExpansion {
	public static Block blockHydraulicDynamo;
	public static Block blockRFPump;
	
	public static void init(){
		initBlocks();
		initRecipes();
	}
	
	public static void initBlocks(){
		blockHydraulicDynamo = new BlockHydraulicDynamo();
		blockRFPump = new BlockRFPump();
		
		GameRegistry.registerBlock(blockHydraulicDynamo, Names.blockHydraulicDynamo.unlocalized);
		GameRegistry.registerBlock(blockRFPump, HandlerRFPump.class, Names.blockRFPump[0].unlocalized);
		
		
		GameRegistry.registerTileEntity(TileHydraulicDynamo.class, "tileHydraulicDynamo");
		GameRegistry.registerTileEntity(TileRFPump.class, "tileRFPump");
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRFPump.class, new RendererRFPump());
		
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicDynamo.act, new RendererHydraulicDynamoItem());
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
