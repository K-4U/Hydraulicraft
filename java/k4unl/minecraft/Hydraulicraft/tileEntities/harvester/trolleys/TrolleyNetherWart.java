package k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys;

import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;

public class TrolleyNetherWart implements IHarvesterTrolley {
    private static final ResourceLocation resLoc =
            new ResourceLocation(ModInfo.LID, "textures/model/harvesterNetherTrolley.png");

    @Override
    public String getName() {
        return "netherWart";
    }

    @Override
    public boolean canPlant(World world, BlockPos pos, ItemStack seed) {
        if (seed.getItem() instanceof IPlantable) {
            Block soil = world.getBlockState(pos.down()).getBlock();
            return (world.getLight(pos) >= 8 ||
                    world.canBlockSeeSky(pos)) &&
                    soil != null && soil.canSustainPlant(world, pos.down(),
                    EnumFacing.UP, (IPlantable) seed.getItem());
        } else {
            return false;
        }
    }

    @Override
    public ArrayList<ItemStack> getHandlingSeeds() {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(Items.nether_wart));
        return ret;
    }

    @Override
    public IBlockState getBlockStateForSeed(ItemStack seed) {
        IPlantable plantingItem = (IPlantable) seed.getItem();
        return plantingItem.getPlant(null, null);
    }

    @Override
    public boolean canHarvest(World world, BlockPos pos) {
        for (ItemStack s : getHandlingSeeds()) {
            if (world.getBlockState(pos).equals(getBlockStateForSeed(s))) {
                if (world.getBlockState(pos).getValue(BlockNetherWart.AGE) == 3) {
                    return true;
                }
            }
        }

        return false;
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
