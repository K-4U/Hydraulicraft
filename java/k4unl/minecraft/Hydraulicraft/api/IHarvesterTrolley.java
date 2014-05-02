package k4unl.minecraft.Hydraulicraft.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public interface IHarvesterTrolley {

	/**
	 * Use this function to update position
	 */
	void doMove();

	/**
	 * Check if this harvester trolley can actually handle the seed
	 * @param seed
	 * @return
	 */
	boolean canHandleSeed(ItemStack seed);

	/**
	 * Checks whether or not this trolley can plant any of the seeds in his row
	 * This should check for a suitable soil.
	 * @param seeds
	 * @param maxLength
	 * @return the index of the itemstack to be used. -1 if not
	 */
	int canPlantSeed(ItemStack[] seeds, int maxLength);

	/**
	 * Actually do the planting
	 * @param seed The seed to plant.
	 */
	void doPlant(ItemStack seed);

	/**
	 * Returns if the trolley is moving yes or no.
	 * @return
	 */
	boolean isMoving();

	/**
	 * Returns if the trolley is working.
	 * @return
	 */
	boolean isWorking();
	
	/**
	 * Checks whether or not a seed is fully grown here.
	 * @param maxLen
	 * @return Items that the plant would have dropped if harvested 
	 */
	ArrayList<ItemStack> checkHarvest(int maxLen);

	/**
	 * Call to actually harvest the block.
	 */
	void doHarvest();

	/**
	 * Used to set the harvester main object.
	 * @param nHarvester
	 */
	void setHarvester(IHarvester nHarvester, int harvesterIndex);

	/**
	 * Returns how far the trolley is extended sideways
	 * @return
	 */
	float getSideLength();

	/**
	 * If the harvester is fully formed, this function gets called to indicate it's part of a harvester.
	 * @param b
	 */
	void setIsHarvesterPart(boolean b);

}
