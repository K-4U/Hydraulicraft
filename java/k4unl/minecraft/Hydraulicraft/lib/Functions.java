package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class Functions {
	private static boolean isUpdateAvailable;
	
	public static void showMessageInChat(EntityPlayer player, String message){
		player.addChatMessage(new ChatComponentText(message));
		
	}
	
	public static List mergeList(List l1, List l2){
		for (Object object : l1) {
			if(!l2.contains(object)){
				l2.add(object);
			}
		}
		return l2;
	}
	
	public static int getIntDirFromDirection(ForgeDirection dir){
		switch(dir){
		case DOWN:
			return 0;
		case EAST:
			return 5;
		case NORTH:
			return 2;
		case SOUTH:
			return 3;
		case UNKNOWN:
			return 0;
		case UP:
			return 1;
		case WEST:
			return 4;
		default:
			return 0;
		}
	}
	
	public static ForgeDirection getDirectionFromInt(int dir){
		int metaDataToSet = 0;
		switch(dir){
		case 0:
			metaDataToSet = 2;
			break;
		case 1:
			metaDataToSet = 4;
			break;
		case 2:
			metaDataToSet = 3;
			break;
		case 3:
			metaDataToSet = 5;
			break;
		}
		return ForgeDirection.getOrientation(metaDataToSet);
	}
	
	public static boolean isInString(String oreName, String[] list){
        boolean ret = false;
        for(int i = 0; i < list.length; i++){
            ret = ret || (oreName.substring(0, list[i].length()).equals(list[i]));
        }
        return ret;
    }
	

    public static String getPrefixName(String oreDictName){
        //TODO: Fix this function up. It looks ugly
        String[] prefix = {"ingot"};
        if(isInString(oreDictName, prefix)){
            return "ingot";
        }

        prefix[0] = "ore";
        if(isInString(oreDictName, prefix)){
            return "ore";
        }

        if(oreDictName.equals("netherquartz")){
            return "Quartz";
        }else{
            return "ERROR";
        }
    }

	public static String getMetalName(String oreDictName){
		String[] prefix = {"ingot"};
		if(isInString(oreDictName, prefix)){
			return oreDictName.substring(prefix[0].length());
		}
		
		prefix[0] = "ore";
		if(isInString(oreDictName, prefix)){
			return oreDictName.substring(prefix[0].length());
		}
		
		if(oreDictName.equals("netherquartz")){
			return "Quartz";
		}else{
			return "ERROR";
		}
	}
	
	public static ItemStack getIngot(String ingotName){
		ArrayList<ItemStack> targetStackL = OreDictionary.getOres(ingotName);
		if(targetStackL.size() > 0){
			return targetStackL.get(0);
		}
		return null;
	}
	
	public static int getMaxGenPerTier(PressureTier tier, boolean isOil){
		if(!isOil){
			return HCConfig.getInt("maxMBarGenWaterT" + (tier.ordinal()+1));
		}else{
			return HCConfig.getInt("maxMBarGenOilT" + (tier.ordinal()+1));
		}
	}
	
	public static int getMaxPressurePerTier(PressureTier tier, boolean isOil){
		if(!isOil){
			switch(tier){
			case LOWPRESSURE:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case MEDIUMPRESSURE:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case HIGHPRESSURE:
				return Constants.MAX_MBAR_WATER_TIER_3;
			default:
				break;
			}			
		}else{
			switch(tier){
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
