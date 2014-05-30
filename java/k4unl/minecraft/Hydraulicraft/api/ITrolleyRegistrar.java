package k4unl.minecraft.Hydraulicraft.api;

import k4unl.minecraft.Hydraulicraft.lib.TrolleyRegistrar;
import net.minecraft.item.ItemStack;

public interface ITrolleyRegistrar {
    /**
     * Use this method to register your own Harvester trolley.
     * @param toRegister
     */
	public void registerTrolley(IHarvesterTrolley toRegister);
	
	/**
	 * Use this to get the trolley item that's associated with the key. This key is the same as the one returned in {@link IHarvesterTrolley#getName()}.
	 * You can use this to register crafting recipes for example.
	 * @param trolleyName
	 * @return
	 */
    public ItemStack getTrolleyItem(String trolleyName);
}
