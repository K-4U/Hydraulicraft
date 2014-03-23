package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicraftBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class HandlerCoreBlock extends MachineTieredBlockHandler {
	public HandlerCoreBlock(Block block) {
		super(block, Names.blockCore);
	}

	@Override
	public IIcon getIconFromDamage(int metadata) {
		return HydraulicraftBlocks.hydraulicPump.getIcon(0, metadata);
	}
}
