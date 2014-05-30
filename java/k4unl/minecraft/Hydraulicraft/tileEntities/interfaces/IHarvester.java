package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;


/**
 * Do NOT implement this interface!
 * @date 21-4-2014
 * @author K-4U
 *
 */
public interface IHarvester {

	void putInInventory(ArrayList<ItemStack> harvestedItems);

	void extendPistonTo(int piston, float length);
	
}
