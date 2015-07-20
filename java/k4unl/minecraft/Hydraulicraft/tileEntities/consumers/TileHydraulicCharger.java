package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IPressurizableItem;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileHydraulicCharger extends TileHydraulicBase implements IInventory, IHydraulicConsumer {
    public static final float MAX_PRESSURE_PER_TICK = 200f;
    public static final float TOLERANCE             = 1f;

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
    public boolean canConnectTo(ForgeDirection side) {
        return true;
    }


    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {
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
                worldObj.createExplosion(null, xCoord, yCoord, zCoord, 2, false);
            }
        }

        return 0.5f;

    }


    @Override
    public boolean canWork(ForgeDirection dir) {
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
    public ItemStack getStackInSlotOnClosing(int var1) {
        return inventory.getStackInSlotOnClosing(var1);
    }


    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        inventory.setInventorySlotContents(var1, var2);
    }


    @Override
    public String getInventoryName() {
        return null;
    }


    @Override
    public boolean hasCustomInventoryName() {
        return false;
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
    public void openInventory() {
    }


    @Override
    public void closeInventory() {
    }


    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return var2.getItem() instanceof IPressurizableItem;
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
}
