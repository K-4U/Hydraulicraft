package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class HandlerHarvester extends MachineTieredBlockHandler {

	public HandlerHarvester(int id, Block block) {
		super(id, Names.blockHydraulicHarvester);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicHarvesterSource.getIcon(0, metadata);
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
