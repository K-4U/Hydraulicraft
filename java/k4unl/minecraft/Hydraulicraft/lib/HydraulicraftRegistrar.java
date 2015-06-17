package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicraftRegistrar;
import net.minecraft.item.ItemStack;

@Deprecated
public class HydraulicraftRegistrar implements IHydraulicraftRegistrar {

    @Override
    public void registerTrolley(IHarvesterTrolley toRegister) {

        Hydraulicraft.trolleyRegistrar.registerTrolley(toRegister);
    }

    @Override
    public ItemStack getTrolleyItem(String trolleyName) {

        return Hydraulicraft.trolleyRegistrar.getTrolleyItem(trolleyName);
    }
}
