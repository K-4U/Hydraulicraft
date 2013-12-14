package pet.minecraft.Hydraulicraft.blocks.handlers;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import pet.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import pet.minecraft.Hydraulicraft.blocks.Blocks;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class HandlerPressureHoze extends MachineTieredBlockHandler {

	public HandlerPressureHoze(int id, Block block) {
		super(id, Names.blockHydraulicHose);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicHose.getIcon(0, metadata);
	}

}
