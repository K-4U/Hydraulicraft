package k4unl.minecraft.Hydraulicraft.thirdParty.bluepower;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;

public class TrolleyFlax implements IHarvesterTrolley {

    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/harvesterFlax.png");

    @Override
    public String getName() {

        return "flax";
    }

    @Override
    public boolean canHarvest(World world, BlockPos pos) {

        return world.getBlockState(pos).getBlock() == BluePower.flaxBlock && world.getBlockState(pos.down()).getBlock() == BluePower.flaxBlock;
        //return world.getBlockMetadata(x, y, z) == 7;
    }

    @Override
    public boolean canPlant(World world, BlockPos pos, ItemStack seed) {

        Block soil = world.getBlockState(pos.down()).getBlock();
        return (soil.canSustainPlant(world.getBlockState(pos), world, pos.down(), EnumFacing.UP, (IPlantable) BluePower.flaxItem) && world.isAirBlock(pos)
                && (soil.isFertile(world, pos.down())));
    }

    @Override
    public ArrayList<ItemStack> getHandlingSeeds() {

        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(BluePower.flaxItem));
        return ret;
    }

    @Override
    public IBlockState getBlockStateForSeed(ItemStack seed) {

        return BluePower.flaxBlock.getDefaultState();
    }

    @Override
    public ResourceLocation getTexture() {

        return resLoc;
    }

    @Override
    public int getPlantHeight(World world, BlockPos pos) {

        return 2;
    }

}
