package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineStorage;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPressureVat extends MachineStorage {

	protected BlockHydraulicPressureVat() {
		super(Ids.blockHydraulicPressureVat, Names.blockHydraulicPressurevat);
		this.hasTopIcon = true;
		this.hasBottomIcon = true;
	}
	
	
}
