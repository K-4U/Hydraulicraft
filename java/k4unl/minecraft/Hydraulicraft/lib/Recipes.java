package k4unl.minecraft.Hydraulicraft.lib;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HydraulicraftItems;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
	public static void init(){
//		GameRegistry.registerCraftingHandler(new CraftingHandler());

		initializeBlockRecipes();
		initializeItemRecipes();
		
		initializeSmeltingRecipes();

        initializeCrushingRecipes();
	}

    private static void initializeCrushingRecipes() {
    	//Yeah, just put them in, right there, yeahhhhhh
    	OreDictionary.registerOre("oreIron", Blocks.iron_ore);
    	OreDictionary.registerOre("ingotIron", Items.iron_ingot);
    	OreDictionary.registerOre("oreGold", Blocks.gold_ore);
    	OreDictionary.registerOre("ingotGold", Items.gold_ingot);
    	OreDictionary.registerOre("oreNetherQuartz", Blocks.quartz_ore);
    	OreDictionary.registerOre("gemDiamond", Items.diamond);
        //Get items from ore dictionary:
        List<String> crushableItems = new ArrayList<String>();
        crushableItems.add("Gold");
        crushableItems.add("Iron");
        //MODDED:
        crushableItems.add("Copper");
        crushableItems.add("Lead");
        crushableItems.add("FzDarkIron");
        crushableItems.add("Tin");
        crushableItems.add("Cobalt");
        crushableItems.add("Silver");
        crushableItems.add("Nickel");
        

        for(String item : crushableItems){
            String oreName = "ore" + item;
            ArrayList<ItemStack> oreStack = OreDictionary.getOres(oreName);

            String ingotName = "ingot" + item;
            ArrayList<ItemStack> ingotStack = OreDictionary.getOres(ingotName);
            
            Log.info("Found " + oreStack.size() + " ores and " + ingotStack.size() + " ingots for " + item);
            
            if(oreStack.size() > 0 && ingotStack.size() > 0){
            	int metaId = HydraulicraftItems.itemChunk.addChunk(item);
                HydraulicraftItems.itemDust.addDust(item, metaId);
                
		        CrushingRecipes.addCrushingRecipe(new CrushingRecipes
		                .CrushingRecipe
		                (oreName, 1.0F, new ItemStack(HydraulicraftItems.itemChunk, 2, metaId)));
		        
		        
		        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe
		                (ingotName, 0.5F,
		                        new ItemStack(HydraulicraftItems.itemDust, 1, metaId)));
		
		        
		        WashingRecipes.addWashingRecipe(new WashingRecipes.WashingRecipe(
		               new ItemStack(HydraulicraftItems.itemChunk, 1, metaId), 400F,
		                new ItemStack(HydraulicraftItems.itemDust, 1, metaId)));
            }
        }
        
        //Other mods. Stuff that doesn't follow the ingot stuff.
        if(Loader.isModLoaded("AppliedEnergistics")){
        	registerNonStandardCrushRecipe("oreCertusQuartz", "crystalCertusQuartz", 2);
        }
        if(Loader.isModLoaded("IC2")){
        	registerNonStandardCrushRecipe("oreUranium", "crushedUranium", 2);
        }
        
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe("oreNetherQuartz", 1.0F, new ItemStack(Items.quartz, 3)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Blocks.cobblestone, 1), 0.9F, new ItemStack(Blocks.sand, 2)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Items.bone, 1), 0.5F, new ItemStack(Items.dye, 5, 15)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Items.blaze_rod, 1), 0.5F, new ItemStack(Items.blaze_powder, 5)));
    }
    
    private static void registerNonStandardCrushRecipe(String sourceName, String targetName, int number){
    	ArrayList<ItemStack> oreStackL = OreDictionary.getOres(sourceName);
        ArrayList<ItemStack> targetStackL = OreDictionary.getOres(targetName);
        if(oreStackL.size() == 0 || targetStackL.size() == 0)
        	return;
        
        ItemStack targetStack = targetStackL.get(0);
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(sourceName, 1.1F, targetStack));
    }

    private static void initializeSmeltingRecipes(){
		GameRegistry.addSmelting(Ores.oreCopper, new ItemStack(HydraulicraftItems.ingotCopper), 0);
		GameRegistry.addSmelting(Ores.oreLead, new ItemStack(HydraulicraftItems.ingotLead), 0);
	}
	
	private static void initializeBlockRecipes(){
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockValve, 4, 0), true ,
				new Object[] {
					"WKW",
					"K K",
					"WKW",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HydraulicraftItems.gasket
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockInterfaceValve, 4, 0), true ,
				new Object[] {
					"WHW",
					"K K",
					"WHW",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HydraulicraftItems.gasket,
					'H', Blocks.hopper
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPiston, 1, 0), true ,
				new Object[] {
					"III",
					"WCW",
					"WKW",
					'W', HCBlocks.hydraulicPressureWall,
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'K', HydraulicraftItems.gasket,
					'R', Items.redstone,
					'I', "ingotIron"
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterSource, 18, 1), true ,
				new Object[] {
					"SSS",
					"-S-",
					"SSS",
					'S', Items.stick
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterSource, 1, 0), true ,
				new Object[] {
					"WWW",
					"ICK",
					"WWW",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'K', HydraulicraftItems.gasket,
					'I', HCBlocks.blockInterfaceValve
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.harvesterTrolley, 4, 0), true ,
				new Object[] {
					"-P-",
					"WCW",
					"-H-",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'H', Items.golden_hoe,
					'P', HCBlocks.hydraulicPiston
			})
		);
		
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.harvesterTrolley, 4, Constants.HARVESTER_ID_SUGARCANE), true ,
				new Object[] {
					"-P-",
					"WCW",
					"-S-",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'S', Items.shears,
					'P', HCBlocks.hydraulicPiston
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCore, 2, 0), true ,
				new Object[] {
					"LSL",
					"SWS",
					"LSL",
					'S' , Blocks.stone,
					'W', HCBlocks.hydraulicPressureWall,
					'L', "ingotLead"
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCore, 1, 1), true ,
				new Object[] {
					"CWC",
					"WBW",
					"CWC",
					'W', HCBlocks.hydraulicPressureWall,
					'C', "ingotCopper",
					'B', new ItemStack(HCBlocks.blockCore, 1, 0)
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCore, 1, 2), true ,
				new Object[] {
					"EWE",
					"WBW",
					"EWE",
					'W', HCBlocks.hydraulicPressureWall,
					'E', "ingotEnrichedCopper",
					'B', new ItemStack(HCBlocks.blockCore, 1, 1)
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressureWall, 8),true,
				new Object [] {
					"SSS",
					"SLS",
					"SSS",
					'S', Blocks.stone,
					'L', "ingotLead"
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPump, 1, 0), true,
			new Object [] {
				"PKP",
				"GCG",
				"PWP",
				'P', Blocks.piston,
				'K', HydraulicraftItems.gasket,
				'G', Blocks.glass,
				'W', HCBlocks.hydraulicPressureWall,
				'C', new ItemStack(HCBlocks.blockCore, 1, 0)
			})
		);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPump,1,1), true,
				new Object [] {
					"PKP",
					"GCG",
					"PUP",
					'P', Blocks.piston,
					'K', HydraulicraftItems.gasket,
					'G', Blocks.glass,
					'U', new ItemStack(HCBlocks.hydraulicPump, 1,0),
					'C', new ItemStack(HCBlocks.blockCore, 1, 1)
				})
			);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPump, 1, 2), true,
                new Object[]{
                        "PKP",
                        "GCG",
                        "PUP",
                        'P', Blocks.piston,
                        'K', HydraulicraftItems.gasket,
                        'G', Blocks.glass,
                        'U', new ItemStack(HCBlocks.hydraulicPump, 1, 1),
                        'C', new ItemStack(HCBlocks.blockCore, 1, 2)
                })
        );

		
		GameRegistry.addRecipe(new ItemStack(HCBlocks.hydraulicMixer, 1),
                new Object[]{
                        "GKG",
                        "KCK",
                        "WIW",
                        'K', HydraulicraftItems.gasket,
                        'G', Blocks.glass,
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 2),
                        'I', HCBlocks.blockInterfaceValve
                });
		/* FMP 
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 4, 0), true,
                new Object[]{
                        "LLL",
                        "K-K",
                        "LLL",
                        'K', HydraulicraftItems.gasket,
                        'L', "ingotLead"
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 1, 1), true,
                new Object[]{
                        "C-C",
                        "KHK",
                        "C-C",
                        'K', HydraulicraftItems.gasket,
                        'C', "ingotCopper",
                        'H', new ItemStack(Multipart.itemPartHose, 1, 0)
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 1, 2), true,
				new Object [] {
					"C-K",
					"-H-",
					"K-C",
					'K', HydraulicraftItems.gasket,
					'C', "ingotEnrichedCopper",
					'H', new ItemStack(Multipart.itemPartHose,1,1)
			}));
		
		*/
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 0), true,
				new Object [] {
					"LWL",
					"KCK",
					"LWL",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HydraulicraftItems.gasket,
					'L', "ingotLead",
					'C', new ItemStack(HCBlocks.blockCore, 1, 0)
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 1), true,
				new Object [] {
					"CWC",
					"KBK",
					"CVC",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HydraulicraftItems.gasket,
					'C', "ingotCopper",
					'V', new ItemStack(HCBlocks.hydraulicPressurevat, 1,0),
					'B', new ItemStack(HCBlocks.blockCore, 1, 1)
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 2), true,
				new Object [] {
					"WWW",
					"KBK",
					"WVW",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HydraulicraftItems.gasket,
					'C', "ingotEnrichedCopper",
					'V', new ItemStack(HCBlocks.hydraulicPressurevat, 1, 1),
					'B', new ItemStack(HCBlocks.blockCore, 1, 2)
			}));
		
		GameRegistry.addRecipe(new ItemStack(HCBlocks.hydraulicFrictionIncinerator, 1),
			new Object [] {
				"GKG",
				"FCF",
				"WIW",
				'W', HCBlocks.hydraulicPressureWall,
				'G', Blocks.glass,
				'F', HydraulicraftItems.itemFrictionPlate,
				'K', HydraulicraftItems.gasket,
				'C', new ItemStack(HCBlocks.blockCore, 1, 1),
				'I', HCBlocks.blockInterfaceValve
				
			});
		
		GameRegistry.addRecipe(new ItemStack(HCBlocks.hydraulicCrusher, 1),
				new Object [] {
					"-K-",
					"PCP",
					"WIW",
					'K', HydraulicraftItems.gasket,
					'P', Blocks.piston,
					'W', HCBlocks.hydraulicPressureWall,
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'I', HCBlocks.blockInterfaceValve
				});
		
		GameRegistry.addRecipe(new ItemStack(HCBlocks.hydraulicWasher, 1),
				new Object [] {
					"GKG",
					"KCG",
					"WWW",
					'K', HydraulicraftItems.gasket,
					'G', Blocks.glass,
					'W', HCBlocks.hydraulicPressureWall,
					'C', new ItemStack(HCBlocks.blockCore, 1, 2)
				});
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicLavaPump, 1, 0), true,
				new Object [] {
					"GKG",
					"ICG",
					"PWP",
					'P', Blocks.piston,
					'K', HydraulicraftItems.gasket,
					'G', Blocks.glass,
					'W', HCBlocks.hydraulicPressureWall,
					'I', HCBlocks.blockInterfaceValve,
					'C', new ItemStack(HCBlocks.blockCore, 1, 0)
				})
			);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicLavaPump,1,1), true,
				new Object [] {
					"GKG",
					"ICG",
					"PUP",
					'P', Blocks.piston,
					'K', HydraulicraftItems.gasket,
					'G', Blocks.glass,
					'I', HCBlocks.blockInterfaceValve,
					'U', new ItemStack(HCBlocks.hydraulicLavaPump, 1,0),
					'C', new ItemStack(HCBlocks.blockCore, 1, 1)
				})
			);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicLavaPump, 1, 2), true,
                new Object[]{
                        "GKG",
                        "ICG",
                        "PUP",
                        'P', Blocks.piston,
                        'K', HydraulicraftItems.gasket,
                        'G', Blocks.glass,
                        'I', HCBlocks.blockInterfaceValve,
                        'U', new ItemStack(HCBlocks.hydraulicLavaPump, 1, 1),
                        'C', new ItemStack(HCBlocks.blockCore, 1, 2)
                })
        );
		/* FMP 
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartValve, 2, 0), true,
                new Object[]{
                        "---",
                        "HLH",
                        "---",
                        'H', new ItemStack(Multipart.itemPartHose, 1, 0),
                        'L', Block.lever
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartValve, 2, 1), true,
                new Object[]{
					"---",
		            "HLH",
		            "---",
		            'H', new ItemStack(Multipart.itemPartHose, 1, 1),
		            'L', Block.lever
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartValve, 2, 2), true,
				new Object [] {
					"---",
		            "HLH",
		            "---",
		            'H', new ItemStack(Multipart.itemPartHose, 1, 2),
		            'L', Block.lever
				}));
*/
	}
	
	private static void initializeItemRecipes(){
		GameRegistry.addRecipe(new ItemStack(HydraulicraftItems.itemFrictionPlate, 1),
		new Object [] {
			"-SS",
			"S-S",
			"SS-",
			'S', Blocks.stone
		});
	
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HydraulicraftItems.ingotEnrichedCopper, 1), "gemDiamond", "ingotCopper"));

		
		
		GameRegistry.addRecipe(new ItemStack(HydraulicraftItems.gasket, 4),
			new Object [] {
				"P-P",
				"-B-",
				"P-P",
				'P', Items.paper,
				'B', Blocks.iron_bars
			}
		);
		
		
	}
	
}
