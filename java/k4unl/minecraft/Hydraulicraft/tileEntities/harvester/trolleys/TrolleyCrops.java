package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

public class TrolleyCrops implements IHarvesterTrolley{

	@Override
	public boolean canHarvest(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean canPlant(IBlockAccess world, int x, int y, int z) {
		return false;
	}

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		return null;
	}

	@Override
	public String getName() {
		return Names.blockHarvesterTrolley[0].unlocalized;
	}

}
