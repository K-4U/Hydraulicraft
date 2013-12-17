package pet.minecraft.Hydraulicraft.lib;

import org.omg.CORBA.INITIALIZE;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import pet.minecraft.Hydraulicraft.blocks.Blocks;
import pet.minecraft.Hydraulicraft.items.Items;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.ores.Ores;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
	public static void init(){
		GameRegistry.registerCraftingHandler(new CraftingHandler());

		initializeBlockRecipes();
		initializeItemRecipes();
		
		initializeSmeltingRecipes();
	}
	
	private static void initializeSmeltingRecipes(){
		GameRegistry.addSmelting(Ids.oreCopper.act, new ItemStack(Items.ingotCopper), 0);
		GameRegistry.addSmelting(Ids.oreLead.act, new ItemStack(Items.ingotLead), 0);
	}
	
	private static void initializeBlockRecipes(){
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPump,1,0), true,
			new Object [] {
				"PKP",
				"GLG",
				"PSP",
				'P', Block.pistonBase,
				'K', Items.gasket,
				'G', Block.glass,
				'S', Block.stone,
				'L', "ingotLead"
			})
		);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPump,1,1), true,
				new Object [] {
					"PKP",
					"GCG",
					"PSP",
					'P', Block.pistonBase,
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.stone,
					'C', "ingotCopper"
				})
			);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPump,1,2), true,
				new Object [] {
					"PKP",
					"GCG",
					"PSP",
					'P', Block.pistonBase,
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.stone,
					'C', "ingotEnrichedCopper"
				})
			);

		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicMixer, 1),
				new Object [] {
					"GKG",
					"KCK",
					"SSS",
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.stone,
					'C', Block.chest
				});
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicHose, 4, 0), true,
				new Object [] {
					"LLL",
					"K-K",
					"LLL",
					'K', Items.gasket,
					'L', "ingotLead"
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicHose, 6, 1), true,
				new Object [] {
					"CCC",
					"KHK",
					"CCC",
					'K', Items.gasket,
					'C', "ingotCopper",
					'H', new ItemStack(Blocks.hydraulicHose,1,0)
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicHose, 12, 2), true,
				new Object [] {
					"C-C",
					"KHK",
					"C-C",
					'K', Items.gasket,
					'C', "ingotEnrichedCopper",
					'H', new ItemStack(Blocks.hydraulicHose,1,1)
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPressurevat, 1, 0), true,
				new Object [] {
					"LSL",
					"KGK",
					"LSL",
					'S', Block.stone,
					'K', Items.gasket,
					'L', "ingotLead",
					'G', Block.glass
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPressurevat, 1, 1), true,
				new Object [] {
					"CSC",
					"KVK",
					"CSC",
					'S', Block.stone,
					'K', Items.gasket,
					'C', "ingotCopper",
					'V', new ItemStack(Blocks.hydraulicPressurevat, 1,0)
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPressurevat, 1, 2), true,
				new Object [] {
					"SCS",
					"KVK",
					"SCS",
					'S', Block.stone,
					'K', Items.gasket,
					'C', "ingotEnrichedCopper",
					'V', new ItemStack(Blocks.hydraulicPressurevat, 1, 1)
			}));
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicFrictionIncinerator, 1),
			new Object [] {
				"GKG",
				"FCF",
				"SSS",
				'S', Block.stone,
				'G', Block.glass,
				'F', Items.itemFrictionPlate,
				'K', Items.gasket,
				'C', Block.chest
				
			});
		
		
		/*GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicPressureValve, 1),
				new Object [] {
					"---",
					"HLH",
					"---",
					'H', Blocks.hydraulicHose,
					'L', Block.lever,
				});*/
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicCrusher, 1),
				new Object [] {
					"-K-",
					"PCP",
					"SSS",
					'K', Items.gasket,
					'P', Block.pistonBase,
					'S', Block.stone,
					'C', Block.chest
				});
		
		GameRegistry.addRecipe(new ItemStack(Blocks.hydraulicWasher, 1),
				new Object [] {
					"GKG",
					"KCG",
					"SSS",
					'K', Items.gasket,
					'G', Block.glass,
					'S', Block.stone,
					'C', Block.chest
				});
	}
	
	private static void initializeItemRecipes(){
		GameRegistry.addRecipe(new ItemStack(Items.itemFrictionPlate, 1),
		new Object [] {
			"-SS",
			"S-S",
			"SS-",
			'S', Block.stone
			
		});
	
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.ingotEnrichedCopper, 4), true,
			new Object [] {
				"---",
				"CDC",
				"---",
				'D', Item.diamond,
				'C', "ingotCopper"
		}));

		
		GameRegistry.addRecipe(new ItemStack(Items.gasket, 4),
			new Object [] {
				"P-P",
				"-B-",
				"P-P",
				'P', Item.paper,
				'B', Block.fenceIron
			}
		);
		
		
	}
	
}
