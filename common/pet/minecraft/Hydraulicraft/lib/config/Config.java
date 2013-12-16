package pet.minecraft.Hydraulicraft.lib.config;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Config {
	public static boolean isInString(String oreName, String[] list){
		boolean ret = false;
		for(int i = 0; i < list.length; i++){
			ret = ret || (oreName.substring(0, list[i].length()).equals(list[i]));
		}
		return ret;
	}
	
	
	public static boolean canBeCrushed(ItemStack itemStack){
		//Only allowed to be crushed are:
		//ingots
		//Ores
		//Smoothstone
		int oreId;
		if((oreId = OreDictionary.getOreID(itemStack)) > 0){
			String oreName = OreDictionary.getOreName(oreId);
			String[] allowed = { "ore", "ingot"};
			return isInString(oreName, allowed) || oreName.equals("stone");
		}else{
			return false;
		}
	}
	
	public static boolean canBeWashed(ItemStack itemStack){
		//Only allowed to be washed are:
		//chunks
		int oreId;
		if((oreId = OreDictionary.getOreID(itemStack)) > 0){
			String oreName = OreDictionary.getOreName(oreId);
			String[] allowed = {"chunk"};
			return isInString(oreName, allowed);
		}else{
			return false;
		}
	}
}
