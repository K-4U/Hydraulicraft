package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import java.util.ArrayList;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class TrolleyCrops implements IHarvesterTrolley{

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		return null;
	}

	@Override
	public String getName() {
		return "crops";
	}

	@Override
	public boolean canPlant(IBlockAccess world, int x, int y, int z,
			ItemStack seed) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Block getBlockForSeed(ItemStack seed) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canHarvest(IBlockAccess world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

}
