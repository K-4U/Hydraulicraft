package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.ItemStackUtils;
import k4unl.minecraft.k4lib.lib.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.*;

public class TileHydraulicFiller extends TileHydraulicBase implements IFluidHandler, IHydraulicConsumer, ISidedInventory {

    private static final int   TRANSFER_PER_TICK = 5;
    private static final float PRESSURE_PER_TICK = 1.0f;
    private static final int   TANK_CAPACITY     = 8000;

    private FillerDirection fillerDirection;
    private SimpleInventory inventory;
    private FluidTank       internalTank;

    public TileHydraulicFiller() {

        super(6);
        init(this);
        fillerDirection = FillerDirection.NONE;
        inventory = new SimpleInventory(2);
        internalTank = new FluidTank(TANK_CAPACITY);
    }

    /* IFluidHandler */
    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        return internalTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {

        return internalTank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        return internalTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

        return true; // TODO possibly check for identical fluids?
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {

        return true; // TODO possibly check for identical fluids?
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        return new FluidTankInfo[]{internalTank.getInfo()};
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (inventory.getStackInSlot(0) == null)
            return 0;

        if (inventory.getStackInSlot(0).getItem() instanceof IFluidContainerItem)
            return workFunctionFluidHandler(simulate, from);
        else if (FluidContainerRegistry.isContainer(inventory.getStackInSlot(0)))
            return workFunctionFluidContainer(simulate, from);

        return 0;
    }

    private float workFunctionFluidContainer(boolean simulate, EnumFacing from) {

        ItemStack itemStack = getStackInSlot(0);

        if (fillerDirection == FillerDirection.FILLING && internalTank.getFluid() != null) {
            int capacity = FluidContainerRegistry.getContainerCapacity(internalTank.getFluid(), itemStack);
            if (internalTank.getFluidAmount() >= capacity) {
                if (ItemStackUtils.canMergeStacks(inventory.getStackInSlot(1), FluidContainerRegistry.fillFluidContainer(new FluidStack(internalTank.getFluid(), capacity), itemStack))) {
                    if (!simulate) {
                        itemStack = FluidContainerRegistry.fillFluidContainer(new FluidStack(internalTank.getFluid(), capacity), itemStack);
                        inventory.setInventorySlotContents(1, ItemStackUtils.mergeStacks(itemStack, inventory.getStackInSlot(1)));
                        internalTank.drain(capacity, true);
                        decrStackSize(0, 1);
                    }

                    return PRESSURE_PER_TICK;
                }
            }
        } else if (fillerDirection == FillerDirection.EMPTYING) {
            int capacity = FluidContainerRegistry.getContainerCapacity(itemStack);
            FluidStack theFluid = FluidContainerRegistry.getFluidForFilledItem(itemStack);
            if ((theFluid.isFluidEqual(internalTank.getFluid()) &&
                    internalTank.getCapacity() - internalTank.getFluidAmount() >= capacity) ||
                    internalTank.getFluid() == null) {
                if (ItemStackUtils.canMergeStacks(inventory.getStackInSlot(1), FluidContainerRegistry.drainFluidContainer(itemStack))) {
                    if (!simulate) {
                        internalTank.fill(theFluid, true);
                        inventory.setInventorySlotContents(1, ItemStackUtils.mergeStacks(FluidContainerRegistry.drainFluidContainer(itemStack), inventory.getStackInSlot(1)));
                        decrStackSize(0, 1);
                    }

                    return PRESSURE_PER_TICK;
                }
            }
        }

        return 0;
    }

    public float workFunctionFluidHandler(boolean simulate, EnumFacing from) {

        ItemStack stack = inventory.getStackInSlot(0);
        if (!(stack.getItem() instanceof IFluidContainerItem))
            return 0;


        IFluidContainerItem fluid = (IFluidContainerItem) stack.getItem();


        if (fillerDirection == FillerDirection.EMPTYING) {
            if (fluid.getFluid(stack) == null || fluid.getFluid(stack).amount == 0 || internalTank.getFluidAmount() == internalTank.getCapacity()) {
                setInventorySlotContents(1, getStackInSlot(0).copy());
                setInventorySlotContents(0, null);
                return 0;
            }

            if (internalTank.getFluid() == null || internalTank.getFluidAmount() == 0) {
                FluidStack drained = fluid.drain(stack, TRANSFER_PER_TICK, false);
                int actuallyFilled = internalTank.fill(drained, true);
                fluid.drain(stack, actuallyFilled, true);

                return PRESSURE_PER_TICK;
            }

            if (fluid.getFluid(stack).equals(internalTank.getFluid())) {
                int maxDrain = Math.min(Math.min(TRANSFER_PER_TICK, fluid.getFluid(stack).amount),
                        internalTank.getCapacity() - internalTank.getFluidAmount());
                FluidStack drained = fluid.drain(stack, maxDrain, false);
                int actuallyFilled = internalTank.fill(drained, true);
                fluid.drain(stack, actuallyFilled, true);

                return PRESSURE_PER_TICK;
            }

        } else if (fillerDirection == FillerDirection.FILLING) {
            if (fluid.getFluid(stack) == null || fluid.getFluid(stack).amount == 0) {
                FluidStack drained = internalTank.drain(TRANSFER_PER_TICK, false);
                int actuallyFilled = fluid.fill(stack, drained, true);
                internalTank.drain(actuallyFilled, true);

                return PRESSURE_PER_TICK;
            }

            if (internalTank.getFluid() == null || internalTank.getFluidAmount() == 0 || fluid.getFluid(stack).amount == fluid.getCapacity(stack)) {
                setInventorySlotContents(1, getStackInSlot(0).copy());
                setInventorySlotContents(0, null);
                return 0;
            }

            if (internalTank.getFluid().equals(fluid.getFluid(stack))) {
                int maxDrain = Math.min(Math.min(TRANSFER_PER_TICK, internalTank.getFluidAmount()),
                        fluid.getCapacity(stack) - fluid.getFluid(stack).amount);
                FluidStack drained = internalTank.drain(maxDrain, false);
                int actuallyFilled = fluid.fill(stack, drained, true);
                internalTank.drain(actuallyFilled, true);

                return PRESSURE_PER_TICK;
            }
        }
        return 0;
    }

    /* /IFluidHandler */

    @Override
    public boolean canWork(EnumFacing dir) {
        // whatever?
        if (getNetwork(dir) == null) {
            return false;
        }
        return dir.equals(EnumFacing.UP);
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return true; // why not?
    }

    /* ISidedInventory */
    @Override
    public int[] getSlotsForFace(EnumFacing side) {

        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {

        return slot == 0 && isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {

        return slot == 1;
    }

    @Override
    public int getSizeInventory() {

        return inventory.getSizeInventory();
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
    public void setInventorySlotContents(int slot, ItemStack stack) {

        inventory.setInventorySlotContents(slot, stack);

        if (stack != null && slot == 0)
            updateDirection();
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
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {

        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        if (slot == 1)
            return false;

        if (stack == null)
            return true;

        return stack.getItem() instanceof IFluidContainerItem || FluidContainerRegistry.isContainer(stack);

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

    /* /ISidedInventory */

    private void updateDirection() {

        ItemStack stack = inventory.getStackInSlot(0);
        if (stack == null)
            return;

        if (FluidContainerRegistry.isContainer(stack)) {
            if (FluidContainerRegistry.isFilledContainer(stack))
                fillerDirection = FillerDirection.EMPTYING;
            else
                fillerDirection = FillerDirection.FILLING;

            return;
        }

        if (!(stack.getItem() instanceof IFluidContainerItem))
            return;

        IFluidContainerItem fluid = (IFluidContainerItem) stack.getItem();
        if (fluid.getFluid(stack) == null)
            fillerDirection = FillerDirection.FILLING;
        else if (fluid.getFluid(stack).amount == 0)
            fillerDirection = FillerDirection.FILLING;
        else
            fillerDirection = FillerDirection.EMPTYING;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        inventory.save(tagCompound);
        internalTank.writeToNBT(tagCompound);
        tagCompound.setInteger("fillerDirection", fillerDirection.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        inventory.load(tagCompound);
        internalTank.readFromNBT(tagCompound);
        fillerDirection = FillerDirection.values()[tagCompound.getInteger("fillerDirection")];
    }

    private enum FillerDirection {
        FILLING, // filling an item tank
        EMPTYING, // emptying an item tank
        NONE // unknown status, should not happen beyond when new item is created
    }

}
