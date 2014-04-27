package k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ExtraUtilities implements IThirdParty{
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
		int enderLilyBlockId = 0;
		/*
		for(int i=0; i < Block.blocksList.length; i++){
			Block current = Block.blocksList[i];
			if(current != null){
				if("tile.extrautils:plant/ender_lilly".equals(current.getUnlocalizedName())){
					Log.info("Found ender lily!");
					enderLilyBlockId = current.blockID;
				}
			}
		}
		
		if(enderLilyBlockId != 0){
			Config.addHarvestableItem(new Seed(Constants.HARVESTER_ID_ENDERLILY, enderLilyBlockId, 7));
			
			((BlockHarvesterTrolley)HydraulicraftBlocks.harvesterTrolley).enableHarvester(Constants.HARVESTER_ID_ENDERLILY);
		}
		*/
	}
	
	public static void initRecipes(){
		/*
		//TODO: Think of a better recipe. Drop the ender pearl.
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HydraulicraftBlocks.harvesterTrolley, 2, Constants.HARVESTER_ID_ENDERLILY), true ,
				new Object[] {
					"-P-",
					"WCW",
					"-H-",
					'C', new ItemStack(HydraulicraftBlocks.blockCore, 1, 1),
					'W', HydraulicraftBlocks.hydraulicPressureWall,
					'H', Item.enderPearl,
					'P', HydraulicraftBlocks.hydraulicPiston
			})
		);
		*/
	}
}
