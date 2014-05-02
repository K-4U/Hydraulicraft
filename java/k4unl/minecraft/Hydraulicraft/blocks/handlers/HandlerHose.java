package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class HandlerHose extends HydraulicTieredBlockHandler {

	public HandlerHose(Block block) {
		super(block, Names.partHose);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return HCBlocks.blockHose.getIcon(0, metadata);
	}
}
