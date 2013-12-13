package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineTransporter;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPressureValve extends MachineTransporter {

	protected BlockHydraulicPressureValve() {
		super(Ids.blockHydraulicPressureValve, Names.blockHydraulicPressureValve);
		this.hasTopIcon = true;
		this.hasBottomIcon = true;
	}
	
	
	
}
