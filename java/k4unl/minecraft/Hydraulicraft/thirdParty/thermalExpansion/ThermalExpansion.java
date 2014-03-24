package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.HandlerRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ThermalExpansion {
	public static Block blockHydraulicDynamo;
	public static Block blockRFPump;
	
	public static void init(){
		initBlocks();
	}
	
	public static void postInit(){
		initRecipes();
	}
	
	public static void initBlocks(){
		blockHydraulicDynamo = new BlockHydraulicDynamo();
		blockRFPump = new BlockRFPump();
		
		GameRegistry.registerBlock(blockHydraulicDynamo, Names.blockHydraulicDynamo.unlocalized);
		GameRegistry.registerBlock(blockRFPump, HandlerRFPump.class, Names.blockRFPump[0].unlocalized);
		
		GameRegistry.registerTileEntity(TileHydraulicDynamo.class, "tileHydraulicDynamo");
		GameRegistry.registerTileEntity(TileRFPump.class, "tileRFPump");
	}
	
	public static void initRecipes(){
		ItemStack powerTransmissionCoil = GameRegistry.findItemStack("ThermalExpansion", "powerCoilSilver", 1);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydraulicDynamo, 1), true,
				new Object [] {
					"-C-",
					"FIK",
					"IRI",
					'F', HydraulicraftItems.itemFrictionPlate,
					'C', powerTransmissionCoil,
					'K', HydraulicraftItems.gasket,
					'I', "ingotCopper",
					'R', Items.redstone
				}));
		
		ItemStack powerReceptionCoil = GameRegistry.findItemStack("ThermalExpansion", "powerCoilGold", 1);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'L', "ingotLead"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 1), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'R', "ingotCopper"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 2), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'R', "ingotEnrichedCopper"
				}));
		
	}
}
