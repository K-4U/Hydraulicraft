package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft;

import ic2.api.item.IC2Items;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IndustrialCraft implements IThirdParty{
	public static Block blockHydraulicGenerator;
	public static Block blockElectricPump;
	
	@Override
    public String getModId(){
        return "IC2";
    }

    @Override
    public void preInit(){
        initBlocks();
        initRecipes();
    }

    @Override
    public void init(){}

    @Override
    public void postInit(){}

    @SideOnly(Side.CLIENT)
    @Override
    public void clientSide(){
        ClientRegistry.bindTileEntitySpecialRenderer(TileHydraulicGenerator.class, new RendererHydraulicGenerator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricPump.class, new RendererElectricPump());
        
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockHydraulicGenerator), new RendererHydraulicGeneratorItem());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(blockElectricPump), new RendererElectricPumpItem());
    }

	public static void initBlocks(){
		blockHydraulicGenerator = new BlockHydraulicGenerator();
		blockElectricPump = new BlockElectricPump();
		
		
		GameRegistry.registerBlock(blockHydraulicGenerator, ItemBlock.class, Names.blockHydraulicGenerator.unlocalized, ModInfo.ID);
		GameRegistry.registerBlock(blockElectricPump, HandlerElectricPump.class, Names.blockElectricPump[0].unlocalized);
		
		GameRegistry.registerTileEntity(TileHydraulicGenerator.class, "tileHydraulicGenerator");
		GameRegistry.registerTileEntity(TileElectricPump.class, "tileElectricPump");
		
		
	}
	
	public static void initRecipes(){
		ItemStack generator = IC2Items.getItem("generator");
		ItemStack battery = IC2Items.getItem("reBattery");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockHydraulicGenerator, 1), true,
				new Object [] {
					"PBP",
					"PGP",
					"WKW",
					'P', Blocks.glass_pane,
					'G', generator,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'B', battery
				}));
		
		ItemStack coil = IC2Items.getItem("coil");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectricPump, 1, 0), true,
				new Object [] {
					"L-L",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', coil,
					'L', "ingotLead"
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectricPump, 1, 1), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', coil,
					'R', "ingotCopper"
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectricPump, 1, 2), true,
				new Object [] {
					"R-R",
					"KGC",
					"WWW",
					'G', Blocks.glass,
					'K', k4unl.minecraft.Hydraulicraft.items.HCItems.gasket,
					'W', HCBlocks.hydraulicPressureWall,
					'C', coil,
					'R', "ingotEnrichedCopper"
				}));
	}

}
