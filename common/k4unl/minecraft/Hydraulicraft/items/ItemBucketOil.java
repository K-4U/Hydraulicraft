package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.item.ItemBucket;

public class ItemBucketOil extends ItemBucket {

	public ItemBucketOil() {
		super(Ids.blockFluidOil.act, Ids.itemBucketOil.act);
		
		Name itemName = Names.itemBucketOil;
		
		setUnlocalizedName(itemName.unlocalized);
		setTextureName(ModInfo.LID + ":" + itemName.unlocalized);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}

}
