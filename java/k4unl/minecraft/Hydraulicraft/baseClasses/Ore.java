package k4unl.minecraft.Hydraulicraft.baseClasses;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class Ore extends Block {
	private Name oName;
	
	public Ore(Name oreName) {
		super(Material.rock);
		
		oName = oreName;
		
		setBlockName(oName.unlocalized);
		setStepSound(Block.soundTypeStone);
		setHardness(3.5F);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegistry){
		blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized);
	}
	
	@Override
	public IIcon getIcon(int side, int metadata){
		return blockIcon;
	}
	
	

}
