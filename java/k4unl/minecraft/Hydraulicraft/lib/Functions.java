package k4unl.minecraft.Hydraulicraft.lib;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.multipart.TileMultipart;
import cpw.mods.fml.relauncher.SideOnly;

public class Functions {
	private static boolean isUpdateAvailable;
	
	public static void showMessageInChat(String message){
		EntityClientPlayerMP pl = Minecraft.getMinecraft().thePlayer;
		pl.addChatMessage(message);
		
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
	
	public static int getMaxGenPerTier(int tier, boolean isOil){
		if(!isOil){
			switch(tier){
			case 0:
				return Constants.MAX_MBAR_GEN_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_GEN_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_GEN_WATER_TIER_3;
			}			
		}else{
			switch(tier){
			case 0:
				return Constants.MAX_MBAR_GEN_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_GEN_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_GEN_OIL_TIER_3;
			}
		}
		return 0;
	}
	
	public static int getMaxPressurePerTier(int tier, boolean isOil){
		if(!isOil){
			switch(tier){
			case 0:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_WATER_TIER_3;
			}			
		}else{
			switch(tier){
			case 0:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_OIL_TIER_3;
			}
		}
		return 0;
	}
}
