package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;

import net.minecraft.item.ItemStack;

import java.util.List;


/**
 * Do NOT implement this interface!
 *
 * @author K-4U
 * @date 21-4-2014
 */
public interface IHarvester {

    void putInInventory(List<ItemStack> harvestedItems);

    void extendPistonTo(int piston, float length);

}
