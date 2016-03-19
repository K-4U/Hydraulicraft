package k4unl.minecraft.Hydraulicraft.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by K-4U on 22-6-2015.
 */
public interface IGUIMultiBlock {

    boolean isValid(IBlockAccess world, BlockPos pos);
}
