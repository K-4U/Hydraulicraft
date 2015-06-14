package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;


import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.recipes.*;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.ItemStackUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileAssembler extends TileHydraulicBase implements IHydraulicConsumer, IInventory, IFluidCraftingMachine, IFluidHandler {
    InventoryFluidCrafting    inventoryCrafting;
    InventoryFluidCraftResult inventoryResult;
    IFluidRecipe              recipe;
    int workProgress = 0;

    public TileAssembler() {
        super(10);
        super.init(this);
        FluidTank[] tanks = new FluidTank[1];

        // TODO size of assembler's crafting tank
        tanks[0] = new FluidTank(4000);
        inventoryCrafting = new InventoryFluidCrafting(this, 3, tanks, null);
        inventoryResult = new InventoryFluidCraftResult(this);
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {
        if (recipe != null) {
            float usedPressure = recipe.getPressure();

            if (inventoryResult.getStackInSlot(0) != null &&
                    !ItemStackUtils.canMergeStacks(inventoryResult.getStackInSlot(0), recipe.getRecipeOutput()))
                return 0;

            if (simulate) {
                return recipe.getPressure();
            }

            if (workProgress == 0) {
                inventoryCrafting.eatItems();
            }

            workProgress++;
            inventoryCrafting.eatFluids(recipe, 1f / recipe.getCraftingTime());

            if (workProgress == recipe.getCraftingTime()) {
                inventoryResult.setInventorySlotContents(0,
                        ItemStackUtils.mergeStacks(recipe.getCraftingResult(inventoryCrafting.getInventoryCrafting()),
                                inventoryResult.getStackInSlot(0)));
                markDirty();
                onCraftingMatrixChanged();
            }

            return usedPressure;
        }

        return 0;
    }

    @Override
    public boolean canWork(ForgeDirection dir) {
        return true;
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(ForgeDirection side) {
        return true;
    }

    public int getScaledAssembleTime() {
        return 0;
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
        return inventoryCrafting.isItemValidForSlot(slot, itemStack);
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

    public InventoryCraftResult getInventoryResult() {
        return inventoryResult;
    }

    @Override
    public void onCraftingMatrixChanged() {
        recipe = HydraulicRecipes.getAssemblerRecipe(inventoryCrafting);
        if (recipe != null) {
            workProgress = 0;
        }
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
}
