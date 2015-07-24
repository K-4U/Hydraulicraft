package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChancedStack {
    private List<ChancedDropStack> chancedStacks;

    /**
     * Chanced stack: when you want to configure random drop chance in a completely custom and any way!
     *
     * @param params a mix of itemstacks and floats - each itemstack is a delimiter and each
     *               following float is added to its chance to drop
     */
    public ChancedStack(Object... params) {
        chancedStacks = new ArrayList<ChancedDropStack>();

        ChancedDropStack stack = null;

        for (Object par : params) {
            if (par instanceof ItemStack) {
                if (stack == null)
                    stack = new ChancedDropStack();
                else {
                    if (stack.chances.size() == 0)
                        throw new IllegalArgumentException("Expected at least one chance to drop an itemstack!");

                    chancedStacks.add(stack);
                    stack = new ChancedDropStack();
                }
                stack.itemStack = (ItemStack) par;
            } else if (par instanceof Float) {
                if (stack == null)
                    throw new IllegalArgumentException("Expected an itemstack, received Float instead!");

                stack.chances.add((Float) par);
            }
        }

        if (stack == null)
            throw new IllegalArgumentException("Received exactly 0 valid items in this chanced stack drop!");

        if (stack.chances.size() == 0)
            throw new IllegalArgumentException("Expected at least one chance to drop an itemstack!");

        chancedStacks.add(stack);
    }

    /**
     * Calculates all the drops for the given Chanced Stack
     *
     * @param random random (seed) if possible
     * @return itemstacks to drop/give out/dispense
     */
    public ItemStack[] getDrops(Random random) {
        List<ItemStack> drops = new ArrayList<ItemStack>();

        for (ChancedDropStack chancedStack : chancedStacks) {
            int tmpDrops = 0;

            // for each possible chance
            for (int j = 0; j < chancedStack.chances.size(); j++) {
                float nextone = random.nextFloat();
                if (nextone >= chancedStack.chances.get(j)) // if we got that chance
                    tmpDrops++; // drop it
            }

            // if any drops are to happen
            if (tmpDrops > 0) {
                ItemStack toAdd = chancedStack.itemStack.copy(); // copy the stack
                toAdd.stackSize = tmpDrops; // set the new stacksize
                drops.add(toAdd);
            }
        }

        return drops.toArray(new ItemStack[drops.size()]);
    }


    private class ChancedDropStack {
        public ItemStack   itemStack;
        public List<Float> chances;

        public ChancedDropStack() {
            chances = new ArrayList<Float>();
        }
    }
}
