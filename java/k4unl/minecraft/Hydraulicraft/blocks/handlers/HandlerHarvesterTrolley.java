package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicraftBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class HandlerHarvesterTrolley extends MachineTieredBlockHandler {
	
	public HandlerHarvesterTrolley(Block block) {
		super(block, Names.blockHarvesterTrolley);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return HydraulicraftBlocks.harvesterTrolley.getIcon(0, metadata);
	}
}
