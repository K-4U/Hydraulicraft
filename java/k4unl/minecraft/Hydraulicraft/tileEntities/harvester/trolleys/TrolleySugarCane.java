package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester.BlockHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;


public class TrolleySugarCane implements IHarvesterTrolley {
	private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterSugarCaneTrolley.png");
    
	
	@Override
	public String getName() {
		return "sugarCane";
	}

	@Override
	public boolean canHarvest(World world, int x, int y, int z) {
		//Check the block underneath too
		//If the block underneath is sugarcane, then harvest, otherwise, don't.
		if(world.getBlock(x, y, z) == Blocks.reeds && world.getBlock(x, y-1, z) == Blocks.reeds){
			return true;
		}
		return false;
	}

	@Override
	public boolean canPlant(World world, int x, int y, int z, ItemStack seed) {
		return Blocks.reeds.canPlaceBlockAt(world, x, y, z);
	}

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Items.reeds));
		return ret;
	}

	@Override
	public Block getBlockForSeed(ItemStack seed) {
		return Blocks.reeds;
	}

	@Override
	public ResourceLocation getTexture() {
		return resLoc;
	}

	@Override
	public int getPlantHeight(World world, int x, int y, int z) {
		y = y + 3;
		if(world.getBlock(x, y, z).getMaterial() == Material.air || world.getBlock(x, y, z) instanceof BlockHarvesterTrolley) {
			return 2;
		}else{
			return 3;
		}
	}

}
