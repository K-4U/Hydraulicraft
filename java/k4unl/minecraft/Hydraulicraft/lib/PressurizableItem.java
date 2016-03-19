package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItemUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* Default implementation of IPressurizableItem */
public class PressurizableItem implements IPressurizableItem {

    private final float MAX_PRESSURE;
    private final int   FLUID_CAPACITY;

    List<IPressurizableItemUpgrade> upgrades;

    public PressurizableItem(float pressure, int capacity) {

        MAX_PRESSURE = pressure;
        FLUID_CAPACITY = capacity;
        upgrades = new ArrayList<>();
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
    public FluidStack getFluid(ItemStack itemStack) {

        return fetchFluidOrCreate(itemStack);
    }

    @Override
    public void setFluid(ItemStack itemStack, FluidStack fluidStack) {

        saveFluid(itemStack, fluidStack);
    }

    @Override
    public float getMaxFluid() {

        return FLUID_CAPACITY;
    }

    @Override
    public List<IPressurizableItemUpgrade> getUpgrades() {

        return upgrades;
    }

    @Override
    public boolean addUpgrade(IPressurizableItemUpgrade upgrade) {

        for (IPressurizableItemUpgrade item : upgrades)
            if (item.toString().equals(upgrade.toString()))
                return false;

        upgrades.add(upgrade);
        return true;
    }

    @Override
    public void removeUpgrade(IPressurizableItem upgrade) {

        upgrades.remove(upgrade);
    }

    public float fetchPressure(ItemStack container) {

        if (container.getTagCompound() == null || container.getTagCompound().getTag("pressure") == null) {
            container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setFloat("pressure", 0);
        }

        return container.getTagCompound().getFloat("pressure");
    }

    public FluidStack fetchFluidOrCreate(ItemStack container) {

        if (container.getTagCompound() == null || container.getTagCompound().getTag("fluid") == null) {
            if (container.getTagCompound() == null)
                container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setTag("fluid", new NBTTagCompound());
        }

        FluidStack existing = FluidStack.loadFluidStackFromNBT((NBTTagCompound) container.getTagCompound().getTag("fluid"));

        if (existing != null)
            saveFluid(container, existing);

        return existing;
    }

    @SuppressWarnings("unchecked") // pls, I know what I am doing
    public void addInformation(ItemStack itemStack, List lines) {

        float pressure = fetchPressure(itemStack);
        FluidStack fluidStack = fetchFluidOrCreate(itemStack);
        lines.add("Pressure: " + Math.floor(pressure / 1000) + " Bar/" + Math.round(getMaxPressure() / 1000) + " Bar");
        if (fluidStack != null)
            lines.add(fluidStack.getFluid().getLocalizedName(fluidStack) + ": " + fluidStack.amount + " mB/" + FLUID_CAPACITY + " mB");
    }

    public void saveFluid(ItemStack container, FluidStack newFluid) {

        if (container.getTagCompound() == null)
            container.setTagCompound(new NBTTagCompound());

        container.getTagCompound().setTag("fluid", newFluid.writeToNBT(new NBTTagCompound()));
    }

    public void savePressure(ItemStack container, float newPressure) {

        if (container.getTagCompound() == null)
            container.setTagCompound(new NBTTagCompound());

        container.getTagCompound().setFloat("pressure", newPressure);
    }

    // TODO get rid of this
    public void onItemUse(EntityPlayer player, float chanceToReleaseWater, float pressurePerUse) {

        onItemUse(player, null, null, chanceToReleaseWater, pressurePerUse);
    }

    public void onItemUse(EntityPlayer player, BlockPos pos, EnumFacing facing, float chanceToReleaseWater, float pressurePerUse) {

        ItemStack itemStack = player.getActiveItemStack();
        if (itemStack == null || !(itemStack.getItem() instanceof IPressurizableItem))
            return;

        onItemUse(itemStack, player.worldObj.rand, player.worldObj, pos, facing, chanceToReleaseWater, pressurePerUse);
    }

    private void onItemUse(ItemStack stack, Random random, World world, BlockPos pos, EnumFacing facing, float chanceToReleaseWater, float pressurePerUse) {

        for (IPressurizableItemUpgrade upgrade : upgrades) {
            upgrade.onAfterUse(stack, world, pos, facing);
        }

        IPressurizableItem item = (IPressurizableItem) stack.getItem();
        item.setPressure(stack, item.getPressure(stack) - pressurePerUse);

        FluidStack fluidStack = item.getFluid(stack);
        if (fluidStack == null || fluidStack.getFluid() != FluidRegistry.WATER)
            return;

        if (random.nextFloat() <= chanceToReleaseWater) {
            // oops, there's some leaky pipe!
            fluidStack.amount -= 1;
            if (fluidStack.amount == 0)
                item.setFluid(stack, null);
            else
                item.setFluid(stack, fluidStack);
        }
    }

    /**
     * world-less check for whether an item can be used
     *
     * @param stack
     * @param pressurePerUse
     * @return
     */
    public boolean canUse(ItemStack stack, float pressurePerUse) { // TODO get rid of this
        return canUse(stack, null, null, null, pressurePerUse);
    }

    /**
     * world-sensitive check for whether an item can be used
     *
     * @param player
     * @param world
     * @param pos
     * @param facing
     * @param pressurePerUse
     * @return
     */
    public boolean canUse(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float pressurePerUse) {

        ItemStack itemStack = player.getActiveItemStack();
        if (itemStack == null || !(itemStack.getItem() instanceof IPressurizableItem))
            return false;

        return canUse(itemStack, world, pos, facing, pressurePerUse);
    }

    private boolean canUse(ItemStack stack, World world, BlockPos pos, EnumFacing facing, float pressurePerUse) {

        for (IPressurizableItemUpgrade upgrade : upgrades)
            if (!upgrade.onBeforeUse(stack, world, pos, facing))
                return false;


        IPressurizableItem item = (IPressurizableItem) stack.getItem();
        FluidStack fluidStack = item.getFluid(stack);
        if (fluidStack == null)
            return false;

        return item.getPressure(stack) >= pressurePerUse && item.getFluid(stack).amount > 0;
    }
}
