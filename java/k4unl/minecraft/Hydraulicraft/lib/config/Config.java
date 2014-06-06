package k4unl.minecraft.Hydraulicraft.lib.config;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class Config {
	private static class configOption{
		private String key;
		private boolean isBool;
		private boolean val;
		private boolean def;
		private int valInt;
		private int defInt;
		public configOption(String nKey, boolean def){
			key = nKey;
			val = def;
			this.def = def;
			isBool = true;
		}
		
		public configOption(String nKey, int _def){
			key = nKey;
			valInt = _def;
			defInt = _def;
			isBool = false;
		}
		
		public String getKey(){
			return key;
		}
		public boolean getValue(){
			return val;
		}
		public int getInt(){
			return valInt;
		}
		public void setValue(boolean nValue){
			val = nValue;
		}
		public void setValue(int nValue){
			valInt = nValue;
		}
		public void loadFromConfig(Configuration config){
			if(isBool){
				val = config.get(config.CATEGORY_GENERAL, key, def).getBoolean(def);
			}else{
				valInt = config.get(config.CATEGORY_GENERAL, key, defInt).getInt(defInt);
			}
		}
	}
	private static final List<configOption> configOptions = new ArrayList<configOption>();
	//First is harvester ID
	//Second blockId. 
	//Next one is metadata when fully grown.
	
	
	static {
		configOptions.add(new configOption("shouldGenCopperOre", true));
		configOptions.add(new configOption("shouldGenLeadOre", true));
		configOptions.add(new configOption("shouldDolleyInHarvesterGoBack", true));
		configOptions.add(new configOption("explosions", true));
		configOptions.add(new configOption("canSawTwoMicroblocksAtOnce", true));
		configOptions.add(new configOption("checkForUpdates", true));
		configOptions.add(new configOption("enableRF", true));
		configOptions.add(new configOption("waterPumpPerTick", 100));
		configOptions.add(new configOption("maxMBarGenWaterT1", 25));
		configOptions.add(new configOption("maxMBarGenWaterT2", 75));
		configOptions.add(new configOption("maxMBarGenWaterT3", 125));
		
		configOptions.add(new configOption("maxMBarGenOilT1", 50));
		configOptions.add(new configOption("maxMBarGenOilT2", 150));
		configOptions.add(new configOption("maxMBarGenOilT3", 250));
		configOptions.add(new configOption("conversionRatioLavaHydraulic", 100));
		configOptions.add(new configOption("maxFluidMultiplier", 10));
	}
	
	public static boolean canBeCrushed(ItemStack toCrush){
		return (CrushingRecipes.getCrushingRecipeOutput(toCrush) != null);
	}
	
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
	
	public static int getInt(String key) {
		for(configOption config : configOptions){
			if(config.getKey().equals(key)){
				return config.getInt();
			}
		}
		return 0;
	}
	
}
