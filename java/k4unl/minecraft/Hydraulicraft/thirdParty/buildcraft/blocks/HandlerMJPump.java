package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerMJPump extends MachineTieredBlockHandler {
	
	public HandlerMJPump(int blockId, Block block) {
		super(blockId, Names.blockMJPump);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Buildcraft.blockMJPump.getIcon(0, metadata);
	}
}
