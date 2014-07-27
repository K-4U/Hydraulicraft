package k4unl.minecraft.Hydraulicraft.lib.config;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class Config {
	private static final String isDev = "@DEV@";
	
	private static class configOption{
		private String key;
		private boolean val;
		private boolean def;
		public configOption(String nKey, boolean def){
			key = nKey;
			val = def;
			this.def = def;
		}
		public String getKey(){
			return key;
		}
		public boolean getValue(){
			return val;
		}
		public void setValue(boolean nValue){
			val = nValue;
		}
		public void loadFromConfig(Configuration config){
			val = config.get(config.CATEGORY_GENERAL, key, def).getBoolean(def);
		}
	}
	private static final List<configOption> configOptions = new ArrayList<configOption>();
	//First is harvester ID
	//Second blockId. 
	//Next one is metadata when fully grown.
	public static List<Seed> harvestableItems = new ArrayList<Seed>();
	
	public static boolean isThisDev(){
		if(!isDev.equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	static {
		configOptions.add(new configOption("shouldGenOres", true));
		configOptions.add(new configOption("shouldDolleyInHarvesterGoBack", true));
		configOptions.add(new configOption("explosions", true));
		configOptions.add(new configOption("canSawTwoMicroblocksAtOnce", true));
		
		addHarvestableItem(new Seed(0, 59, 7, 295));
		addHarvestableItem(new Seed(0, 141, 7, 391));
		addHarvestableItem(new Seed(0, 142, 7, 392));
		addHarvestableItem(new Seed(Constants.HARVESTER_ID_SUGARCANE, Block.reed.blockID, 0, Item.reed.itemID));
	}
	
	
	public static void addHarvestableItem(Seed toAdd){
		harvestableItems.add(toAdd);
	}
	
	public static boolean canBeCrushed(ItemStack toCrush){
		return (CrushingRecipes.getCrushingRecipeOutput(toCrush) != null);
	}
	
	/*
	public static boolean canBeCrushed(String oreName){
		return (CrushingRecipes.getCrushingRecipe(oreName) != null);
	}*/
	
	public static boolean canBeWashed(ItemStack itemStack){
        return (WashingRecipes.getWashingRecipeOutput(itemStack) != null);
	}

	public static void loadConfigOptions(Configuration c){
		for(configOption config : configOptions){
			config.loadFromConfig(c);
		}
	}
	
	public static boolean get(String key) {
		for(configOption config : configOptions){
			if(config.getKey().equals(key)){
				return config.getValue();
			}
		}
		return false;
	}
	
}
