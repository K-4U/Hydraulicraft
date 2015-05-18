package k4unl.minecraft.Hydraulicraft.fluids;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.registry.GameRegistry;

public class Fluids {
    public static Fluid fluidHydraulicOil;
    public static Block fluidHydraulicOilBlock;

    public static Fluid fluidOil;
    public static Block fluidOilBlock;


    public static void init() {

        fluidHydraulicOil = new FluidHydraulicOil();
        fluidHydraulicOilBlock = new BlockFluidHydraulicOil();

        fluidOil = new FluidOil();
        fluidOilBlock = new BlockFluidOil();

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
	}

}
