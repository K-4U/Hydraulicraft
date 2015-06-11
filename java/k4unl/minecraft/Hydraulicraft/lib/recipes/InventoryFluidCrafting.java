package k4unl.minecraft.Hydraulicraft.lib.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class InventoryFluidCrafting implements IFluidInventory {
    protected FluidTank[]           inputTanks;
    protected FluidTank[]           outputTanks;
    protected IFluidCraftingMachine feedback;
    protected InventoryCrafting     crafting;

    public InventoryFluidCrafting(IFluidCraftingMachine cnt, int gridSize) {
        this(cnt, gridSize, null, null);
    }

    public InventoryFluidCrafting(IFluidCraftingMachine cnt, int gridSize, FluidTank[] inputTank, FluidTank[] outputTank) {
        this.crafting = new InventoryCrafting(new DummyContainer(), gridSize, gridSize);
        inputTanks = inputTank;
        outputTanks = outputTank;
        this.feedback = cnt;
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

        markDirty();

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

        markDirty();

        return false;
    }

    @Override
    public InventoryCrafting getInventoryCrafting() {
        return crafting;
    }

    @Override
    public void eatFluids(IFluidRecipe recipe, float percent) {
        for (FluidStack inFluid : recipe.getInputFluids()) {
            drainFluid(inFluid, false);
        }

        for (FluidStack outFluid : recipe.getOutputFluids()) {
            fillFluid(outFluid, false);
        }
    }

    @Override
    public int getSizeInventory() {
        return crafting.getSizeInventory() + 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return crafting.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrBy) {
        return crafting.decrStackSize(slot, decrBy);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return crafting.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        crafting.setInventorySlotContents(slot, itemStack);

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

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            if (crafting.getStackInSlot(i) == null)
                continue;

            NBTTagCompound compound1 = new NBTTagCompound();
            compound1.setByte("Slot", (byte) i);
            crafting.getStackInSlot(i).writeToNBT(compound1);
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
        crafting = new InventoryCrafting(new DummyContainer(), (int) Math.sqrt(crafting.getSizeInventory()), (int) Math.sqrt(crafting.getSizeInventory()));

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound compound1 = list.getCompoundTagAt(i);
            int j = compound1.getByte("Slot") & 255;
            if (j >= 0 && j < this.crafting.getSizeInventory())
                crafting.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(compound1));
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

    public void eatItems() {
        // copypasta from SlotCrafting :X

        for (int i = 0; i < crafting.getSizeInventory(); i++) {
            ItemStack stack = crafting.getStackInSlot(i);
            if (stack != null) {
                crafting.decrStackSize(i, 1);
                if (stack.getItem().hasContainerItem(stack)) {
                    ItemStack containerStack = stack.getItem().getContainerItem(stack);
                    if (containerStack != null && containerStack.isItemStackDamageable() && containerStack.getItemDamage() > containerStack.getMaxDamage()) {
                        continue;
                    }

                    if (stack.getItem().doesContainerItemLeaveCraftingGrid(stack)) {
                        if (crafting.getStackInSlot(i) == null)
                            crafting.setInventorySlotContents(i, containerStack);
                        else
                            feedback.spawnOverflowItemStack(containerStack);
                    }
                }
            }
        }
    }
}
