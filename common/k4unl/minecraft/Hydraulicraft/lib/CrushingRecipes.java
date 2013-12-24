package k4unl.minecraft.Hydraulicraft.lib;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CrushingRecipes {

    public static class CrushingRecipe {
        public final ItemStack input;
        public final ItemStack output;
        public final float pressure;

        public CrushingRecipe(ItemStack inp, float press, ItemStack outp){
            input = inp;
            output = outp;
            pressure = press;
        }
    }

    public static LinkedList<CrushingRecipe> crushingRecipes = new
            LinkedList<CrushingRecipe>();

    public static void addCrushingRecipe(CrushingRecipe toAdd){
        crushingRecipes.add(toAdd);
    }

    public static ItemStack getCrushingRecipe(ItemStack itemStack){
        for(CrushingRecipe rec : crushingRecipes){
            if(rec.input.isItemEqual(itemStack)){
                return rec.output;
            }
        }
        return null;
    }

/*
    public ItemStack getCrushingRecipe(ItemStack itemStack){
        ItemStack ret = null;

        List<String> allowedList = new ArrayList<String>();
        allowedList.add("Gold");
        allowedList.add("Iron");
        allowedList.add("Copper");
        allowedList.add("Lead");
        allowedList.add("Quartz");

        //Get oreDictionaryName
        String oreName = itemStack.getUnlocalizedName();
        oreName = oreName.substring("tile.".length());
        String metalName = Functions.getMetalName(oreName);
        if(allowedList.contains(metalName)){
            for(int i = 0; i < chunks.size(); i++){
                String cName = chunks.get(i).getName();
                if(cName.equals(metalName)){
                    if(metalName.equals("Quartz")){
                        return new ItemStack(Item.netherQuartz,3 + ((new Random()).nextFloat() > 0.85F ? 1 : 0));
                    }else{
                        return new ItemStack(this.itemID, 2, i);
                    }
                }
            }
        }

        return ret;
    }*/

}
