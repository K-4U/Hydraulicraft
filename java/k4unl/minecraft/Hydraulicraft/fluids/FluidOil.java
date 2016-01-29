package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by koen_000 on 18-5-2015.
 */
public class FluidOil extends Fluid {

    public FluidOil() {
        super(Names.fluidOil.unlocalized, new ResourceLocation(ModInfo.LID, "blocks/oil_still"), new ResourceLocation(ModInfo.LID, "blocks/oil_flowing"));
        setDensity(20); //How thick the fluid is, affects movement inside the fluid.
        setViscosity(1000); // How fast the fluid flows.

        setUnlocalizedName(Names.fluidOil.unlocalized);
        FluidRegistry.registerFluid(this);
        setBlock(new BlockFluidOil(this, Names.fluidOil));

    }
}
