package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerKineticPump extends MachineTieredBlockHandler {
	
	public HandlerKineticPump(int blockId, Block block) {
		super(blockId, Names.blockKineticPump);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Buildcraft.blockKineticPump.getIcon(0, metadata);
	}
}
