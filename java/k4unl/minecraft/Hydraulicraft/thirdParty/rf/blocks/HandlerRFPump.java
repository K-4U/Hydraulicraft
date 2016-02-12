package k4unl.minecraft.Hydraulicraft.thirdParty.rf.blocks;

import k4unl.minecraft.Hydraulicraft.blocks.handlers.HydraulicTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;

public class HandlerRFPump extends HydraulicTieredBlockHandler {

    public HandlerRFPump(Block block) {

        super(block, Names.blockRFPump);
    }

	/*@Override
	public IIcon getIconFromDamage(int metadata) {
		return ThermalExpansion.blockRFPump.getIcon(0, metadata);
	}*/
}
