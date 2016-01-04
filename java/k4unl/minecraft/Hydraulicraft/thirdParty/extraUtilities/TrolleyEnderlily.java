package k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;

public class TrolleyEnderlily implements IHarvesterTrolley {
	private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterEnderLilyTrolley.png");
	
	@Override
	public String getName() {
		return "enderLily";
	}

	@Override
	public boolean canHarvest(World world, BlockPos pos) {
		return world.getBlockState(pos).getValue(BlockCrops.AGE) == 11;
	}

	@Override
	public boolean canPlant(World world, BlockPos pos, ItemStack seed) {
		IBlockState soil = world.getBlockState(pos.down());
		//TODO: PICK THE PROPER META FOR ENDER CORE
		return soil.getBlock().equals(Blocks.dirt) || soil.getBlock().equals(Blocks.grass) || soil.getBlock().equals(Blocks.end_stone) || (soil.getBlock().equals(ExtraUtilities.enderCore));// && soil. == 11);
	}

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(Item.getItemFromBlock(ExtraUtilities.enderLily)));
		return ret;
	}

	@Override
	public IBlockState getBlockStateForSeed(ItemStack seed) {
		return ExtraUtilities.enderLily.getDefaultState();
	}

	@Override
	public ResourceLocation getTexture() {
		return resLoc;
	}

	@Override
	public int getPlantHeight(World world, BlockPos pos) {
		return 1;
	}

}
