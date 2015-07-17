package k4unl.minecraft.Hydraulicraft.thirdParty.igwmod;

import codechicken.nei.NEIClientUtils;
import igwmod.TextureSupplier;
import igwmod.WikiUtils;
import igwmod.api.IRecipeIntegrator;
import igwmod.gui.*;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import net.minecraft.item.ItemStack;

import java.util.List;

public class IntegratorAssembler implements IRecipeIntegrator {
    @Override
    public String getCommandKey() {
        return "assembler";
    }

    @Override
    public void onCommandInvoke(String[] arguments, List<IReservedSpace> reservedSpaces, List<LocatedString> locatedStrings, List<LocatedStack> locatedStacks, List<IWidget> locatedTextures) throws IllegalArgumentException {
        if(arguments.length != 3) throw new IllegalArgumentException("Code needs 3 arguments!");
        int x;
        try {
            x = Integer.parseInt(arguments[0]);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("The first parameter (the x coordinate) contains an invalid number. Check for invalid characters!");
        }
        int y;
        try {
            y = Integer.parseInt(arguments[1]);
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("The second parameter (the y coordinate) contains an invalid number. Check for invalid characters!");
        }
        locatedTextures.add(new LocatedTexture(TextureSupplier.getTexture(ModInfo.LID + ":textures/wiki/guiAssembler.png"), x, y, 1 / GuiWiki.TEXT_SCALE));

        IFluidRecipe foundRecipe = findRecipe(HydraulicRecipes.getAssemblerRecipes(), arguments[2]);


        //locatedStacks.add(new LocatedStack(foundRecipe.getInputItems(), (int)(GuiWiki.TEXT_SCALE * x) + 1, (int)(GuiWiki.TEXT_SCALE * y) + 46));
        locatedStacks.add(new LocatedStack(foundRecipe.getRecipeOutput(), (int)(GuiWiki.TEXT_SCALE * x) + 68, (int)(GuiWiki.TEXT_SCALE * y) + 46));
        //Tell fluids..
    }

    public boolean areItemStacksEqual(ItemStack a, ItemStack b, boolean matchNBT) {
        boolean tmp = NEIClientUtils.areStacksSameTypeCrafting(a, b);
        if (!matchNBT || !tmp)
            return tmp;

        if (a.getTagCompound() == null)
            return b.getTagCompound() == null;

        return a.getTagCompound().equals(b.getTagCompound());
    }

    public IFluidRecipe findRecipe(List<IFluidRecipe> recipes, String search){
        for(IFluidRecipe recipe : recipes) {
            if(WikiUtils.getNameFromStack(recipe.getRecipeOutput()).equals(search)) {
                return recipe;
            }
        }
        return null;
    }
}
