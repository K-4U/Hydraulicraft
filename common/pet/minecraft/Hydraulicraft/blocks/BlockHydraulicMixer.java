package pet.minecraft.Hydraulicraft.blocks;

import pet.minecraft.Hydraulicraft.baseClasses.MachineConsumer;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicMixer extends MachineConsumer {

	protected BlockHydraulicMixer() {
		super(Ids.blockHydraulicMixer, Names.blockHydraulicMixer);
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
