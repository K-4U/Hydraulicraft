package pet.minecraft.Hydraulicraft.fluids;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidOil extends Fluid {

	public FluidOil(String fluidName) {
		super(fluidName);
		setDensity(10); //How thick the fluid is, affects movement inside the fluid.
		setViscosity(3000); // How fast the fluid flows.
		FluidRegistry.registerFluid(this);
	}

}
