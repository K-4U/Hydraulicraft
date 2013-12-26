package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerPressureHoze extends MachineTieredBlockHandler {

	public HandlerPressureHoze(int id, Block block) {
		super(id, Names.blockHydraulicHose);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return Blocks.hydraulicHose.getIcon(0, metadata);
	}

}
