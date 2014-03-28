package k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.blocks;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class HandlerKineticPump extends MachineTieredBlockHandler {
	
	public HandlerKineticPump(Block block) {
		super(block, Names.blockKineticPump);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return Buildcraft.blockKineticPump.getIcon(0, metadata);
	}
}
