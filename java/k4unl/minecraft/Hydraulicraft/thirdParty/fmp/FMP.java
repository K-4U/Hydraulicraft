package k4unl.minecraft.Hydraulicraft.thirdParty.fmp;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.blocks.BlockHydraulicSaw;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities.TileHydraulicSaw;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;


public class FMP {
	public static Block hydraulicSaw;
	
	
	public static void init(){
		initBlocks();
	}
	
	public static void initBlocks(){
		hydraulicSaw = new BlockHydraulicSaw();
		
		GameRegistry.registerBlock(hydraulicSaw, ItemBlock.class, Names.blockHydraulicSaw.unlocalized, ModInfo.ID);
		GameRegistry.registerTileEntity(TileHydraulicSaw.class, "tileHydraulicSaw");
		
		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", new ItemStack(HCBlocks.hydraulicPressureWall, 1));
	}
	
	public static void initRecipes(){
		//FMP
		ItemStack saw = GameRegistry.findItemStack("ForgeMicroblock","sawIron", 1);
		GameRegistry.addRecipe(new ShapedOreRecipe(hydraulicSaw, true,
				new Object [] {
					"GSG",
					"KCI",
					"WWW",
					'G', Blocks.glass,
					'K', HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', new ItemStack(HCBlocks.blockCore,1,1),
					'I', HCBlocks.blockInterfaceValve,
					'S', saw
				}));
		
	}
}
