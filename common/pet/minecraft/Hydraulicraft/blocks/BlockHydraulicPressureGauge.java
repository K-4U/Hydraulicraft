package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineTransporter;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPressureGauge extends MachineTransporter {

	protected BlockHydraulicPressureGauge() {
		super(Ids.blockHydraulicPressureGauge, Names.blockHydraulicPressureGauge);
		this.hasTopIcon = true;
		this.hasBottomIcon = true;
	}
	
	
	
}
