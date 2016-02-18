package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @author Koen Beckers (K-4U)
 */
public class FluidClayWater extends Fluid {


    public FluidClayWater() {

        super(Names.fluidClayWater.unlocalized, new ResourceLocation(ModInfo.LID, "blocks/fluidClayWater_still"), new ResourceLocation(ModInfo.LID, "blocks/fluidClayWater_flowing"));
        setDensity(900); //How thick the fluid is, affects movement inside the fluid.
        setViscosity(999); // How fast the fluid flows.

        setUnlocalizedName(Names.fluidClayWater.unlocalized);
        FluidRegistry.registerFluid(this);
        setBlock(new BlockBaseFluid(this, Names.fluidClayWater));
    }
}
