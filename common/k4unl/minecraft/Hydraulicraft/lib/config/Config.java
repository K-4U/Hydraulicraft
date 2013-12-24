package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import net.minecraft.item.ItemStack;

public class Config {
	public static boolean canBeCrushed(ItemStack itemStack){
		return (CrushingRecipes.getCrushingRecipe(itemStack) != null);
	}
	
	public static boolean canBeWashed(ItemStack itemStack){
        return (WashingRecipes.getWashingRecipe(itemStack) != null);
	}
}
