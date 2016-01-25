package k4unl.minecraft.Hydraulicraft.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

public class ItemCopperEnrichedDust extends HydraulicItemBase {

	public ItemCopperEnrichedDust() {
		super(Names.itemCopperEnrichedDust, true);
		
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(this, 1),
                new ItemStack(HCItems.ingotEnrichedCopper, 1), 0F);
	}

}
