package k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities;

import cpw.mods.fml.common.registry.GameRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ExtraUtilities {
	
	
	public static void init(){
		initBlocks();
		initRecipes();
	}
	
	public static void initBlocks(){
		int enderLilyBlockId = 0;
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
			
			((BlockHarvesterTrolley)Blocks.harvesterTrolley).enableHarvester(Constants.HARVESTER_ID_ENDERLILY);
		}
	}
	
	public static void initRecipes(){
		//TODO: Think of a better recipe. Drop the ender pearl.
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.harvesterTrolley, 2, Constants.HARVESTER_ID_ENDERLILY), true ,
				new Object[] {
					"-P-",
					"WCW",
					"-H-",
					'C', new ItemStack(Blocks.blockCore, 1, 1),
					'W', Blocks.hydraulicPressureWall,
					'H', Item.enderPearl,
					'P', Blocks.hydraulicPiston
			})
		);
	}

	public static void initRenderers() {
		
	}
}
