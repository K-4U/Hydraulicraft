package k4unl.minecraft.Hydraulicraft.items;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Optional;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

@Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraftAPI|core")
public class ItemHydraulicWrench extends HydraulicItemBase implements IPressurizableItem, IToolWrench {
    public static final float MAX_PRESSURE        = 1500 * 1000;
    public static final float PRESSURE_PER_WRENCH = 1000;

    public ItemHydraulicWrench() {
        super(Names.itemHydraulicWrench);
    }

    @Override
    public float getPressure(ItemStack itemStack) {
        return fetchPressure(itemStack);
    }

    @Override
    public void setPressure(ItemStack itemStack, float newStored) {
        savePressure(itemStack, newStored);
    }

    @Override
    public float getMaxPressure() {
        return MAX_PRESSURE;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    private float fetchPressure(ItemStack container) {
        if (container.getTagCompound() == null || container.getTagCompound().getTag("pressure") == null) {
            container.stackTagCompound = new NBTTagCompound();
            container.stackTagCompound.setFloat("pressure", 0);
        }

        return container.stackTagCompound.getFloat("pressure");
    }

    private void savePressure(ItemStack container, float newPressure) {
        if (container.getTagCompound() == null)
            container.setTagCompound(new NBTTagCompound());

        container.stackTagCompound.setFloat("pressure", newPressure);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean noIdea) {
        super.addInformation(itemStack, player, lines, noIdea);
        float pressure = fetchPressure(itemStack);
        lines.add("Pressure: " + Math.round(pressure / 1000) + " Bar/" + Math.round(getMaxPressure() / 1000) + " Bar");
    }

    @Override
    public boolean canWrench(EntityPlayer entityPlayer, int i, int i1, int i2) {
        ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemHydraulicWrench))
            return false;

        ItemHydraulicWrench wrench = (ItemHydraulicWrench) itemStack.getItem();
        return wrench.fetchPressure(itemStack) >= PRESSURE_PER_WRENCH;
    }

    @Override
    public void wrenchUsed(EntityPlayer entityPlayer, int i, int i1, int i2) {
        ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
        if (itemStack == null || !(itemStack.getItem() instanceof ItemHydraulicWrench))
            return;

        ItemHydraulicWrench wrench = (ItemHydraulicWrench) itemStack.getItem();
        wrench.setPressure(itemStack, wrench.getPressure(itemStack) - PRESSURE_PER_WRENCH);
    }
}
