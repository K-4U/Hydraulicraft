package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.IndustrialCraft;
import net.minecraft.block.Block;
import net.minecraft.util.Icon;

public class HandlerElectricPump extends MachineTieredBlockHandler {
	
	public HandlerElectricPump(int blockId, Block block) {
		super(blockId, Names.blockElectricPump);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return IndustrialCraft.blockElectricPump.getIcon(0, metadata);
	}
}
