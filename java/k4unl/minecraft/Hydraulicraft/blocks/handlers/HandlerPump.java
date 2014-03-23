package k4unl.minecraft.Hydraulicraft.blocks.handlers;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlockHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class HandlerPump extends MachineTieredBlockHandler {
	
	public HandlerPump(int blockId, Block block) {
		super(blockId, Names.blockHydraulicPump);
	}
	
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		itemIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
	}
	
	@Override
	public Icon getIconFromDamage(int metadata) {
		return itemIcon;
	}
}
