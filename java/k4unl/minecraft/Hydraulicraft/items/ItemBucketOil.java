package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;

public class ItemBucketOil extends ItemBucket {

	public ItemBucketOil() {
		super(Fluids.fluidOilBlock);
		
		Name itemName = Names.itemBucketOil;
		
		setUnlocalizedName(itemName.unlocalized);
		setTextureName(ModInfo.LID + ":" + itemName.unlocalized);
		
		setContainerItem((Item) Item.itemRegistry.getObject("bucket"));
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}

}
