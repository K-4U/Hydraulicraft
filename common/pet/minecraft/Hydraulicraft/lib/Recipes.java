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
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicFrictionPlate, 1),
				new Object [] {
					"GKG",
					"F-F",
					"SSS",
					"S", Block.cobblestone,
					"G", Block.glass,
					"S", Block.cobblestone
					
				});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicPressureValve, 1),
				new Object [] {
					"---",
					"HLH",
					"---",
					'H', Blocks.hydraulicHoze,
					'L', Items.ingotLead,
				});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicCrusher, 1),
				new Object [] {
					"-K-",
					"P-P",
					"SSS",
					'K', Items.gasket,
					'P', Blocks.hydraulicPiston,
					'S', Block.cobblestone
				});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicWasher, 1),
				new Object [] {
					"GKG",
					"K-G",
					"SSS",
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.cobblestone
				});

		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicMixer, 1),
				new Object [] {
					"GKG",
					"K-K",
					"SSS",
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.cobblestone
				});

	}
}
