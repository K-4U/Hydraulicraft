package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicraftBlocks;
import k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks.BlockHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.block.BlockSupplier;
import cpw.mods.fml.common.registry.GameRegistry;

public class Pneumaticraft {
	public static Block hydraulicPneumaticCompressor;
	
	public static void init(){
		
		initBlocks();
		initRecipes();
	}
	
	public static void initBlocks(){
		hydraulicPneumaticCompressor = new BlockHydraulicPneumaticCompressor();
		GameRegistry.registerBlock(hydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor.unlocalized);
		//LanguageRegistry.addName(hydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor.localized);
		
		GameRegistry.registerTileEntity(TileHydraulicPneumaticCompressor.class, "tileHydraulicPneumaticCompressor");
	}
	
	public static void initRecipes(){
		GameRegistry.addRecipe(new ItemStack(hydraulicPneumaticCompressor, 1),
				new Object [] {
					"WWW",
					"KCT",
					"WWW",
					'K', HydraulicraftItems.gasket,
					'T', new ItemStack(BlockSupplier.getBlock("pressureTube"), 1, 0),
					'W', HydraulicraftBlocks.hydraulicPressureWall,
					'C', BlockSupplier.getBlock("airCompressor")
				});
	}

	public static void initRenderers() {
		// 
	}
}
