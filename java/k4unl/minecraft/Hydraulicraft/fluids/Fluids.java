package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.registry.GameRegistry;

public class Fluids {
    public static Fluid fluidHydraulicOil;
    public static Block fluidHydraulicOilBlock;

    public static Fluid fluidOil;
    public static Block fluidOilBlock;

    public static Fluid fluidLubricant;
    public static Block fluidLubricantBlock;


    public static void init() {

        fluidHydraulicOil = new FluidHydraulicOil();
        fluidHydraulicOilBlock = new BlockBaseFluid(fluidHydraulicOil, Names.fluidHydraulicOil);

        fluidOil = new FluidOil();
        fluidOilBlock = new BlockFluidOil();

        fluidLubricant = new FluidLubricant();
        fluidLubricantBlock = new BlockBaseFluid(fluidLubricant, Names.fluidLubricant);

        registerFluids();
    }

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Registers the Fluids to the GameRegistry
     */
	public static void registerFluids(){
		GameRegistry.registerBlock(fluidHydraulicOilBlock, fluidHydraulicOilBlock.getUnlocalizedName());
        GameRegistry.registerBlock(fluidOilBlock, fluidOilBlock.getUnlocalizedName());
        GameRegistry.registerBlock(fluidLubricantBlock, fluidLubricant.getUnlocalizedName());
	}

}
