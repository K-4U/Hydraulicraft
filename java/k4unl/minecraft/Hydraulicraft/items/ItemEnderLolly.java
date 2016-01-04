package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemFood;

public class ItemEnderLolly extends ItemFood {
	public ItemEnderLolly() {
		super(HCConfig.INSTANCE.getInt("enderLollyFoodLevel"), false);
		
		setMaxStackSize(64);
		setUnlocalizedName(Names.itemEnderLolly.unlocalized);
		//setTextureName(ModInfo.LID + ":" + Names.itemEnderLolly.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
}
