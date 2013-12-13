package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineConsumer;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicFrictionIncinerator extends MachineConsumer {

	protected BlockHydraulicFrictionIncinerator() {
		super(Ids.blockHydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator);
		this.hasTopIcon = true;
		this.hasBottomIcon = true;
	}
	
	public int getMaxBar() {
		return 0;
	}
	
	public int getMinimumBar() {
		return 0;
	}
	
	public void workFunction(int bar) {
		
	}
	
	
}
