package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.SimpleInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileHydraulicFiller extends TileHydraulicBase implements IFluidHandler, IHydraulicConsumer, ISidedInventory {
    private static final int   TRANSFER_PER_TICK = 5;
    private static final float PRESSURE_PER_TICK = 1.0f;
    private static final int   TANK_CAPACITY     = 8000;

    private FillerDirection fillerDirection;
    private SimpleInventory inventory;
    private FluidTank       internalTank;
    private boolean checkForWork = false;

    public TileHydraulicFiller() {
        super(6);
        init(this);
        fillerDirection = FillerDirection.NONE;
        inventory = new SimpleInventory(1);
        internalTank = new FluidTank(TANK_CAPACITY);
    }

    /* IFluidHandler */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return internalTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return internalTank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return internalTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true; // TODO possibly check for identical fluids?
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true; // TODO possibly check for identical fluids?
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{internalTank.getInfo()};
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {
        if (inventory.getStackInSlot(0) == null)
            return 0;

        if (inventory.getStackInSlot(0).getItem() instanceof IFluidContainerItem)
            return workFunctionFluidHandler(simulate, from);
        else if (FluidContainerRegistry.isContainer(inventory.getStackInSlot(0)))
            return workFunctionFluidContainer(simulate, from);

        return 0;
    }

    private float workFunctionFluidContainer(boolean simulate, ForgeDirection from) {
        if (checkForWork) {
            ItemStack itemStack = getStackInSlot(0);
            if (fillerDirection == FillerDirection.FILLING && internalTank.getFluid() != null) {
                int capacity = FluidContainerRegistry.getContainerCapacity(internalTank.getFluid(), itemStack);
                if (internalTank.getFluidAmount() >= capacity) {
                    if (!simulate) {
                        itemStack = FluidContainerRegistry.fillFluidContainer(new FluidStack(internalTank.getFluid(), capacity), itemStack);
                        setInventorySlotContents(0, itemStack);
                        internalTank.drain(capacity, true);

                        checkForWork = false;
                    }

                    return PRESSURE_PER_TICK;
                }
            } else if (fillerDirection == FillerDirection.EMPTYING) {
                int capacity = FluidContainerRegistry.getContainerCapacity(itemStack);
                FluidStack theFluid = FluidContainerRegistry.getFluidForFilledItem(itemStack);
                if ((theFluid.isFluidEqual(internalTank.getFluid()) &&
                        internalTank.getCapacity() - internalTank.getFluidAmount() >= capacity) ||
                        internalTank.getFluid() == null) {
                    internalTank.fill(theFluid, true);
                    setInventorySlotContents(0, FluidContainerRegistry.drainFluidContainer(itemStack));
                    checkForWork = false;

                    return PRESSURE_PER_TICK;
                }
            }
        }

        return 0;
    }

    public float workFunctionFluidHandler(boolean simulate, ForgeDirection from) {
        if (inventory.getStackInSlot(0) == null)
            return 0;
        if (!(inventory.getStackInSlot(0).getItem() instanceof IFluidContainerItem))
            return 0;

        ItemStack stack = inventory.getStackInSlot(0);
        IFluidContainerItem fluid = (IFluidContainerItem) stack.getItem();


        if (fillerDirection == FillerDirection.EMPTYING) {
            if (fluid.getFluid(stack) == null || fluid.getFluid(stack).amount == 0)
                return 0;

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
            if (internalTank.getFluid() == null || internalTank.getFluidAmount() == 0)
                return 0;

            if (fluid.getFluid(stack) == null || fluid.getFluid(stack).amount == 0) {
                FluidStack drained = internalTank.drain(TRANSFER_PER_TICK, false);
                int actuallyFilled = fluid.fill(stack, drained, true);
                internalTank.drain(actuallyFilled, true);

                return PRESSURE_PER_TICK;
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
    public boolean canWork(ForgeDirection dir) {
        // whatever?
        if (getNetwork(dir) == null) {
            return false;
        }
        return dir.equals(ForgeDirection.UP);
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(ForgeDirection side) {
        return true; // why not?
    }

    /* ISidedInventory */
    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot == 0 && isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return slot == 0;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot > 0)
            return null;
        return inventory.getStackInSlot(0);
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrBy) {
        return inventory.decrStackSize(slot, decrBy);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
        if (stack != null)
            updateDirection();
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
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null)
            return true;

        return stack.getItem() instanceof IFluidContainerItem || FluidContainerRegistry.isContainer(stack);

    }

    /* /ISidedInventory */

    private void updateDirection() {
        ItemStack stack = inventory.getStackInSlot(0);
        if (stack == null)
            return;

        if (FluidContainerRegistry.isContainer(stack)) {
            checkForWork = true;
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
