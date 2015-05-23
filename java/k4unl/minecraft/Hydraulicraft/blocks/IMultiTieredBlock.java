package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;

public interface IMultiTieredBlock {

    public PressureTier getTier(int metadata);
}
