package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineTransporter;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicHoze extends MachineTransporter {

	protected BlockHydraulicHoze() {
		super(Ids.blockHydraulicHoze, Names.blockHydraulicHoze);
		this.hasTopIcon = true;
		this.hasBottomIcon = true;
	}
	
	
	
}
