package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class Functions {

    private static boolean isUpdateAvailable;

    public static void showMessageInChat(EntityPlayer player, String message) {

        player.addChatMessage(new TextComponentString(message));

    }

    public static List mergeList(List l1, List l2) {

        for (Object object : l1) {
            if (!l2.contains(object)) {
                l2.add(object);
            }
        }
        return l2;
    }

    public static ItemStack getIngot(String ingotName) {

        List<ItemStack> targetStackL = OreDictionary.getOres(ingotName);
        if (targetStackL.size() > 0) {
            return targetStackL.get(0);
        }
        return null;
    }

    public static int getMaxGenPerTier(PressureTier tier, boolean isOil) {

        if (!isOil) {
            return HCConfig.INSTANCE.getInt("maxMBarGenWaterT" + (tier.ordinal() + 1));
        } else {
            return HCConfig.INSTANCE.getInt("maxMBarGenOilT" + (tier.ordinal() + 1));
        }
    }

    public static int getMaxPressurePerTier(PressureTier tier, boolean isOil) {

        if (!isOil) {
            switch (tier) {
                case LOWPRESSURE:
                    return Constants.MAX_MBAR_WATER_TIER_1;
                case MEDIUMPRESSURE:
                    return Constants.MAX_MBAR_WATER_TIER_2;
                case HIGHPRESSURE:
                    return Constants.MAX_MBAR_WATER_TIER_3;
                default:
                    break;
            }
        } else {
            switch (tier) {
                case LOWPRESSURE:
                    return Constants.MAX_MBAR_OIL_TIER_1;
                case MEDIUMPRESSURE:
                    return Constants.MAX_MBAR_OIL_TIER_2;
                case HIGHPRESSURE:
                    return Constants.MAX_MBAR_OIL_TIER_3;
                default:
                    break;
            }
        }
        return 0;
    }
}
