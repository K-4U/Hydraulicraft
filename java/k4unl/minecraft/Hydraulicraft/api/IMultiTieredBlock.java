package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IMultiTieredBlock {

    /**
     * Only used from items
     * @param damage
     * @return
     */
    PressureTier getTier(int damage);

    /**
     * Used to get the tier in world.
     * @param world
     * @param pos
     * @return
     */
    PressureTier getTier(IBlockAccess world, BlockPos pos);
}
