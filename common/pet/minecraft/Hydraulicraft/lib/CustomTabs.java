package pet.minecraft.Hydraulicraft.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomTabs {
	public static CreativeTabs tabHydraulicraft;
	
	public static void init(){
		tabHydraulicraft = new CreativeTabs("tabHydraulicraft") {
            public ItemStack getIconItemStack() {
            		//TODO: Add Hydraulicraft icon
                    return new ItemStack(Item.eyeOfEnder, 1, 0);
            }
		};
	}
}
