Hydraulicraft
=============

Not much to say about the mod here. Sorry, i've never been good at writing stuff.

If you have bugs, please report them in the [issue tracker](https://github.com/K-4U/Hydraulicraft/issues). Please look in the closed issues, or other issues before opening a new one! Somebody may have had the same bug you have!

Also, if you want to contact me, come join me on IRC: #Hydraulicraft on esper.net

## Using my API ##

Mod devs: Using my mod is easier than ever thanks to maven:

	repositories {
	    maven {
	        name = "MM repo"
	        url = "http://maven.k-4u.nl/"
	    }
	}

	dependencies {
		#Either just use the API
		compile "k4unl:HydCraft:1.7.10-2.1.119:api"

		#Or use the deobf version
		compile "k4unl:HydCraft:1.7.10-2.1.119:deobf"
	}


## Registring recipes ##

in [`HCApi`](https://github.com/K-4U/Hydraulicraft/blob/master/java/k4unl/minecraft/Hydraulicraft/api/HCApi.java) there is a `getRecipeHandler()` method. This returns an instance of [`IRecipeHandler`](https://github.com/K-4U/Hydraulicraft/blob/master/java/k4unl/minecraft/Hydraulicraft/api/recipes/IRecipeHandler.java).

You can then use a [`FluidShapedOreRecipe`](https://github.com/K-4U/Hydraulicraft/blob/master/java/k4unl/minecraft/Hydraulicraft/api/recipes/FluidShapedOreRecipe.java) or [`FluidShapelessOreRecipe`](https://github.com/K-4U/Hydraulicraft/blob/master/java/k4unl/minecraft/Hydraulicraft/api/recipes/FluidShapelessOreRecipe.java) to register recipes. For example:

**Adding a recipe to the assembler**

	recipeHandler.addAssemblerRecipe(new FluidShapedOreRecipe(new ItemStack(HCBlocks.hydraulicHarvesterSource, 1, 0), true,
		new Object[]{
			"WWW",
			"ICK",
			"WWW",
			'C', new ItemStack(HCBlocks.blockCore, 1, 1),
			'W', HCBlocks.hydraulicPressureWall,
			'K', HCItems.gasket,
			'I', HCBlocks.blockInterfaceValve
		}).addFluidInput(new FluidStack(Fluids.fluidLubricant, 100))
	);    

**Adding a recipe for the crusher**

	recipeHandler.addCrushingRecipe(new FluidShapelessOreRecipe(new ItemStack(Blocks.sand, 2), Blocks.cobblestone).setPressure(0.9F));


The pressure is how much pressure it should use. 1 is the normal amount of pressure. I use 1.2 for diamonds. So, the harder your material is, the higher you make this (Although i do not recommend going above 3!)


## Registring trolleys ##

Make a normal class and implement [`IHarvesterTrolley`](https://github.com/K-4U/Hydraulicraft/blob/master/java/k4unl/minecraft/Hydraulicraft/api/IHarvesterTrolley.java) (example: [Flax trolley](https://github.com/K-4U/Hydraulicraft/blob/master/java/k4unl/minecraft/Hydraulicraft/thirdParty/bluepower/TrolleyFlax.java))

Then, register it in the Trolley Registrar, of which you can get an instance of through HCApi.
