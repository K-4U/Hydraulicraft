package k4unl.minecraft.Hydraulicraft.api;

import java.util.ArrayList;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.item.ItemStack;

public interface IHarvesterTrolley {

	void doMove();

	boolean canHandleSeed(ItemStack seed);

	/**
	 * Checks whether or not this trolley can plant any of the seeds in his row
	 * @param seeds
	 * @param maxLength
	 * @return the index of the itemstack to be used. -1 if not
	 */
	int canPlantSeed(ItemStack[] seeds, int maxLength);

	void doPlant(ItemStack seed);
	
	void setPiston(TileHydraulicPiston nPiston);

	boolean isMoving();

	boolean isWorking();
	
	/**
	 * Checks whether or not a seed is fully grown here.
	 * @param maxLen
	 * @return Items that the plant would have dropped if harvested 
	 */
	ArrayList<ItemStack> checkHarvest(int maxLen);

	void doHarvest();

	void setHarvester(TileHydraulicHarvester nHarvester);

}
