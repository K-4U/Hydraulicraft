package k4unl.minecraft.Hydraulicraft.thirdParty.bluepower;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class BluePower implements IThirdParty{
	public static Block flaxBlock;
	public static Item flaxItem;

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
		flaxBlock = GameRegistry.findBlock("bluepower", "flax_crop");
		flaxItem = GameRegistry.findItem("bluepower", "flax_seeds");
		Hydraulicraft.harvesterTrolleyRegistrar.registerTrolley(new TrolleyFlax());
	}
	
	public static void initRecipes(){
		Item sickle = GameRegistry.findItem("bluepower", "gold_sickle");
		ItemStack flaxTrolly = Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem("flax");
		flaxTrolly.stackSize = 2;
		
		GameRegistry.addRecipe(new ShapedOreRecipe(flaxTrolly, true ,
				new Object[] {
					"-P-",
					"WCW",
					"-H-",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'H', new ItemStack(sickle),
					'P', HCBlocks.hydraulicPiston
			})
		);
		
	}
}
