package k4unl.minecraft.Hydraulicraft.baseClasses;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class Ore extends Block {
	private Id tBlockId;
	private Name oName;
	
	public Ore(Id blockId, Name oreName) {
		super(blockId.act, Material.rock);
		
		tBlockId = blockId;
		oName = oreName;
		
		setUnlocalizedName(oName.unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5F);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized);
	}
	
	@Override
	public Icon getIcon(int side, int metadata){
		return blockIcon;
	}
	
	

}
