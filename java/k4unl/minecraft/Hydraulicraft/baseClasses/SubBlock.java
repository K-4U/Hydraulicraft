package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;

public class SubBlock extends MachineBlock {
	private List<Icon> icons;
	/*private List<Icon> topIcons;
	private List<Icon> bottomIcons;
	*/
	private Id tBlockId;
	private Name[] mName;
	
	
	protected SubBlock(Id blockId, Name[] machineName) {
		super(blockId, machineName[0]);
		
		tBlockId = blockId;
		mName = machineName;
		
		icons = new ArrayList<Icon>();
		//topIcons = new ArrayList<Icon>();
		//bottomIcons = new ArrayList<Icon>();
		
		
		setUnlocalizedName(mName[0].unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5F);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	public void setHasIcons(int index, boolean hasTop, boolean hasBottom){
		
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list){
		for(int i = 0; i < 3; i++){
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	
	private String getTextureName(String side, int subId){
		if(side != ""){
			return ModInfo.LID + ":" + mName[subId].unlocalized + "_" + side;
		}else{
			return ModInfo.LID + ":" + mName[subId].unlocalized;
		}
	}
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		for(int i = 0; i < mName.length; i++){
			icons.add(i, iconRegistry.registerIcon(getTextureName("",i)));
			/*if(hasTopIcon[i]){
				topIcons.add(i, iconRegistry.registerIcon(getTextureName("top",i)));
			}else{
				topIcons.add(i, icons.get(i));
			}
			if(hasBottomIcon[i]){
				bottomIcons.add(i, iconRegistry.registerIcon(getTextureName("bottom",i)));
			}else{
				bottomIcons.add(i, icons.get(i));
			}*/
		}
	}
	
	
	
	@Override
	public Icon getIcon(int side, int metadata){
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(metadata >= icons.size()){
			metadata = 0;
		}/*
		if(s.equals(ForgeDirection.UP)){
			return topIcons.get(metadata);
		}else if(s.equals(ForgeDirection.DOWN)){
			return bottomIcons.get(metadata);
		}*/

		return icons.get(metadata);
		
	}
	
	@Override
	public int damageDropped(int damageValue){
		return damageValue;
	}
	

}
