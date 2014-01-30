package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import net.minecraft.item.ItemStack;

public class Config {
	//First is blockId. Next one is metadata when fully grown.
	public static int[][] harvestableItems = {
			{59, 7},
			{141, 7},
			{142, 7}
	};
	
	public static boolean canBeCrushed(ItemStack toCrush){
		return (CrushingRecipes.getCrushingRecipe(toCrush) != null);
	}
	
	public static boolean canBeCrushed(String oreName){
		return (CrushingRecipes.getCrushingRecipe(oreName) != null);
	}
	
	public static boolean canBeWashed(ItemStack itemStack){
        return (WashingRecipes.getWashingRecipe(itemStack) != null);
	}
	
}
