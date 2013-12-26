package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerPressureVat extends MachineTieredBlockHandler {

	public HandlerPressureVat(int blockId, Block block) {
		super(blockId, Names.blockHydraulicPressurevat);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicPressurevat.getIcon(0, metadata);
	}

}
