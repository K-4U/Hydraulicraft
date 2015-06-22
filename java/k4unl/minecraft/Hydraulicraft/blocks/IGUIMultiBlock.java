package k4unl.minecraft.Hydraulicraft.blocks;

import net.minecraft.world.IBlockAccess;

/**
 * Created by K-4U on 22-6-2015.
 */
public interface IGUIMultiBlock {

    public boolean isValid(IBlockAccess world, int x, int y, int z);
}
