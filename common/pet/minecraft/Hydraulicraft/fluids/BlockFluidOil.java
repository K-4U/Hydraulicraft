package pet.minecraft.Hydraulicraft.fluids;

import pet.minecraft.Hydraulicraft.lib.config.Ids;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidOil extends BlockFluidClassic {

	public BlockFluidOil() {
		super(Ids.blockFluidOil.act, Fluids.fluidOil, Material.water);
	}

}
