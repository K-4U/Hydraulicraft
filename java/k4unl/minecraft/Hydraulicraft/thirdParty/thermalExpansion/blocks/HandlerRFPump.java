package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks;

import k4unl.minecraft.Hydraulicraft.blocks.handlers.HydraulicTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class HandlerRFPump extends HydraulicTieredBlockHandler {
	
	public HandlerRFPump(Block block) {
		super(block, Names.blockRFPump);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return ThermalExpansion.blockRFPump.getIcon(0, metadata);
	}
}
