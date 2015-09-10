package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

public class SubBlockBase extends HydraulicBlockBase {
	private List<IIcon> icons;
	/*private List<Icon> topIcons;
	private List<Icon> bottomIcons;
	*/
	private Name[] mName;
	
	
	protected SubBlockBase(Name[] machineName) {
		super(machineName[0], true);
		
		mName = machineName;
		
		icons = new ArrayList<IIcon>();
		//topIcons = new ArrayList<Icon>();
		//bottomIcons = new ArrayList<Icon>();
		
		
		setBlockName(mName[0].unlocalized);
		setStepSound(Block.soundTypeStone);
		setHardness(3.5F);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	public void setHasIcons(int index, boolean hasTop, boolean hasBottom){
		
	}
	
	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list){
		for(int i = 0; i < 3; i++){
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	
	private String getTextureName(String side, int subId){
		if(!side.equals("")){
			return ModInfo.LID + ":" + mName[subId].unlocalized + "_" + side;
		}else{
			return ModInfo.LID + ":" + mName[subId].unlocalized;
		}
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegistry){
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
	public IIcon getIcon(int side, int metadata){
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
