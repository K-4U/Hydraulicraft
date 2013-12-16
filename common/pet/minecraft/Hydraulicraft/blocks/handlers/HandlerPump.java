package pet.minecraft.Hydraulicraft.blocks.handlers;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import pet.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import pet.minecraft.Hydraulicraft.blocks.Blocks;
import pet.minecraft.Hydraulicraft.lib.config.Names;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class HandlerPump extends MachineTieredBlockHandler {
	
	public HandlerPump(int blockId, Block block) {
		super(blockId, Names.blockHydraulicPump);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicPump.getIcon(0, metadata);
	}
}
