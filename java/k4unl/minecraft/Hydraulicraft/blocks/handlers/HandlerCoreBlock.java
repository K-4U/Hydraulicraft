package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerCoreBlock extends MachineTieredBlockHandler {
	public HandlerCoreBlock(int blockId, Block block) {
		super(blockId, Names.blockCore);
	}

	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicPump.getIcon(0, metadata);
	}
}
