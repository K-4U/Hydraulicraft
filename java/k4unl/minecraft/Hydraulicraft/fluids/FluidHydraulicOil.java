package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidHydraulicOil extends Fluid {

	public FluidHydraulicOil() {
		super(Names.fluidHydraulicOil.unlocalized, new ResourceLocation(ModInfo.LID, "blocks/hydraulicOil_still"), new ResourceLocation(ModInfo.LID, "blocks/hydraulicOil_flowing"));
		setDensity(10); //How thick the fluid is, affects movement inside the fluid.
		setViscosity(3000); // How fast the fluid flows.
		
		setUnlocalizedName(Names.fluidHydraulicOil.unlocalized);
		FluidRegistry.registerFluid(this);
		setBlock(new BlockBaseFluid(this, Names.fluidHydraulicOil));

	}
}
