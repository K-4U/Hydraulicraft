package pet.minecraft.Hydraulicraft.TileEntities;

import pet.minecraft.Hydraulicraft.baseClasses.entities.TileConsumer;
import pet.minecraft.Hydraulicraft.lib.config.Constants;

public class TileHydraulicIncinerator extends TileConsumer {

	@Override
	public void workFunction(int bar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMaxBar() {
		return Constants.MAX_BAR_TIER_3;
	}

}
