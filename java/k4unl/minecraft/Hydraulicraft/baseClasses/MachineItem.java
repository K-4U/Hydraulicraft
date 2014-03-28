package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MachineItem extends Item {
	private Name mName;
	private boolean _hasEffect = false;
	private String defaultInfo = "";
	
	
	public MachineItem(Name itemName) {
		super();
		
		mName = itemName;
		
		setMaxStackSize(64);
		setUnlocalizedName(itemName.unlocalized);
		setTextureName(ModInfo.LID + ":" + itemName.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Sets whether the "enchanted" effect is active on this item.
	 */
	public void setEffect(boolean __hasEffect){
		_hasEffect = __hasEffect;
	}
	
	@Override
	public boolean hasEffect(ItemStack itemStack){
		return _hasEffect;
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Sets the default tooltip information
	 */
	public void setDefaultInfo(String info){
		defaultInfo = info;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
		list.add(defaultInfo);
	}

}
