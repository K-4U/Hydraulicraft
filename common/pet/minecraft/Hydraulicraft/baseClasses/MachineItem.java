package pet.minecraft.Hydraulicraft.baseClasses;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class MachineItem extends Item {
	private Id tItemId;
	private Name mName;
	private boolean _hasEffect = false;
	private String defaultInfo = "";
	
	
	public MachineItem(Id itemId, Name itemName) {
		super(itemId.act);
		
		tItemId = itemId;
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
		if(par4){
			list.add("Uh?");
		}
		list.add(defaultInfo);
	}

}
