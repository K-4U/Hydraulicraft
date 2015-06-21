package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidCraftingMachine;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidInventory;
import k4unl.minecraft.Hydraulicraft.lib.recipes.InventoryFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public class TileHydraulicCrusher extends TileHydraulicBase implements IInventory, IHydraulicConsumer, IFluidInventory, IFluidCraftingMachine {

    private final float requiredPressure = 5F;
    private InventoryFluidCrafting inventory;
    private IFluidRecipe           recipe;

    public TileHydraulicCrusher() {

        super(5);
        super.init(this);
        inventory = new InventoryFluidCrafting(this, 1, null, null);
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {

        if (recipe != null) {
            float usedPressure = recipe.getPressure();

            if (!inventory.canWork(recipe))
                return 0;

            float maxPressure = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
            float ratio = getPressure(ForgeDirection.UP) / maxPressure;
            //int ticks = (int) ((float) pNetwork.getLowestTier().ordinal() * 16 * ratio);
            int ticks = 1;

            if (!simulate)
                for (int i = 0; i < ticks; i++)
                    inventory.recipeTick(recipe);

            return usedPressure * (Functions.getMaxGenPerTier(pNetwork.getLowestTier(), true) * 0.75f);
        }

        return 0;
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
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory.setInventorySlotContents(slot, itemStack);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord, yCoord, zCoord) < 64;
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }


    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return inventory.isItemValidForSlot(slot, itemStack);
    }


    @Override
    public void onBlockBreaks() {
        dropItemStackInWorld(inventory.getStackInSlot(0));
        dropItemStackInWorld(inventory.getStackInSlot(1));
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        inventory.load(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        inventory.save(tagCompound);
    }


    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public boolean canConnectTo(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean canWork(ForgeDirection dir) {
        if (getNetwork(dir) == null) {
            return false;
        }
        return dir.equals(ForgeDirection.UP);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public String getInventoryName() {
        return Localization.getLocalizedName(Names.blockHydraulicCrusher.unlocalized);
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, boolean doDrain) {

        return null;
    }

    @Override
    public FluidStack craftingDrain(FluidStack fluidStack, boolean doDrain) {

        return null;
    }

    @Override
    public int fill(FluidStack fluidStack, boolean doDrain) {

        return 0;
    }

    @Override
    public int craftingFill(FluidStack fluidStack, boolean doDrain) {

        return 0;
    }

    @Override
    public InventoryCrafting getInventory() {

        return null;
    }

    @Override
    public void recipeTick(IFluidRecipe recipe) {

    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

        return null;
    }

    @Override
    public boolean canFill(Fluid fluid) {

        return false;
    }

    @Override
    public boolean canDrain(Fluid fluid) {

        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo() {

        return new FluidTankInfo[0];
    }

    @Override
    public void onCraftingMatrixChanged() {
        if (inventory.isCraftingInProgress())
            return;

        recipe = HydraulicRecipes.getCrusherRecipe(inventory);
        if (recipe != null)
            inventory.startRecipe(recipe);

        markDirty();

    }

    public int getScaledCrushTime() {
        if (recipe == null)
            return 0;

        return (int) ((inventory.getProgress() / (float) recipe.getCraftingTime()) * 16);
    }

    @Override
    public void spawnOverflowItemStack(ItemStack stack) {
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, stack));
    }
}
