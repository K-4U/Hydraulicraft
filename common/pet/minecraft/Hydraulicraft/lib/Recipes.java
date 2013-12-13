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
		
		GameRegistry.addRecipe(new ItemStack(Items.itemFrictionPlate, 1),
				new Object [] {
					"-SS",
					"S-S",
					"SS-",
					'S', Block.cobblestone
					
				});
	
		GameRegistry.addRecipe(new ItemStack(Items.ingotEnrichedCopper, 1),
			new Object [] {
				"-D-",
				"CCC",
				"-D-",
				'D', Item.diamond,
				'C', Items.ingotCopper
		});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicFrictionIncinerator, 1),
			new Object [] {
				"GKG",
				"FCF",
				"SSS",
				'S', Block.cobblestone,
				'G', Block.glass,
				'F', Items.itemFrictionPlate,
				'K', Items.gasket,
				'C', Block.chest
				
			});
		
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicPressureValve, 1),
				new Object [] {
					"---",
					"HLH",
					"---",
					'H', Blocks.hydraulicHoze,
					'L', Block.lever,
				});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicCrusher, 1),
				new Object [] {
					"-K-",
					"PCP",
					"SSS",
					'K', Items.gasket,
					'P', Block.pistonBase,
					'S', Block.cobblestone,
					'C', Block.chest
				});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicWasher, 1),
				new Object [] {
					"GKG",
					"KCG",
					"SSS",
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.cobblestone,
					'C', Block.chest
				});

		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicMixer, 1),
				new Object [] {
					"GKG",
					"KCK",
					"SSS",
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.cobblestone,
					'C', Block.chest
				});
				

	}
}
