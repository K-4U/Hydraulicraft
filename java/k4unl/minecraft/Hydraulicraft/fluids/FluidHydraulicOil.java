package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHydraulicOil extends Fluid {

	public FluidHydraulicOil() {
		super(Names.fluidHydraulicOil.unlocalized);
		setDensity(10); //How thick the fluid is, affects movement inside the fluid.
		setViscosity(3000); // How fast the fluid flows.
		
		setUnlocalizedName(Names.fluidHydraulicOil.unlocalized);
		setBlock(Fluids.fluidHydraulicOilBlock);
		
		FluidRegistry.registerFluid(this);
	}
}
