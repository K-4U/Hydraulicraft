package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.handlers.HandlerHydraulicBlock;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ThermalExpansion implements IThirdParty {
	public static Block blockHydraulicDynamo;
	public static Block blockRFPump;

    @Override
    public void preInit(){
        initBlocks();
    }

    @Override
    public void init(){}

    @Override
    public void postInit(){
        initRecipes();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void clientSide(){
    	ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicDynamo.class, new RendererHydraulicDynamo());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRFPump.class, new RendererRFPump());
        
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockHydraulicDynamo), new RendererHydraulicDynamoItem());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockRFPump), new RendererRFPumpItem());
    }
	
	public static void initBlocks(){
		blockHydraulicDynamo = new BlockHydraulicDynamo();
		blockRFPump = new BlockRFPump();
		
		GameRegistry.registerBlock(blockHydraulicDynamo, HandlerHydraulicBlock.class, Names.blockHydraulicDynamo.unlocalized);
		GameRegistry.registerBlock(blockRFPump, HandlerRFPump.class, Names.blockRFPump[0].unlocalized);
		
		GameRegistry.registerTileEntity(TileHydraulicDynamo.class, "tileHydraulicDynamo");
		GameRegistry.registerTileEntity(TileRFPump.class, "tileRFPump");
	}
	
	public static void initRecipes(){
		ItemStack powerTransmissionCoil = GameRegistry.findItemStack("ThermalExpansion", "powerCoilSilver", 1);
		if(powerTransmissionCoil == null){
			powerTransmissionCoil = new ItemStack(Items.redstone);
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydraulicDynamo, 1), true,
				new Object [] {
					"-C-",
					"FIK",
					"IRI",
					'F', HCItems.itemFrictionPlate,
					'C', powerTransmissionCoil,
					'K', HCItems.gasket,
					'I', "ingotCopper",
					'R', Items.redstone
				}));
		
		ItemStack powerReceptionCoil = GameRegistry.findItemStack("ThermalExpansion", "powerCoilGold", 1);
		if(powerReceptionCoil == null){
			powerReceptionCoil = new ItemStack(Items.redstone);
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockRFPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
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
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
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
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', powerReceptionCoil,
					'R', "ingotEnrichedCopper"
				}));
		
	}
}
