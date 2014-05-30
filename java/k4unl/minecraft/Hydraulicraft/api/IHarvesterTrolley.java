package k4unl.minecraft.Hydraulicraft.api;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public interface IHarvesterTrolley {

	String getName();
	
	boolean canHarvest(IBlockAccess world, int x, int y, int z);
	
	boolean canPlant(IBlockAccess world, int x, int y, int z);
	
	ArrayList<ItemStack> getHandlingSeeds();
	
}
