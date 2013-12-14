package pet.minecraft.Hydraulicraft.baseClasses.entities;

import pet.minecraft.Hydraulicraft.baseClasses.MachineEntity;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public abstract class TileConsumer extends MachineEntity {
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * Function that gets called to let the machine do its work
	 * Argument of bar so that there can be calculations
	 */
	
	public abstract void workFunction(int bar);
	
	
	/*!
	 * @author Koen Beckers
	 * @date 14-12-2013
	 * This will return the max ammount of bar this consumer can handle.
	 */
	public abstract int getMaxBar();
}
