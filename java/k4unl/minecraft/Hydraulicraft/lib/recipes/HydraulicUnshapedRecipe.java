package k4unl.minecraft.Hydraulicraft.lib.recipes;

import com.google.common.collect.Iterators;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * UNSHAPED internal machine recipes!
 */
public class HydraulicUnshapedRecipe {
    private FluidStack[]      inputFluids;
    private ItemStack[]       inputItemStacks;
    private String[]          inputItemOreDict;
    private List<ItemStack>[] inputOreDictParsed;
    private Object[]          outputs;

    public HydraulicUnshapedRecipe(Object[] inputs, Object[] outputs) {
        parseInputs(inputs);
        this.outputs = outputs;
    }

    /**
     * Parses input just to speed up recipe checking
     *
     * @param inputs Object array of inputs
     */
    private void parseInputs(Object[] inputs) {
        List<FluidStack> fluids = new ArrayList<FluidStack>();
        List<ItemStack> items = new ArrayList<ItemStack>();
        List<String> oredicts = new ArrayList<String>();

        for (Object input : inputs) {
            if (input instanceof FluidStack) {
                fluids.add((FluidStack) input);
            } else if (input instanceof ItemStack) {
                items.add((ItemStack) input);
            } else if (input instanceof String) {
                oredicts.add((String) input);
            } else {
                Log.info("Unknown thing in a recipe: " + input.toString() + " -- skipping!");
            }
        }

        inputFluids = fluids.toArray(new FluidStack[fluids.size()]);
        inputItemStacks = items.toArray(new ItemStack[items.size()]);
        inputItemOreDict = oredicts.toArray(new String[oredicts.size()]);

        parseOreDicts();
    }

    private void parseOreDicts() {
        inputOreDictParsed = new List[inputItemOreDict.length];
        for (int i = 0; i < inputItemOreDict.length; i++)
            inputOreDictParsed[i] = OreDictionary.getOres(inputItemOreDict[i]);
    }

    public boolean matches(Object[] recipeInput) {
        Iterator<FluidStack> fluids = Iterators.forArray(inputFluids.clone());
        Iterator<ItemStack> items = Iterators.forArray(inputItemStacks.clone());
        Iterator<List<ItemStack>> itemOreDicts = Iterators.forArray(inputOreDictParsed.clone());

        int matchedItems = 0;
        int prevMatchedItems = 0;
        for (Object input : recipeInput) {
            prevMatchedItems = matchedItems;

            if (input instanceof ItemStack) { // ItemStack
                ItemStack is = (ItemStack) input;
                // check item first as it is faster
                while (items.hasNext()) {
                    if (items.next().equals(is)) {
                        items.remove();
                        matchedItems++;
                        break;
                    }
                }

                if (matchedItems == prevMatchedItems) { // was not caught as an item thing
                    while (itemOreDicts.hasNext()) {
                        for (ItemStack istack : itemOreDicts.next()) {
                            if (istack.equals(is)) { // item equals
                                matchedItems++;
                                break; // but we cannot modify the iterator here because concurrentmodification
                            }
                        }
                        if (matchedItems != prevMatchedItems) { // we've found a match in the for
                            itemOreDicts.remove();
                            break;
                        }
                    }
                }

            } else if (input instanceof FluidStack) { // FluidStack
                FluidStack fs = (FluidStack) input;

                while (fluids.hasNext()) {
                    if (fluids.next().equals(fs)) {
                        fluids.remove();
                        matchedItems++;
                        break;
                    }
                }
            }

            if (matchedItems == prevMatchedItems)
                return false; // current item was not matched, we can just skip it
        }

        // will fail in case there were more inputs than required
        return (matchedItems == (inputFluids.length + inputItemStacks.length + inputItemOreDict.length));
    }

    /**
     * Returns outputs (items, fluids) for a recipe
     *
     * @return the outputs
     */
    public Object[] getOutputs() {
        return outputs;
    }

    /**
     * Gives back fluids used for a recipe (used for decreasing internal tanks etc)
     *
     * @return input fluids
     */
    public FluidStack[] getInputFluids() {
        return inputFluids;
    }
}
