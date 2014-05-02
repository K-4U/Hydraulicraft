package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class HandlerHarvester extends HydraulicTieredBlockHandler {

	public HandlerHarvester(Block block) {
		super(block, Names.blockHydraulicHarvester);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return HCBlocks.hydraulicHarvesterSource.getIcon(0, metadata);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		if(itemStack.getItemDamage() >= Names.blockHydraulicHarvester.length){
			return "DERP";
		}
		String unlocalizedName = Names.blockHydraulicHarvester[itemStack.getItemDamage()].unlocalized;
		if(!unlocalizedName.startsWith("tile.")){
			unlocalizedName = "tile." + unlocalizedName;
		}
		
		return unlocalizedName;
	}

}
