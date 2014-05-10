package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemFood;

public class ItemBacon extends ItemFood {
	public ItemBacon() {
		super(4, false);
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemBacon.unlocalized);
		setTextureName(ModInfo.LID + ":" + Names.itemBacon.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
}
