package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion;

import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.items.Items;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.BlockRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks.HandlerRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererHydraulicDynamoItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererRFPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.renderers.RendererRFPumpItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
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
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRFPump.class, new RendererRFPump());
		
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicDynamo.act, new RendererHydraulicDynamoItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockRFPump.act, new RendererRFPumpItem());
	}
	
	public static void initRecipes(){
		ItemStack powerTransmissionCoil = GameRegistry.findItemStack("ThermalExpansion", "powerCoilSilver", 1);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydraulicDynamo, 1), true,
				new Object [] {
					"-C-",
					"FIK",
					"IRI",
					'F', Items.itemFrictionPlate,
					'C', powerTransmissionCoil,
					'K', Items.gasket,
					'I', "ingotCopper",
					'R', Item.redstone
				}));
		
		ItemStack powerReceptionCoil = GameRegistry.findItemStack("ThermalExpansion", "powerCoilGold", 1);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGC",
					"WWW",
					'G', Block.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.Items.gasket,
					'W', Blocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'L', "ingotLead"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 1), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Block.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.Items.gasket,
					'W', Blocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'R', "ingotCopper"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 2), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Block.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.Items.gasket,
					'W', Blocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'R', "ingotEnrichedCopper"
				}));
		
	}
}
