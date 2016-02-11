package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.ItemFood;

public class ItemBacon extends ItemFood {

    public ItemBacon() {

        super(HCConfig.INSTANCE.getInt("baconFoodLevel"), false);

        setMaxStackSize(64);
        setUnlocalizedName(Names.itemBacon.unlocalized);

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }
}
