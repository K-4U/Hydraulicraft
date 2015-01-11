package k4unl.minecraft.Hydraulicraft.lib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.multipart.Multipart;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

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
        crushableItems.add("Ardite");
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
            	int metaId = HCItems.itemChunk.addChunk(item);
                HCItems.itemDust.addDust(item, metaId);
                
		        CrushingRecipes.addCrushingRecipe(new CrushingRecipes
		                .CrushingRecipe
		                (oreName, 1.0F, new ItemStack(HCItems.itemChunk, 2, metaId)));
		        
		        
		        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe
		                (ingotName, 0.5F,
		                        new ItemStack(HCItems.itemDust, 1, metaId)));
		
		        
		        WashingRecipes.addWashingRecipe(new WashingRecipes.WashingRecipe(
		               new ItemStack(HCItems.itemChunk, 1, metaId), 400F,
		                new ItemStack(HCItems.itemDust, 1, metaId)));
            }
        }
        
        //Other mods. Stuff that doesn't follow the ingot stuff.
        if(Loader.isModLoaded("IC2")){
        	registerNonStandardCrushRecipe("oreUranium", "crushedUranium", 2);
        }
        
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Blocks.quartz_ore, 1), 1.0F, new ItemStack(Items.quartz, 3)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Blocks.cobblestone, 1), 0.9F, new ItemStack(Blocks.sand, 2)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Items.bone, 1), 0.5F, new ItemStack(Items.dye, 5, 15)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Items.blaze_rod, 1), 0.5F, new ItemStack(Items.blaze_powder, 5)));
        CrushingRecipes.addCrushingRecipe(new CrushingRecipes.CrushingRecipe(new ItemStack(Items.diamond), 1.2F, new ItemStack(HCItems.itemDiamondShards, 9)));
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
		GameRegistry.addSmelting(Ores.oreCopper, new ItemStack(HCItems.ingotCopper), 0);
		GameRegistry.addSmelting(Ores.oreLead, new ItemStack(HCItems.ingotLead), 0);
	}
	
	private static void initializeBlockRecipes(){
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockValve, 4, 0), true ,
				new Object[] {
					"WKW",
					"K K",
					"WKW",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HCItems.gasket
			})
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockInterfaceValve, 4, 0), true ,
				new Object[] {
					"WHW",
					"K K",
					"WHW",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HCItems.gasket,
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
					'K', HCItems.gasket,
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
					'K', HCItems.gasket,
					'I', HCBlocks.blockInterfaceValve
			})
		);
		
		ItemStack cropsTrolly = Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem("crops");
		cropsTrolly.stackSize = 4;
		
		GameRegistry.addRecipe(new ShapedOreRecipe(cropsTrolly, true ,
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
		
		ItemStack sugarTrolly = Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem("sugarCane");
		sugarTrolly.stackSize = 4;
		
		GameRegistry.addRecipe(new ShapedOreRecipe(sugarTrolly, true ,
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
		
		ItemStack cactusTrolly = Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem("cactus");
		cactusTrolly.stackSize = 4;
		
		GameRegistry.addRecipe(new ShapedOreRecipe(cactusTrolly, true ,
				new Object[] {
					"-P-",
					"WCW",
					"-S-",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'S', Items.golden_sword,
					'P', HCBlocks.hydraulicPiston
			})
		);
		
		ItemStack netherWartTrolly = Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem("netherWart");
		netherWartTrolly.stackSize = 2;
		
		GameRegistry.addRecipe(new ShapedOreRecipe(netherWartTrolly, true ,
				new Object[] {
					"-P-",
					"WCW",
					"-S-",
					'C', new ItemStack(HCBlocks.blockCore, 1, 1),
					'W', HCBlocks.hydraulicPressureWall,
					'S', Items.ghast_tear,
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
				'K', HCItems.gasket,
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
					'K', HCItems.gasket,
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
                        'K', HCItems.gasket,
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
                        'K', HCItems.gasket,
                        'G', Blocks.glass,
                        'W', HCBlocks.hydraulicPressureWall,
                        'C', new ItemStack(HCBlocks.blockCore, 1, 2),
                        'I', HCBlocks.blockInterfaceValve
                });

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 4, 0), true,
                new Object[]{
                        "LLL",
                        "K-K",
                        "LLL",
                        'K', HCItems.gasket,
                        'L', "ingotLead"
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 1, 1), true,
                new Object[]{
                        "C-C",
                        "KHK",
                        "C-C",
                        'K', HCItems.gasket,
                        'C', "ingotCopper",
                        'H', new ItemStack(Multipart.itemPartHose, 1, 0)
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartHose, 1, 2), true,
				new Object [] {
					"C-K",
					"-H-",
					"K-C",
					'K', HCItems.gasket,
					'C', "ingotEnrichedCopper",
					'H', new ItemStack(Multipart.itemPartHose,1,1)
			}));
		
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 0), true,
				new Object [] {
					"LWL",
					"KCK",
					"LWL",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HCItems.gasket,
					'L', "ingotLead",
					'C', new ItemStack(HCBlocks.blockCore, 1, 0)
			}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressurevat, 1, 1), true,
				new Object [] {
					"CWC",
					"KBK",
					"CVC",
					'W', HCBlocks.hydraulicPressureWall,
					'K', HCItems.gasket,
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
					'K', HCItems.gasket,
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
				'F', HCItems.itemFrictionPlate,
				'K', HCItems.gasket,
				'C', new ItemStack(HCBlocks.blockCore, 1, 1),
				'I', HCBlocks.blockInterfaceValve
				
			});
		
		GameRegistry.addRecipe(new ItemStack(HCBlocks.hydraulicCrusher, 1),
				new Object [] {
					"-K-",
					"PCP",
					"WIW",
					'K', HCItems.gasket,
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
					'K', HCItems.gasket,
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
					'K', HCItems.gasket,
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
					'K', HCItems.gasket,
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
                        'K', HCItems.gasket,
                        'G', Blocks.glass,
                        'I', HCBlocks.blockInterfaceValve,
                        'U', new ItemStack(HCBlocks.hydraulicLavaPump, 1, 1),
                        'C', new ItemStack(HCBlocks.blockCore, 1, 2)
                })
        );
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartValve, 2, 0), true,
                new Object[]{
                        "---",
                        "HLH",
                        "---",
                        'H', new ItemStack(Multipart.itemPartHose, 1, 0),
                        'L', Blocks.lever
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartValve, 2, 1), true,
                new Object[]{
					"---",
		            "HLH",
		            "---",
		            'H', new ItemStack(Multipart.itemPartHose, 1, 1),
		            'L', Blocks.lever
                }));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Multipart.itemPartValve, 2, 2), true,
				new Object [] {
					"---",
		            "HLH",
		            "---",
		            'H', new ItemStack(Multipart.itemPartHose, 1, 2),
		            'L', Blocks.lever
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockHydraulicWaterPump, 1), true,
				new Object [] {
					"-P-",
		            "GCG",
		            "WWW",
		            'W', HCBlocks.hydraulicPressureWall,
		            'C', new ItemStack(HCBlocks.blockCore, 1, 0),
		            'P', Blocks.piston,
		            'G', HCItems.gasket
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.hydraulicPressureGlass, 8), true,
				new Object [] {
					"GWG",
		            "GLG",
		            "GWG",
		            'W', HCBlocks.hydraulicPressureWall,
		            'L', "ingotLead",
		            'G', Blocks.glass
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockCopper, 1), true,
				new Object [] {
					"CCC",
		            "CCC",
		            "CCC",
		            'C', "ingotCopper"
				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.blockLead, 1), true,
				new Object [] {
					"LLL",
		            "LLL",
		            "LLL",
		            'L', "ingotLead"
				}));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.ingotCopper, 9), "blockCopper"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.ingotLead, 9), "blockLead"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemMovingPane, 2), true,
				new Object [] {
					"GGG",
		            "GCG",
		            "WKW",
		            'W', HCBlocks.hydraulicPressureWall,
		            'C', new ItemStack(HCBlocks.blockCore, 1, 2),
		            'K', HCItems.gasket,
		            'G', Blocks.glass_pane
				}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.portalBase, 1), true,
				new Object[] {
			"III",
			"ECE",
			"IGI",
			'I', Items.iron_ingot,
			'E', Items.ender_pearl,
			'C', new ItemStack(HCBlocks.blockCore, 1, 2),
			'G', HCItems.gasket
		}));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCBlocks.portalFrame, 6), true,
				new Object[] {
			"III",
			"IEI",
			"III",
			'I', Items.iron_ingot,
			'E', Items.ender_pearl
		}));
		
		

	}
	
	private static void initializeItemRecipes(){
		GameRegistry.addRecipe(new ItemStack(HCItems.itemFrictionPlate, 1),
		new Object [] {
			"-SS",
			"S-S",
			"SS-",
			'S', Blocks.stone
		});
	
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.itemEnrichedCopperDust, 2), "diamondShard", "dustCopper"));

		
		
		GameRegistry.addRecipe(new ItemStack(HCItems.gasket, 4),
			new Object [] {
				"P-P",
				"-B-",
				"P-P",
				'P', Items.paper,
				'B', Blocks.iron_bars
			}
		);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemLamp, 1), true,
				new Object [] {
					"-G-",
					"GTG",
					"-G-",
					'G', Blocks.glass,
					'T', Blocks.torch
			}));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(HCItems.itemMiningHelmet, 1), HCItems.itemLamp, Items.iron_helmet));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemIPCard, 1), true,
				new Object[] {
			"ROO",
			"RWI",
			"RII",
			'I', Items.iron_ingot,
			'R', Items.redstone,
			'W', new ItemStack(Blocks.wool, 1, 15),
			'O', new ItemStack(Blocks.wool, 1, 14)
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemEnderLolly, 4), true,
				new Object[] {
						"E",
						"S",
						'E', Items.ender_pearl,
						'S', Items.stick
				}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HCItems.itemJarDirt, 1), true,
				new Object[] {
						"-S-",
						"GDG",
						"GGG",
						'G', Blocks.glass,
						'D', Blocks.dirt,
						'S', "slabWood"
				}));

	}
	
}
