package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by K-4U on 31-5-2015.
 */
public class FluidLubricant extends Fluid {

    public FluidLubricant() {

        super(Names.fluidLubricant.unlocalized, new ResourceLocation(ModInfo.LID, "blocks/lubricant_still"), new ResourceLocation(ModInfo.LID, "blocks/lubricant_flowing"));
        setDensity(900); //How thick the fluid is, affects movement inside the fluid.
        setViscosity(9999); // How fast the fluid flows.

        setUnlocalizedName(Names.fluidLubricant.unlocalized);
        FluidRegistry.registerFluid(this);
        setBlock(new BlockBaseFluid(this, Names.fluidLubricant));

    }
}
