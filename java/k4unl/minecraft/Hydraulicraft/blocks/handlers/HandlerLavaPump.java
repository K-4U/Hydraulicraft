package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;

public class HandlerLavaPump extends HydraulicTieredBlockHandler {

    public HandlerLavaPump(Block block) {

        super(block, Names.blockHydraulicLavaPump);
    }

	/*
	@Override
	public void registerIcons(IIconRegister iconRegistry){
		itemIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		return itemIcon;
	}*/
}
