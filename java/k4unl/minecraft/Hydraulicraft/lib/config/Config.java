package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.*;


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

        protected configOption(String nKey){
            key = nKey;
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
    private static final Map<Block, Integer> tankScores = new HashMap<Block, Integer>();
    private static final Map<Block, Boolean> tankBlackList = new HashMap<Block, Boolean>();

	
	static {
		configOptions.add(new configOption("shouldGenCopperOre", true));
		configOptions.add(new configOption("shouldGenLeadOre", true));
		configOptions.add(new configOption("shouldDolleyInHarvesterGoBack", true));
		configOptions.add(new configOption("explosions", true));
		configOptions.add(new configOption("canSawTwoMicroblocksAtOnce", true));
		configOptions.add(new configOption("checkForUpdates", true));
		configOptions.add(new configOption("enableRF", false));
		configOptions.add(new configOption("waterPumpPerTick", 100));
		configOptions.add(new configOption("maxMBarGenWaterT1", 25));
		configOptions.add(new configOption("maxMBarGenWaterT2", 75));
		configOptions.add(new configOption("maxMBarGenWaterT3", 125));
		
		configOptions.add(new configOption("maxMBarGenOilT1", 50));
		configOptions.add(new configOption("maxMBarGenOilT2", 150));
		configOptions.add(new configOption("maxMBarGenOilT3", 250));
		configOptions.add(new configOption("conversionRatioLavaHydraulic", 100));
		configOptions.add(new configOption("maxFluidMultiplier", 10));
		
		configOptions.add(new configOption("maxPortalHeight", 10));
		configOptions.add(new configOption("maxPortalWidth", 10));
		configOptions.add(new configOption("portalTimeoutInSeconds", 20));
		configOptions.add(new configOption("portalmBarUsagePerTickPerBlock", 1000));
		configOptions.add(new configOption("pressurePerTeleport", 10000));
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

    public static void loadTankOptions(Configuration c){
        tankBlackList.clear();
        tankScores.clear();
        Iterator<Block> itr = Block.blockRegistry.iterator();
        while(itr.hasNext()){
            Block bl = itr.next();

            boolean isBlackListed = false;
            if(Constants.TANK_BLACKLIST.containsKey(bl)){
                isBlackListed = Constants.TANK_BLACKLIST.get(bl);
            }
            if(!isBlackListed){
                int score = 1;
                if(Constants.TANK_SCORELIST.containsKey(bl)){
                    score = Constants.TANK_SCORELIST.get(bl);
                }
                tankScores.put(bl, c.get("tankBlockScores", bl.getUnlocalizedName(), score).getInt());
            }

            tankBlackList.put(bl, c.get("tankBlacklist", bl.getUnlocalizedName(), isBlackListed).getBoolean());
        }
	}

    public static boolean isTankBlockBlacklisted(Block bl){
        if(tankBlackList.containsKey(bl)){
            return tankBlackList.get(bl);
        }
        return false;
    }

    public static int getTankBlockScore(Block bl){
        if(tankScores.containsKey(bl)){
            return tankScores.get(bl);
        }
        return 1;
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
