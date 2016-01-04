package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;

public class HandlerPump extends HydraulicTieredBlockHandler {
	
	public HandlerPump(Block block) {
		super(block, Names.blockHydraulicPump);
	}
	/*
	
	@Override
	public void registerIcons(IIconRegister iconRegistry){
		itemIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return itemIcon;
	}
	*/
}
