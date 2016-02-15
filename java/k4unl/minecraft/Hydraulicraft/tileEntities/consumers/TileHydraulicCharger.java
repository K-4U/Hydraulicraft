package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileHydraulicCharger extends TileHydraulicBase implements IInventory, IHydraulicConsumer {

    public static final float MAX_PRESSURE_PER_TICK = 200f;
    public static final float TOLERANCE             = 1f;
    public static final float MAX_FLUID_PER_TICK    = 2f;

    SimpleInventory inventory;

    public TileHydraulicCharger() {

        super(1);
        super.init(this);
        inventory = new SimpleInventory(1);
    }


    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return true;
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (inventory.getStackInSlot(0) == null)
            return 0;
        if (!(inventory.getStackInSlot(0).getItem() instanceof IPressurizableItem))
            return 0;

        ItemStack itemStack = inventory.getStackInSlot(0);
        IPressurizableItem pressurizableItem = (IPressurizableItem) itemStack.getItem();

        // balance the pressure
        float currentPressureItem = pressurizableItem.getPressure(itemStack);
        float currentPressureNetwork = getPressure(from);

        float diff = Math.abs(currentPressureItem - currentPressureNetwork);

        if (diff < TOLERANCE)
            return 0f;

        if (!simulate) {
            float maxTransfer = Math.min(diff, MAX_PRESSURE_PER_TICK);
            if (currentPressureNetwork > currentPressureItem) {
                pressurizableItem.setPressure(itemStack, currentPressureItem + maxTransfer);
                setPressure(currentPressureNetwork - maxTransfer, from);
            } else {
                pressurizableItem.setPressure(itemStack, currentPressureItem - maxTransfer);
                setPressure(currentPressureNetwork + maxTransfer, from);
            }

            if (pressurizableItem.getMaxPressure() < pressurizableItem.getPressure(itemStack)) {
                inventory.setInventorySlotContents(0, null);
                worldObj.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), 2, false);
            }

            FluidStack currentFluid = pressurizableItem.getFluid(itemStack);
            float maxFluid = pressurizableItem.getMaxFluid();

            if (currentFluid == null) {
                if (getStored() > 0) {
                    float toAdd = Math.min(Math.min(maxFluid, MAX_FLUID_PER_TICK), getStored());
                    FluidStack toFill = new FluidStack(isOilStored() ? Fluids.fluidHydraulicOil : FluidRegistry.WATER, (int) toAdd);
                    pressurizableItem.setFluid(itemStack, toFill);
                    setStored((int) (getStored() - toAdd), isOilStored(), true);
                }
            } else if (currentFluid.amount < maxFluid && getStored() > 0) {
                if ((isOilStored() && currentFluid.getFluid().equals(Fluids.fluidHydraulicOil)) || (!isOilStored() && currentFluid.getFluid().equals(FluidRegistry.WATER))) {
                    float toAdd = Math.min(Math.min(maxFluid - currentFluid.amount, MAX_FLUID_PER_TICK), getStored());
                    currentFluid.amount += toAdd;
                    pressurizableItem.setFluid(itemStack, currentFluid);
                    setStored((int) (getStored() - toAdd), isOilStored(), true);
                }
            }

            //if(currentFluid.amount < maxFluid && currentFluid.getFluid().equals())
        }

        return 0.5f;

    }


    @Override
    public boolean canWork(EnumFacing dir) {

        return true;
    }


    @Override
    public int getSizeInventory() {

        return 1;
    }


    @Override
    public ItemStack getStackInSlot(int slot) {

        return inventory.getStackInSlot(slot);
    }


    @Override
    public ItemStack decrStackSize(int slot, int decrBy) {

        return inventory.decrStackSize(slot, decrBy);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return inventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {

        inventory.setInventorySlotContents(var1, var2);
    }

    @Override
    public int getInventoryStackLimit() {

        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {

        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }


    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {

        return var2.getItem() instanceof IPressurizableItem;
    }

    @Override
    public int getField(int id) {

        return inventory.getField(id);
    }

    @Override
    public void setField(int id, int value) {

        inventory.setField(id, value);
    }

    @Override
    public int getFieldCount() {

        return inventory.getFieldCount();
    }

    @Override
    public void clear() {

        inventory.clear();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        inventory.save(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        inventory.load(tagCompound);
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public boolean hasCustomName() {

        return false;
    }

    @Override
    public IChatComponent getDisplayName() {

        return null;
    }
}
