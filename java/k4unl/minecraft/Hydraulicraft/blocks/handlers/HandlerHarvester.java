package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

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
		return Names.blockHydraulicHarvester[itemStack.getItemDamage()].unlocalized;
	}

}
