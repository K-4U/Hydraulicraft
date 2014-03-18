package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerHarvesterTrolley extends MachineTieredBlockHandler {
	
	public HandlerHarvesterTrolley(int id, Block block) {
		super(id, Names.blockHarvesterTrolley);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.harvesterTrolley.getIcon(0, metadata);
	}
}
