package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerRFPump extends MachineTieredBlockHandler {
	
	public HandlerRFPump(int blockId, Block block) {
		super(blockId, Names.blockRFPump);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return ThermalExpansion.blockRFPump.getIcon(0, metadata);
	}
}
