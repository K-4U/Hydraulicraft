package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by K-4U on 31-5-2015.
 */
public class FluidLubricant extends Fluid {
    public FluidLubricant() {
        super(Names.fluidLubricant.unlocalized);
        setDensity(900); //How thick the fluid is, affects movement inside the fluid.
        setViscosity(9999); // How fast the fluid flows.

        setUnlocalizedName(Names.fluidLubricant.unlocalized);
        setBlock(Fluids.fluidLubricantBlock);

        FluidRegistry.registerFluid(this);
    }
}
