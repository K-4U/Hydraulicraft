package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks.BlockHydraulicEngine;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.block.Block;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ThermalExpansion {
	public static Block blockHydraulicDynamo;
	
	public static void init(){
		initBlocks();
		initRecipes();
	}
	
	public static void initBlocks(){
		blockHydraulicDynamo = new BlockHydraulicDynamo();
		GameRegistry.registerBlock(blockHydraulicDynamo, Names.blockHydraulicDynamo.unlocalized);
		LanguageRegistry.addName(blockHydraulicDynamo, Names.blockHydraulicDynamo.localized);
		
		GameRegistry.registerTileEntity(TileHydraulicDynamo.class, "tileHydraulicDynamo");
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
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
