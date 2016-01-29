package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidOil extends BlockBaseFluid {


    public BlockFluidOil(Fluid fluid, Name name) {

        super(fluid, name);
    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        //We only want to drain from the pumpjack.

        //For now, i don't care.
        return true;
    }
}
