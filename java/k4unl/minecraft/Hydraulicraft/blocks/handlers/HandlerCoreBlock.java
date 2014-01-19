package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class HandlerCoreBlock extends MachineTieredBlockHandler {
	public HandlerCoreBlock(int blockId, Block block) {
		super(blockId, Names.blockCore);
	}

	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicPump.getIcon(0, metadata);
	}
}
