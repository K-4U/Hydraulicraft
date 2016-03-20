package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidCraftingMachine;
import k4unl.minecraft.Hydraulicraft.lib.recipes.InventoryFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;

/**
 * @author Koen Beckers (K-4U)
 */
public class TileFluidRecombobulator extends TileHydraulicBase implements IHydraulicConsumer, ITickable, IFluidHandler, IInventory, IFluidCraftingMachine {

    private InventoryFluidCrafting inventoryCrafting;
    private IFluidRecipe           recipe;
    private FluidTank inputTank       = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
    private ItemStack inventoryInput  = null;
    private ItemStack inventoryOutput = null;

    public TileFluidRecombobulator() {

        super(10);
        super.init(this);

        FluidTank[] inputTanks = new FluidTank[1];
        inputTanks[0] = inputTank;

        inventoryCrafting = new InventoryFluidCrafting(this, 0, inputTanks);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        return inventoryCrafting.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {

        return inventoryCrafting.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        return inventoryCrafting.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

        //Do a check here if there's a recipe with this fluid.

        return inventoryCrafting.canFill(fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {

        return inventoryCrafting.canDrain(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {

        return inventoryCrafting.getTankInfo();
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return true;
    }

    @Override
    public int getSizeInventory() {

        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {

        if (index == 0) {
            return inventoryInput;
        }
        if (index == 1) {
            return inventoryOutput;
        }
        return inventoryCrafting.getStackInSlot(index - 2);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {

        if (index == 0) {
            ItemStack old = inventoryInput.copy();
            inventoryInput.stackSize--;
            if (inventoryInput.stackSize == 0) {
                inventoryInput = null;
            }
            return old;
        }
        if (index == 1) {
            ItemStack old = inventoryOutput.copy();
            inventoryOutput.stackSize--;
            if (inventoryOutput.stackSize == 0) {
                inventoryOutput = null;
            }
            return old;
        }
        return inventoryCrafting.decrStackSize(index - 2, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return decrStackSize(index, getStackInSlot(index).stackSize);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

        if (index == 0) {
            inventoryInput = stack;
        } else if (index == 1) {
            inventoryOutput = stack;
        } else {
            inventoryCrafting.setInventorySlotContents(index - 2, stack);
        }
        if(inventoryInput != null) {
            ItemStack inUse = getStackInSlot(0);
            FluidStack input = FluidContainerRegistry.getFluidForFilledItem(inUse);
            if (input != null) {
                ItemStack empty = FluidContainerRegistry.drainFluidContainer(inUse);
                boolean canPlace = false;
                if (empty.isItemEqual(getStackInSlot(1))) {
                    if (getStackInSlot(1).getMaxStackSize() > getStackInSlot(1).stackSize) {
                        canPlace = true;
                        empty = getStackInSlot(1);
                        empty.stackSize++;
                    }
                } else if (getStackInSlot(1) == null) {
                    canPlace = true;
                }
                if (canPlace) {
                    int filled = fill(EnumFacing.UP, input, false);
                    if (filled == FluidContainerRegistry.getContainerCapacity(inUse)) {
                        //Do it!
                        fill(EnumFacing.UP, input, true);
                        markDirty();
                        worldObj.notifyBlockUpdate(pos);

                        setInventorySlotContents(0, null);
                        setInventorySlotContents(1, empty);
                    }
                }
            } else {
                if (FluidContainerRegistry.isEmptyContainer(inUse)) {
                    FluidTankInfo tankInfo = getTankInfo(EnumFacing.UP)[0];
                    if (tankInfo.fluid == null) return;
                    ItemStack filledContainer = FluidContainerRegistry.fillFluidContainer(tankInfo.fluid, inUse);
                    if (filledContainer == null) return;
                    int toDrain = FluidContainerRegistry.getContainerCapacity(filledContainer);

                    FluidStack drained = drain(EnumFacing.UP, toDrain, false);
                    boolean canPlace = false;
                    if (getStackInSlot(1) != null && getStackInSlot(1).isItemEqual(filledContainer)) {
                        if (getStackInSlot(1).getMaxStackSize() > getStackInSlot(1).stackSize) {
                            canPlace = true;
                            filledContainer.stackSize += getStackInSlot(index).stackSize;
                        }
                    } else if (getStackInSlot(1) == null) {
                        canPlace = true;
                    }
                    if (drained != null && drained.amount == toDrain && canPlace) {
                        drain(EnumFacing.UP, toDrain, true);
                        markDirty();
                        markBlockForUpdate();
                        decrStackSize(0, 1);
                        setInventorySlotContents(1, filledContainer);
                    }
                }
            }
        }
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
    public void openInventory(EntityPlayer player) {

        inventoryCrafting.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {

        inventoryCrafting.closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {

        if(index == 0){
            return FluidContainerRegistry.isContainer(stack);
        }
        if(index == 1){
            return false;
        }
        return inventoryCrafting.isItemValidForSlot(index-2, stack);
    }

    @Override
    public int getField(int id) {

        return inventoryCrafting.getField(id);
    }

    @Override
    public void setField(int id, int value) {

        inventoryCrafting.setField(id, value);
    }

    @Override
    public int getFieldCount() {

        return inventoryCrafting.getFieldCount();
    }

    @Override
    public void clear() {

        inventoryCrafting.clear();
    }

    @Override
    public String getName() {

        return Names.blockFluidRecombobulator.unlocalized;
    }

    @Override
    public boolean hasCustomName() {

        return false;
    }

    @Override
    public IChatComponent getDisplayName() {

        return null;
    }

    @Override
    public void onCraftingMatrixChanged() {

        if (inventoryCrafting.isCraftingInProgress())
            return;

        recipe = HydraulicRecipes.getRecombobulatorRecipe(inventoryCrafting);
        if (recipe != null)
            inventoryCrafting.startRecipe(recipe);

        markDirty();
    }

    @Override
    public void spawnOverflowItemStack(ItemStack stack) {

    }

    public InventoryFluidCrafting getInventoryCrafting() {

        return inventoryCrafting;
    }


    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (recipe != null) {
            float usedPressure = recipe.getPressure();

            if (!inventoryCrafting.canWork(recipe))
                return 0;

            if (simulate) {
                return recipe.getPressure();
            }

            inventoryCrafting.recipeTick(recipe);

            return usedPressure;
        }

        return 0;
    }

    @Override
    public boolean canWork(EnumFacing dir) {

        return recipe != null && dir.equals(EnumFacing.UP);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        inventoryCrafting.load(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        inventoryCrafting.save(tagCompound);
    }
}
