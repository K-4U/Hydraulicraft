package k4unl.minecraft.Hydraulicraft.lib.recipes;

import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.k4lib.lib.ItemStackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

public class InventoryFluidCrafting implements IFluidInventory {
    protected FluidTank[]               inputTanks;
    protected FluidTank[]               outputTanks;
    protected IFluidCraftingMachine     feedback;
    protected InventoryCrafting         crafting;
    protected InventoryFluidCraftResult inventoryResult;
    protected ItemStack                 outputItem; // used only for saving
    protected float                     progress;
    private   boolean                   craftingInProgress;

    public InventoryFluidCrafting(IFluidCraftingMachine cnt, int gridSize) {
        this(cnt, gridSize, null, null);
    }

    public InventoryFluidCrafting(IFluidCraftingMachine cnt, int gridSize, FluidTank[] inputTank, FluidTank[] outputTank) {
        this.crafting = new InventoryCrafting(new DummyContainer(), gridSize, gridSize);
        inputTanks = inputTank;
        outputTanks = outputTank;
        this.feedback = cnt;
        inventoryResult = new InventoryFluidCraftResult(cnt);
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, boolean doDrain) {
        if (outputTanks == null)
            return null;

        for (FluidTank tank : outputTanks) {
            if (tank.getFluid() != null && tank.getFluid().equals(fluidStack)) {
                FluidStack retval = tank.drain(fluidStack.amount, doDrain);
                if (doDrain)
                    markDirty();

                return retval;
            }
        }

        return null;
    }

