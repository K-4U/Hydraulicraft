package k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCactus;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class ExtraUtilities implements IThirdParty{
	public static Block enderLily;
	
	@Override
    public String getModId(){
        return "ExtraUtilities";
    }

    @Override
    public void preInit(){}

    @Override
    public void init(){}

    @Override
    public void postInit(){
        initBlocks();
        initRecipes();
    }

    @Override
    public void clientSide(){}
	
	public static void initBlocks(){
		enderLily = GameRegistry.findBlock("ExtraUtilities", "plant/ender_lilly");
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleyEnderlily());
	}
	
	public static void initRecipes(){
		
		//TODO: Think of a better recipe. Drop the ender pearl.
		/*
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.harvesterTrolley, 2, Constants.HARVESTER_ID_ENDERLILY), true ,
				new Object[] {
					"-P-",
					"WCW",
					"-H-",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'H', Items.ender_pearl,
					'P', HCBlocks.hydraulicPiston
			})
		);*/
		
	}
}
