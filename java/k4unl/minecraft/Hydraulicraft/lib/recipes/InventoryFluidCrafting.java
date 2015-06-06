package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class InventoryFluidCrafting implements IFluidInventory {
    protected ItemStack[]           grid;
    protected FluidTank[]           inputTanks;
    protected FluidTank[]           outputTanks;
    protected IFluidCraftingMachine feedback;

    public InventoryFluidCrafting(IFluidCraftingMachine cnt, int gridSize) {
        this(cnt, gridSize, null, null);
    }

    public InventoryFluidCrafting(IFluidCraftingMachine cnt, int gridSize, FluidTank[] inputTank, FluidTank[] outputTank) {
        this.grid = new ItemStack[gridSize * gridSize];
        inputTanks = inputTank;
        outputTanks = outputTank;
        this.feedback = cnt;
    }

    public ItemStack[] getGrid() {
        return grid;
    }

    @Override
    public boolean drainFluid(FluidStack fluidStack, boolean pretend) {
        if (inputTanks == null)
            return false;

        for (FluidTank tank : inputTanks) {
            if (tank.getFluid() != null && tank.getFluid().equals(fluidStack))
                if (tank.drain(fluidStack.amount, false).amount == fluidStack.amount) {
                    if (!pretend)
                        tank.drain(fluidStack.amount, true);

                    return true;
                }
        }

        return false;
    }

    @Override
    public boolean fillFluid(FluidStack fluidStack, boolean pretend) {
        if (outputTanks == null)
            return false;

        for (FluidTank tank : outputTanks) {
            if (tank.getFluid() == null || (tank.getFluid().equals(fluidStack) && tank.fill(fluidStack, true) == fluidStack.amount)) {
                if (!pretend)
                    tank.fill(fluidStack, false);

                return true;
            }
        }

        return false;
    }

    @Override
    public int getSizeInventory() {
        return grid.length + 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot > grid.length || slot < 0)
            return null;

        return grid[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrBy) {
        if (this.grid[slot] != null) {
            ItemStack itemstack;

            if (this.grid[slot].stackSize <= decrBy) {
                itemstack = this.grid[slot];
                this.grid[slot] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.grid[slot].splitStack(decrBy);
                if (this.grid[slot].stackSize == 0) {
                    this.grid[slot] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.grid[slot] != null) {
            ItemStack itemstack = this.grid[slot];
            this.grid[slot] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        this.grid[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
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
        return 64;
    }

    @Override
    public void markDirty() {
        feedback.onCraftingMatrixChanged();
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
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if (slot < 9)
            return true;

        return false;
    }

    public void save(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == null)
                continue;

            NBTTagCompound compound1 = new NBTTagCompound();
            compound1.setByte("Slot", (byte) i);
            grid[i].writeToNBT(compound1);
            list.appendTag(compound1);
        }

        compound.setTag("Items", list);

        list = new NBTTagList();

        if (inputTanks != null)
            for (int i = 0; i < inputTanks.length; i++) {
                NBTTagCompound compound1 = new NBTTagCompound();
                compound1.setByte("Tank", (byte) i);
                inputTanks[i].writeToNBT(compound1);
                list.appendTag(compound1);
            }

        compound.setTag("InputTanks", list);

        list = new NBTTagList();

        if (outputTanks != null)
            for (int i = 0; i < outputTanks.length; i++) {
                NBTTagCompound compound1 = new NBTTagCompound();
                compound1.setByte("Tank", (byte) i);
                outputTanks[i].writeToNBT(compound1);
                list.appendTag(compound1);
            }

        compound.setTag("OutputTanks", list);
    }

    public void load(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList("Items", 10);
        grid = new ItemStack[grid.length]; // reset the grid

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound1 = list.getCompoundTagAt(i);
            int j = compound1.getByte("Slot") & 255;
            if (j >= 0 && j < this.grid.length)
                grid[j] = ItemStack.loadItemStackFromNBT(compound1);
        }

        list = compound.getTagList("InputTanks", 10);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound1 = list.getCompoundTagAt(i);
            int j = compound1.getByte("Tank") & 255;
            if (j >= 0 && j < this.inputTanks.length)
                inputTanks[i].readFromNBT(compound1);
        }

        list = compound.getTagList("OutputTanks", 10);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound1 = list.getCompoundTagAt(i);
            int j = compound1.getByte("Tank") & 255;
            if (j >= 0 && j < this.outputTanks.length)
                outputTanks[i].readFromNBT(compound1);
        }
    }
}
