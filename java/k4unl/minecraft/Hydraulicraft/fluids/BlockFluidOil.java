package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.world.World;

public class BlockFluidOil extends BlockBaseFluid {
    public BlockFluidOil() {

        super(Fluids.fluidOil, Names.fluidOil);

    }

    @Override
    public boolean canDrain(World world, int x, int y, int z) {
        //We only want to drain from the pumpjack.
        return false;
    }
}
