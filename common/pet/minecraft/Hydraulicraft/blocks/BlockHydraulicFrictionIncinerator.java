package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.baseClasses.interfaces.IMachineConsumer;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicFrictionIncinerator extends MachineBlock implements IMachineConsumer {

	protected BlockHydraulicFrictionIncinerator() {
		super(Ids.blockHydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator);
		this.hasFrontIcon = true;
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
