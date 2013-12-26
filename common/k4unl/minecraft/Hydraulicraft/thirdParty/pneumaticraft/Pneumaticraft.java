package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks.BlockHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Pneumaticraft {
	public static Block hydraulicPneumaticCompressor;
	
	public static void init(){
		
		initBlocks();
	}
	
	public static void initBlocks(){
		hydraulicPneumaticCompressor = new BlockHydraulicPneumaticCompressor();
		GameRegistry.registerBlock(hydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor.unlocalized);
		LanguageRegistry.addName(hydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor.localized);
		
		GameRegistry.registerTileEntity(TileHydraulicPneumaticCompressor.class, "tileHydraulicPneumaticCompressor");
	}
}
