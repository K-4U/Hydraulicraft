package pet.minecraft.Hydraulicraft.lib.config;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pet.minecraft.Hydraulicraft.lib.Functions;

public class Config {
	
	public static boolean canBeCrushed(String oreName){
		String[] allowed = { "ore", "ingot"};
		return Functions.isInString(oreName, allowed) || oreName.equals("stone");
	}
	
	public static boolean canBeCrushed(ItemStack itemStack){
		//Only allowed to be crushed are:
		//ingots
		//Ores
		//Smoothstone
		int oreId;
		if((oreId = OreDictionary.getOreID(itemStack)) > 0){
			String oreName = OreDictionary.getOreName(oreId);
			return canBeCrushed(oreName);
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
			return Functions.isInString(oreName, allowed);
		}else{
			return false;
		}
	}
}