    @Override
    public FluidStack craftingDrain(FluidStack fluidStack, boolean doDrain) {
        if (inputTanks == null)
            return null;

        for (FluidTank tank : inputTanks) {
            if (tank.getFluid() != null && tank.getFluid().equals(fluidStack)) {
                return tank.drain(fluidStack.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public int fill(FluidStack fluidStack, boolean doDrain) {
        if (inputTanks == null)
            return 0;

        for (FluidTank tank : inputTanks) {
            if (tank.getFluid() == null || tank.getFluid().equals(fluidStack)) {
                int retval = tank.fill(fluidStack, doDrain);
                if (doDrain)
                    markDirty();

                return retval;
            }
        }

        return 0;
    }

    @Override
    public int craftingFill(FluidStack fluidStack, boolean doDrain) {
        if (outputTanks == null)
            return 0;

        for (FluidTank tank : outputTanks) {
            if (tank.getFluid() == null || tank.getFluid().equals(fluidStack)) {
                return tank.fill(fluidStack, doDrain);
            }
        }

        return 0;
    }

    @Override
    public InventoryCrafting getInventory() {
        return crafting;
    }

    public void startRecipe(IFluidRecipe recipe) {
        if (!canWork(recipe) || craftingInProgress)
            return;

        eatItems();
        progress = 0;
        outputItem = recipe.getRecipeOutput().copy();
        craftingInProgress = true;
    }

    public void finishRecipe(IFluidRecipe recipe) {
        inventoryResult.setInventorySlotContents(0,
                ItemStackUtils.mergeStacks(outputItem.copy(), inventoryResult.getStackInSlot(0)));

        outputItem = null;
        craftingInProgress = false;
        progress = 0;

        markDirty();
    }

    public boolean canWork(IFluidRecipe recipe) {
        if (recipe == null)
            return false;

        if (inventoryResult.getStackInSlot(0) != null &&
                !ItemStackUtils.canMergeStacks(inventoryResult.getStackInSlot(0), recipe.getRecipeOutput()))
            return false;

        return true;
    }

    @Override
    public void recipeTick(IFluidRecipe recipe) {
        if (!canWork(recipe))
            return;

        if (!craftingInProgress) {
            startRecipe(recipe);
            return;
        }

        float percent = 1f / recipe.getCraftingTime();

        if (recipe.getInputFluids() != null)
            for (FluidStack inFluid : recipe.getInputFluids()) {
                FluidStack toDrain = inFluid.copy();
                toDrain.amount *= percent;
                FluidStack drained = craftingDrain(toDrain, false);
                if (drained == null)
                    return;
                if (drained.amount < toDrain.amount)
                    return;

                craftingDrain(toDrain, true);
            }

        if (recipe.getOutputFluids() != null)
            for (FluidStack outFluid : recipe.getOutputFluids()) {
                FluidStack toFill = outFluid.copy();
                toFill.amount *= percent;
                if (craftingFill(toFill, false) < toFill.amount)
                    return;

                craftingFill(toFill, true);
            }

        progress++;

        if (progress >= recipe.getCraftingTime()) {
            finishRecipe(recipe);
        }
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (outputTanks == null)
            return null;

        for (FluidTank tank : outputTanks) {
            if (tank.getFluid() != null && tank.getFluidAmount() > 0) {
                return tank.drain(maxDrain, doDrain);
            }
        }

        return null;
    }

    @Override
    public boolean canFill(Fluid fluid) {
        if (inputTanks == null)
            return false;

        for (FluidTank tank : inputTanks) {
            if (tank.getFluid() == null || tank.getFluid().getFluid() == fluid) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canDrain(Fluid fluid) {
        if (outputTanks == null)
            return false;

        for (FluidTank tank : outputTanks) {
            if (tank.getFluid() == null || tank.getFluid().getFluid() == fluid) {
                return true;
            }
        }

        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo() {
        int inputTanksCnt = 0, outputTanksCnt = 0;
        if (inputTanks != null)
            inputTanksCnt += inputTanks.length;

        if (outputTanks != null)
            outputTanksCnt += outputTanks.length;

        FluidTankInfo[] info = new FluidTankInfo[inputTanksCnt + outputTanksCnt];

        if (inputTanks != null)
            for (int i = 0; i < inputTanks.length; i++)
                info[i] = inputTanks[i].getInfo();

        if (outputTanks != null)
            for (int i = 0; i < outputTanks.length; i++)
                info[i + inputTanksCnt] = outputTanks[i].getInfo();

        return info;
    }

    @Override
    public int getSizeInventory() {
        return crafting.getSizeInventory() + inventoryResult.getSizeInventory();
    }

    public int getInternalSizeInventory() {
        return crafting.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < getInternalSizeInventory())
            return crafting.getStackInSlot(slot);

        return inventoryResult.getStackInSlot(slot - getInternalSizeInventory());
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrBy) {
        if (slot < getInternalSizeInventory())
            return crafting.decrStackSize(slot, decrBy);

        return inventoryResult.decrStackSize(slot - getInternalSizeInventory(), decrBy);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (slot < getInternalSizeInventory())
            return crafting.getStackInSlotOnClosing(slot);

        return inventoryResult.getStackInSlotOnClosing(slot - getInternalSizeInventory());
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (slot < getInternalSizeInventory())
            crafting.setInventorySlotContents(slot, itemStack);
        else
            inventoryResult.setInventorySlotContents(slot - getInternalSizeInventory(), itemStack);

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
        if (slot < getInternalSizeInventory())
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

        inventoryResult.save(compound);

        if (outputItem != null)
            compound.setTag("OutputItem", outputItem.writeToNBT(new NBTTagCompound()));

        compound.setFloat("Progress", progress);
        compound.setBoolean("craftingInProgress", craftingInProgress);
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

        inventoryResult.load(compound);

        if (compound.getTag("OutputItem") != null && compound.getTag("OutputItem") instanceof NBTTagCompound)
            outputItem = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("OutputItem"));

        progress = compound.getFloat("Progress");
        craftingInProgress = compound.getBoolean("craftingInProgress");
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


    public boolean isCraftingInProgress() {
        return craftingInProgress;
    }

    public boolean canInsertItem(int slot, ItemStack itemStack) {
        if (getStackInSlot(slot) == null)
            return true;

        if (itemStack == null)
            return false;

        if (getStackInSlot(slot) != null && ItemStackUtils.canMergeStacks(getStackInSlot(slot), itemStack))
            return true;

        return false; // did I forget some possibility?
    }

    public boolean canExtractItem(int slot, ItemStack itemStack) {
        if (slot >= getInternalSizeInventory() && inventoryResult.canExtract(slot - getInternalSizeInventory(), itemStack))
            return true;

        return false;
    }

    public float getProgress() {
        return progress;
    }
}
