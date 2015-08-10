package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;


import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidCraftingMachine;
import k4unl.minecraft.Hydraulicraft.lib.recipes.InventoryFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileAssembler extends TileHydraulicBase implements IHydraulicConsumer, IInventory,
        IFluidCraftingMachine, IFluidHandler, ISidedInventory {
    public static final int   MAX_RECIPE_TICKS_AT_MAX_PRESSURE = 4;
    public static final float PRESSURE_USAGE_MULTIPLIER        = 1.2f;

    InventoryFluidCrafting inventoryCrafting;
    IFluidRecipe           recipe;

    public TileAssembler() {

        super(10);
        super.init(this);
        FluidTank[] tanks = new FluidTank[1];

        // TODO size of assembler's crafting tank
        tanks[0] = new FluidTank(4000);
        inventoryCrafting = new InventoryFluidCrafting(this, 3, tanks, null);
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {
        int maxTicks = (int) (((float) getPressure(from) / getMaxPressure(isOilStored(), from)) * MAX_RECIPE_TICKS_AT_MAX_PRESSURE);

        if (recipe != null) {
            float usedPressure = recipe.getPressure() * (maxTicks * PRESSURE_USAGE_MULTIPLIER);

            if (!inventoryCrafting.canWork(recipe))
                return 0;

            if (simulate) {
                return usedPressure;
            }

            while (maxTicks-- > 0)
                inventoryCrafting.recipeTick(recipe);

            return usedPressure;
        }

        return 0;
    }

    @Override
    public boolean canWork(ForgeDirection dir) {
        return dir.equals(ForgeDirection.UP);
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(ForgeDirection side) {
        return true;
    }

    public int getScaledAssembleTime() {
        if (recipe == null)
            return 0;

        return (int) ((inventoryCrafting.getProgress() / (float) recipe.getCraftingTime()) * 18);
    }

    @Override
    public int getSizeInventory() {
        return inventoryCrafting.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventoryCrafting.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventoryCrafting.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventoryCrafting.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventoryCrafting.setInventorySlotContents(slot, itemStack);
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
        return inventoryCrafting.getInventoryStackLimit();
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
        if (slot < inventoryCrafting.getSizeInventory())
            return inventoryCrafting.isItemValidForSlot(slot, itemStack);

        // for slot = 9 = output slot return false
        return false;
    }

    public InventoryFluidCrafting getFluidInventory() {
        return inventoryCrafting;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventoryCrafting.save(tagCompound);

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventoryCrafting.load(tagCompound);
    }

    @Override
    public void onCraftingMatrixChanged() {
        if (inventoryCrafting.isCraftingInProgress())
            return;

        recipe = HydraulicRecipes.getAssemblerRecipe(inventoryCrafting);

        if (recipe != null)
            inventoryCrafting.startRecipe(recipe);

        markDirty();
    }

    @Override
    public void spawnOverflowItemStack(ItemStack stack) {
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, stack));
    }

    /* ***** IFLUIDHANDLER */

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return inventoryCrafting.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return inventoryCrafting.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return inventoryCrafting.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return inventoryCrafting.canFill(fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return inventoryCrafting.canDrain(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return inventoryCrafting.getTankInfo();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return inventoryCrafting.canInsertItem(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
        if (inventoryCrafting.canExtractItem(slot, itemStack))
            return true;

        return false;
    }
}
