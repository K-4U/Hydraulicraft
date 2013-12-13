package pet.minecraft.Hydraulicraft.lib;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
	public static void init(){
		GameRegistry.registerCraftingHandler(new CraftingHandler());
		
	}
}
