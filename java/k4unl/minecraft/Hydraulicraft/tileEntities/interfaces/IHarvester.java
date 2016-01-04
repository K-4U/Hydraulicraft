package k4unl.minecraft.Hydraulicraft.tileEntities.interfaces;

import net.minecraft.item.ItemStack;

import java.util.List;


/**
 * Do NOT implement this interface!
 * @date 21-4-2014
 * @author K-4U
 *
 */
public interface IHarvester {

	void putInInventory(List<ItemStack> harvestedItems);

	void extendPistonTo(int piston, float length);
	
}
