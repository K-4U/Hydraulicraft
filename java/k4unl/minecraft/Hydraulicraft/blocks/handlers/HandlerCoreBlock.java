package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;

public class HandlerCoreBlock extends HydraulicTieredBlockHandler {

    public HandlerCoreBlock(Block block) {

        super(block, Names.blockCore);
    }

	/*@Override
    public IIcon getIconFromDamage(int metadata) {
		return HCBlocks.hydraulicPump.getIcon(0, metadata);
	}*/
}
