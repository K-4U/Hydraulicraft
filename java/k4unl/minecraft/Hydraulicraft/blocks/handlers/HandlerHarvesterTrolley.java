package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

public class HandlerHarvesterTrolley extends MachineTieredBlockHandler {
	
	public HandlerHarvesterTrolley(int id, Block block) {
		super(id, Names.blockHarvesterTrolley);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.harvesterTrolley.getIcon(0, metadata);
	}
}
