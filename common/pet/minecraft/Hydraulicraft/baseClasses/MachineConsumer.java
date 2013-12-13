package pet.minecraft.Hydraulicraft.baseClasses;

import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public abstract class MachineConsumer extends MachineBlock {
	public abstract void workFunction(int bar);
	public abstract int getMaxBar();
	public abstract int getMinimumBar();
	
	protected MachineConsumer(Id blockId, Name machineName) {
		super(blockId, machineName);
	}
}
