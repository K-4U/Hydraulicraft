package pet.minecraft.Hydraulicraft.blocks.handlers;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import pet.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import pet.minecraft.Hydraulicraft.blocks.Blocks;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class HandlerPressureVat extends MachineTieredBlockHandler {

	public HandlerPressureVat(int blockId, Block block) {
		super(blockId, Names.blockHydraulicPressurevat);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicPressurevat.getIcon(0, metadata);
	}

}
