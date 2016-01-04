package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks;

import k4unl.minecraft.Hydraulicraft.blocks.handlers.HydraulicTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;

public class HandlerElectricPump extends HydraulicTieredBlockHandler {
	
	public HandlerElectricPump(Block block) {
		super(block, Names.blockElectricPump);
	}
	
/*	@Override
	public IIcon getIconFromDamage(int metadata) {
		return IndustrialCraft.blockElectricPump.getIcon(0, metadata);
	}*/
}
