package k4unl.minecraft.Hydraulicraft.api;

import net.minecraft.item.ItemStack;

public interface IPressurizableItem {
    float getPressure(ItemStack itemStack);

    void setPressure(ItemStack itemStack, float newStored);

    float getMaxPressure();
}
