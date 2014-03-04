package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft;

import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks.BlockElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks.BlockHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks.HandlerElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererElectricPumpItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.renderers.RendererHydraulicGeneratorItem;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import net.minecraft.block.Block;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class IndustrialCraft {
	public static Block blockHydraulicGenerator;
	public static Block blockElectricPump;
	
	public static void init(){
		initBlocks();
		initRecipes();
	}
	
	public static void initBlocks(){
		blockHydraulicGenerator = new BlockHydraulicGenerator();
		blockElectricPump = new BlockElectricPump();
		
		
		GameRegistry.registerBlock(blockHydraulicGenerator, Names.blockHydraulicGenerator.unlocalized);
		GameRegistry.registerBlock(blockElectricPump, HandlerElectricPump.class, Names.blockElectricPump[0].unlocalized);
		
		GameRegistry.registerTileEntity(TileHydraulicGenerator.class, "tileHydraulicGenerator");
		GameRegistry.registerTileEntity(TileElectricPump.class, "tileElectricPump");
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicGenerator.class, new RendererHydraulicGenerator());
		ClientRegistry.bindTileEntitySpecialRenderer(TileElectricPump.class, new RendererElectricPump());
		
		MinecraftForgeClient.registerItemRenderer(Ids.blockHydraulicGenerator.act, new RendererHydraulicGeneratorItem());
		MinecraftForgeClient.registerItemRenderer(Ids.blockElectricPump.act, new RendererElectricPumpItem());
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
