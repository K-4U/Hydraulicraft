package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TileHydraulicCrusher extends TileHydraulicBase implements IInventory, IHydraulicConsumer,
        ISidedInventory, IFluidCraftingMachine {

    private final float requiredPressure = 5F;
    private InventoryFluidCrafting inventory;
    private IFluidRecipe           recipe;

    public TileHydraulicCrusher() {

        super(5);
        super.init(this);
        inventory = new InventoryFluidCrafting(this, 1, null, null);
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (recipe != null) {
            float usedPressure = recipe.getPressure();

            if (!inventory.canWork(recipe))
                return 0;

            float maxPressure = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
            float ratio = getPressure(EnumFacing.UP) / maxPressure;
            //int ticks = (int) ((float) pNetwork.getLowestTier().ordinal() * 16 * ratio);
            // TODO CRUSHER used pressure based on the amount of it inside (same for speed)
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
    public ItemStack removeStackFromSlot(int index) {
        return inventory.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        inventory.setInventorySlotContents(slot, itemStack);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(getPos()) == this && player.getDistanceSq(getPos()) < 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

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
    public boolean canConnectTo(EnumFacing side) {
        return true;
    }

    @Override
    public boolean canWork(EnumFacing dir) {
        if (getNetwork(dir) == null) {
            return false;
        }
        return dir.equals(EnumFacing.UP);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public String getName() {
        return Localization.getLocalizedName(Names.blockHydraulicCrusher.unlocalized);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
    public void onFluidLevelChanged(int old) {
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
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, getPos().getX(), getPos().getY(), getPos().getZ(), stack));
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return inventory.canInsertItem(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return inventory.canExtractItem(index, stack);
    }
}
