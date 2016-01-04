package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockFluidOil extends BlockBaseFluid {
    public BlockFluidOil() {

        super(Fluids.fluidOil, Names.fluidOil);

    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        //We only want to drain from the pumpjack.

        //For now, i don't care.
        return true;
    }
}
