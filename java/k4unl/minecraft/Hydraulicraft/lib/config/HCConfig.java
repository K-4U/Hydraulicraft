package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import k4unl.minecraft.k4lib.lib.config.Config;
import k4unl.minecraft.k4lib.lib.config.ConfigOption;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.*;


public class HCConfig extends Config{
    public static final Config INSTANCE = new HCConfig();

    private static final Map<Block, Integer>                     tankScores    = new HashMap<Block, Integer>();
    private static final Map<Block, Boolean>                     tankBlackList = new HashMap<Block, Boolean>();

    @Override
    public void init(){
        configOptions.add(new ConfigOption("shouldDolleyInHarvesterGoBack", true));
        configOptions.add(new ConfigOption("explosions", true));
        configOptions.add(new ConfigOption("canSawTwoMicroblocksAtOnce", true));
        configOptions.add(new ConfigOption("checkForUpdates", true));
        configOptions.add(new ConfigOption("enableRF", false));
        configOptions.add(new ConfigOption("waterPumpPerTick", 100));
        configOptions.add(new ConfigOption("maxMBarGenWaterT1", 25));
        configOptions.add(new ConfigOption("maxMBarGenWaterT2", 75));
        configOptions.add(new ConfigOption("maxMBarGenWaterT3", 125));

        configOptions.add(new ConfigOption("maxMBarGenOilT1", 50));
        configOptions.add(new ConfigOption("maxMBarGenOilT2", 150));
        configOptions.add(new ConfigOption("maxMBarGenOilT3", 250));
        configOptions.add(new ConfigOption("conversionRatioLavaHydraulic", 100));
        configOptions.add(new ConfigOption("maxFluidMultiplier", 10));

        configOptions.add(new ConfigOption("maxPortalHeight", 10).setCategory("portals"));
        configOptions.add(new ConfigOption("maxPortalWidth", 10).setCategory("portals"));
        configOptions.add(new ConfigOption("portalTimeoutInSeconds", 20).setCategory("portals"));
        configOptions.add(new ConfigOption("portalmBarUsagePerTickPerBlock", 1000).setCategory("portals"));
        configOptions.add(new ConfigOption("pressurePerTeleport", 10000).setCategory("portals"));

        configOptions.add(new ConfigOption("disableBacon", false).setCategory("food"));
        configOptions.add(new ConfigOption("baconFoodLevel", 4).setCategory("food"));
        configOptions.add(new ConfigOption("disableEnderLolly", false).setCategory("food"));
        configOptions.add(new ConfigOption("enderLollyFoodLevel", 4).setCategory("food"));


        configOptions.add(new ConfigOption("shouldGenCopperOre", true).setCategory("worldgen"));
        configOptions.add(new ConfigOption("shouldGenLeadOre", true).setCategory("worldgen"));
        configOptions.add(new ConfigOption("shouldGenMinerals", true).setCategory("worldgen"));
        configOptions.add(new ConfigOption("copperMinY", 35).setCategory("worldgen"));
        configOptions.add(new ConfigOption("copperMaxY", 90).setCategory("worldgen"));
        configOptions.add(new ConfigOption("copperVeinSize", 7).setCategory("worldgen"));
        configOptions.add(new ConfigOption("copperVeinCount", 8).setCategory("worldgen"));

        configOptions.add(new ConfigOption("leadMinY", 35).setCategory("worldgen"));
        configOptions.add(new ConfigOption("leadMaxY", 90).setCategory("worldgen"));
        configOptions.add(new ConfigOption("leadVeinSize", 4).setCategory("worldgen"));
        configOptions.add(new ConfigOption("leadVeinCount", 8).setCategory("worldgen"));

        //TODO: Come up with several minerals.
        configOptions.add(new ConfigOption("loneziumMinY", 35).setCategory("worldgen"));
        configOptions.add(new ConfigOption("loneziumMaxY", 90).setCategory("worldgen"));
        configOptions.add(new ConfigOption("loneziumVeinSize", 7).setCategory("worldgen"));
        configOptions.add(new ConfigOption("loneziumVeinCount", 8).setCategory("worldgen"));

        configOptions.add(new ConfigOption("shouldGenOil", true).setCategory("worldgen"));
        configOptions.add(new ConfigOption("oilChance", 0.005).setCategory("worldgen"));
        configOptions.add(new ConfigOption("oilSpoutSize", 1).setCategory("worldgen").setComment("How big the oil spout is, above ground. Set to negative value for harder to find spouts"));
	}
	
	public static boolean canBeCrushed(ItemStack toCrush){
		return (CrushingRecipes.getCrushingRecipeOutput(toCrush) != null);
	}
	
	public static boolean canBeWashed(ItemStack itemStack){
        return (WashingRecipes.getWashingRecipeOutput(itemStack) != null);
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
}
