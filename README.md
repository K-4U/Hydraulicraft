Hydraulicraft
=============

Not much to say about the mod here. Sorry, i've never been good at writing stuff.

Mod devs: If you want to use my API. Go ahead.

API for 1.6.4 is a beast. API for 1.7.2 is fine to use and will probably not change much.

## IMC ##
Just an example on how to use IMC to register crushing recipes:

	NBTTagCompound toRegister = new NBTTagCompound();
    ItemStack beginStack = new ItemStack(Blocks.sand, 1);
    ItemStack endStack = new ItemStack(Items.diamond, 10);
    NBTTagCompound itemFrom = new NBTTagCompound();
	NBTTagCompound itemTo = new NBTTagCompound();

    beginStack.writeToNBT(itemFrom);  
    endStack.writeToNBT(itemTo);

    toRegister.setTag("itemFrom", itemFrom);
    toRegister.setTag("itemTo", itemTo);
    toRegister.setFloat("pressureRatio", 1.0F);
    FMLInterModComms.sendMessage("HydCraft", "registerCrushingRecipe", toRegister);

Where itemFrom and itemTo are ItemStacks.
The pressureRatio is how much pressure it should use. 1 is the normal amount of pressure. I use 1.2 for diamonds. So, the harder your material is, the higher you make this (Although i do not recommend going above 3!)
You can also use registerWashingRecipe, with the exact same arguments.
