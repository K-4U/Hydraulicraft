package k4unl.minecraft.Hydraulicraft.thirdParty.bluepower;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class TrolleyFlax implements IHarvesterTrolley {
	private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID,"textures/model/harvesterFlax.png");
	
	@Override
	public String getName() {
		return "flax";
	}

	@Override
	public boolean canHarvest(World world, int x, int y, int z) {
        if(world.getBlock(x, y, z) == BluePower.flaxBlock && world.getBlock(x, y-1, z) == BluePower.flaxBlock) {
            return true;
        }else {
            return false;
        }
		//return world.getBlockMetadata(x, y, z) == 7;
	}

	@Override
	public boolean canPlant(World world, int x, int y, int z, ItemStack seed) {
		Block soil = world.getBlock(x, y-1, z);
		return (soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (IPlantable) BluePower.flaxItem) && world.isAirBlock(x, y, z)
                && (soil.isFertile(world, x, y - 1, z)));
	}

	@Override
	public ArrayList<ItemStack> getHandlingSeeds() {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(BluePower.flaxItem));
		return ret;
	}

	@Override
	public Block getBlockForSeed(ItemStack seed) {
		return BluePower.flaxBlock;
	}

	@Override
	public ResourceLocation getTexture() {
		return resLoc;
	}

	@Override
	public int getPlantHeight(World world, int x, int y, int z) {
		return 2;
	}

}
