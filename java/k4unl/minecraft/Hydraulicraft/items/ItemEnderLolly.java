package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemFood;

public class ItemEnderLolly extends ItemFood {
	public ItemEnderLolly() {
		super(4, false);
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemEnderLolly.unlocalized);
		setTextureName(ModInfo.LID + ":" + Names.itemEnderLolly.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
}
