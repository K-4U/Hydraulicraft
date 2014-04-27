package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks.BlockHydraulicPneumaticCompressor;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import pneumaticCraft.api.block.BlockSupplier;
import cpw.mods.fml.common.registry.GameRegistry;

public class Pneumaticraft implements IThirdParty {
	public static Block hydraulicPneumaticCompressor;
	
	@Override
    public String getModId(){
        return "PneumaticCraft";
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

    @Override
    public void clientSide(){}
	
	public static void initBlocks(){
		hydraulicPneumaticCompressor = new BlockHydraulicPneumaticCompressor();
		GameRegistry.registerBlock(hydraulicPneumaticCompressor, ItemBlock.class, Names.blockHydraulicPneumaticCompressor.unlocalized, ModInfo.ID);
		//LanguageRegistry.addName(hydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor.localized);
		
		GameRegistry.registerTileEntity(TileHydraulicPneumaticCompressor.class, "tileHydraulicPneumaticCompressor");
	}
	
	public static void initRecipes(){
		GameRegistry.addRecipe(new ItemStack(hydraulicPneumaticCompressor, 1),
				new Object [] {
					"WWW",
					"KCT",
					"WWW",
					'K', HCItems.gasket,
					'T', new ItemStack(BlockSupplier.getBlock("pressureTube"), 1, 0),
					'W', HCBlocks.hydraulicPressureWall,
					'C', BlockSupplier.getBlock("airCompressor")
				});
	}
}
