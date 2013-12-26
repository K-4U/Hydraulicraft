package k4unl.minecraft.Hydraulicraft.lib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
import k4unl.minecraft.Hydraulicraft.items.Items;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipes {
	public static void init(){
		GameRegistry.registerCraftingHandler(new CraftingHandler());

		initializeBlockRecipes();
		initializeItemRecipes();
		
		initializeSmeltingRecipes();

        initializeCrushingRecipes();
	}

    private static void initializeCrushingRecipes() {
        //Get items from ore dictionary:
        List<String> crushableItems = new ArrayList<String>();
        crushableItems.add("Gold");
        crushableItems.add("Iron");
        crushableItems.add("Copper");
        crushableItems.add("Lead");
        if(Loader.isModLoaded("factorization")){
        	crushableItems.add("FzDarkIron");
        }
        crushableItems.add("Tin");
        crushableItems.add("Cobalt");
        crushableItems.add("Silver");
        crushableItems.add("Nickel");
        
        
        //allowedList.add("Quartz");

        for(String item : crushableItems){
            

            String oreName = "ore" + item;
            ArrayList<ItemStack> oreStack = OreDictionary.getOres(oreName);

            String ingotName = "ingot" + item;
            ArrayList<ItemStack> ingotStack = OreDictionary.getOres(ingotName);
            
            if(oreStack.size() > 0 && ingotStack.size() > 0){
            	int metaId = Items.itemChunk.addChunk(item);
                Items.itemDust.addDust(item, metaId);
            	
		        CrushingRecipes.addCrushingRecipe(new CrushingRecipes
		                .CrushingRecipe
		                (oreStack.get(0), 10F, new ItemStack(Items.itemChunk
		                .itemID, 2, metaId)));
		
		        
		        
		        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe
		                (ingotStack.get(0), 10F,
		                        new ItemStack(Items.itemDust.itemID, 1, metaId)));
		
		        
		        WashingRecipes.addWashingRecipe(new WashingRecipes.WashingRecipe(
		               new ItemStack(Items.itemChunk.itemID, 1, metaId), 10F,
		                new ItemStack(Items.itemDust.itemID, 2, metaId)));
            }
        }
        
        //Other mods. Stuff that doesn't follow the ingot stuff.
        if(Loader.isModLoaded("AppliedEnergistics")){
        	registerNonStandardCrushRecipe("oreCertusQuartz", "crystalCertusQuartz", 2);
        }
        if(Loader.isModLoaded("IC2")){
        	registerNonStandardCrushRecipe("oreUranium", "crushedUranium", 2);
        }
    }
    
    private static void registerNonStandardCrushRecipe(String sourceName, String targetName, int number){
    	ArrayList<ItemStack> oreStackL = OreDictionary.getOres(sourceName);
        ArrayList<ItemStack> targetStackL = OreDictionary.getOres(targetName);
        if(oreStackL.size() == 0 || targetStackL.size() == 0)
        	return;
        
        ItemStack oreStack = oreStackL.get(0);
        ItemStack targetStack = targetStackL.get(0);
        targetStack.stackSize = number;
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(oreStack, 10F, targetStack));
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicPump, 1, 2), true,
                new Object[]{
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
                new Object[]{
                        "GKG",
                        "KCK",
                        "SSS",
                        'K', Items.gasket,
                        'G', Block.glass,
                        'S', Block.stone,
                        'C', Block.chest
                });
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicHose, 4, 0), true,
                new Object[]{
                        "LLL",
                        "K-K",
                        "LLL",
                        'K', Items.gasket,
                        'L', "ingotLead"
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.hydraulicHose, 6, 1), true,
                new Object[]{
                        "CCC",
                        "KHK",
                        "CCC",
                        'K', Items.gasket,
                        'C', "ingotCopper",
                        'H', new ItemStack(Blocks.hydraulicHose, 1, 0)
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
