package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @author Koen Beckers (K-4U)
 */
public class FluidRubber extends Fluid {

    public FluidRubber() {

        super(Names.fluidRubber.unlocalized, new ResourceLocation(ModInfo.LID, "blocks/rubber_still"), new ResourceLocation(ModInfo.LID, "blocks/rubber_flowing"));
        setDensity(90000); //How thick the fluid is, affects movement inside the fluid.
        setViscosity(99999); // How fast the fluid flows.

        setUnlocalizedName(Names.fluidRubber.unlocalized);
        FluidRegistry.registerFluid(this);
        setBlock(new BlockBaseFluid(this, Names.fluidRubber));
    }
}
