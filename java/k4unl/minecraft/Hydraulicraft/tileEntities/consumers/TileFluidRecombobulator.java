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
    private FluidTank inputTank  = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

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

        return inventoryCrafting.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {

        return inventoryCrafting.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return inventoryCrafting.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventoryCrafting.setInventorySlotContents(index, stack);
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

        return inventoryCrafting.isItemValidForSlot(index, stack);
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

    public InventoryFluidCrafting getInventoryCrafting(){

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
