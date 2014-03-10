package k4unl.minecraft.Hydraulicraft.fluids;

import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class Fluids {
	public static Fluid fluidOil;
	public static Block fluidOilBlock;
	
	
	public static void init(){
		fluidOil = new FluidOil();
		fluidOilBlock = new BlockFluidOil();
		
		registerFluids();
		addNames();
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Registers the Fluids to the GameRegistry
	 */
	public static void registerFluids(){
		GameRegistry.registerBlock(fluidOilBlock,fluidOilBlock.getUnlocalizedName());
	}
	
	/*!
	 * @author Koen Beckers
	 * @date 13-12-2013
	 * Adds the name to the LanguageRegistry.
	 * Note: No localization yet. Maybe after Modjam!
	 */
	public static void addNames(){
		//LanguageRegistry.addName(fluidOilBlock, Names.fluidOil.localized);
	}
}
