package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class HandlerHarvesterTrolley extends HydraulicTieredBlockHandler {
	
	public HandlerHarvesterTrolley(Block block) {
		super(block, Names.blockHarvesterTrolley);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return HCBlocks.harvesterTrolley.getIcon(0, metadata);
	}
}
