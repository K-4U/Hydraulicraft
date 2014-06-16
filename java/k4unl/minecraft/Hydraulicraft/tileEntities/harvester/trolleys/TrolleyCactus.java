package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import java.util.ArrayList;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class TrolleyCactus implements IHarvesterTrolley {
	private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterCactusTrolley.png");
    
	
	@Override
	public String getName() {
		return "cactus";
	}

	@Override
	public boolean canHarvest(World world, int x, int y, int z) {
		//Check the block underneath too
		//If the block underneath is sugarcane, then harvest, otherwise, don't.
		if(world.getBlock(x, y, z) == Blocks.cactus && world.getBlock(x, y-1, z) == Blocks.cactus){
			return true;
		}
		return false;
	}

	@Override
	public boolean canPlant(World world, int x, int y, int z, ItemStack seed) {
		return Blocks.cactus.canPlaceBlockAt(world, x, y, z);
	}

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Item.getItemFromBlock(Blocks.cactus)));
		return ret;
	}

	@Override
	public Block getBlockForSeed(ItemStack seed) {
		return Blocks.cactus;
	}

	@Override
	public ResourceLocation getTexture() {
		return resLoc;
	}

	@Override
	public int getPlantHeight(World world, int x, int y, int z) {
		return 3;
	}

}
