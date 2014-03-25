package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.items.HCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomTabs {
	public static CreativeTabs tabHydraulicraft;
	
	public static void init(){
		tabHydraulicraft = new CreativeTabs("tabHydraulicraft") {
            public ItemStack getIconItemStack() {
                return new ItemStack(HCItems.gasket, 1, 0);
            }

			@Override
			public Item getTabIconItem() {
				return HCItems.gasket;
			}
		};
	}
}
