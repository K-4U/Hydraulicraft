package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.world.IBlockAccess;

public interface IMultiTieredBlock {

    public PressureTier getTier(int metadata);

    public PressureTier getTier(IBlockAccess world, int x, int y, int z);
}
