package k4unl.minecraft.Hydraulicraft.lib.config;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import net.minecraft.item.ItemStack;

public class Config {
	
	//First is harvester ID
	//Second blockId. 
	//Next one is metadata when fully grown.
	public static List<Seed> harvestableItems = new ArrayList<Seed>();
	
	public static void initHarvestableItems(){
		addHarvestableItem(new Seed(0, 59, 7, 295));
		addHarvestableItem(new Seed(0, 141, 7));
		addHarvestableItem(new Seed(0, 142, 7));
	}
	
	public static void addHarvestableItem(Seed toAdd){
		harvestableItems.add(toAdd);
	}
	
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
