package pet.minecraft.Hydraulicraft.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pet.minecraft.Hydraulicraft.blocks.Blocks;
import pet.minecraft.Hydraulicraft.items.Items;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
	public static void init(){
		GameRegistry.registerCraftingHandler(new CraftingHandler());
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicPump, 1),
			new Object [] {
				"PKP",
				"GIG",
				"PSP",
				'P', Block.pistonBase,
				'K', Items.gasket,
				'G', Block.glass,
				'S', Block.cobblestone,
				'I', Item.ingotIron
			});
	}
}
