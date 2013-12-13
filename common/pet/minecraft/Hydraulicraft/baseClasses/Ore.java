package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

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
