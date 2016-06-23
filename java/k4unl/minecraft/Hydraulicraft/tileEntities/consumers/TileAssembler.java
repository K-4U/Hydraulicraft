package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;


import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
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
    public float workFunction(boolean simulate, EnumFacing from) {

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
    public boolean canWork(EnumFacing dir) {

        return dir.equals(EnumFacing.UP);
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

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
    public ItemStack removeStackFromSlot(int index) {

        return inventoryCrafting.removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {

        inventoryCrafting.setInventorySlotContents(slot, itemStack);
    }


    @Override
    public String getName() {

        return Names.blockHydraulicAssembler.unlocalized;
    }

    @Override
    public boolean hasCustomName() {

        return true;
    }

    @Override
    public ITextComponent getDisplayName() {

        return new TextComponentTranslation(Names.blockHydraulicAssembler.unlocalized);
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

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }


    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {

        if (slot < inventoryCrafting.getSizeInventory())
            return inventoryCrafting.isItemValidForSlot(slot, itemStack);

        // for slot = 9 = output slot return false
        return false;
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

    public InventoryFluidCrafting getFluidInventory() {

        return inventoryCrafting;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        inventoryCrafting.save(tagCompound);

        return tagCompound;
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

        worldObj.spawnEntityInWorld(new EntityItem(worldObj, getPos().getX(), getPos().getY(), getPos().getZ(), stack));
    }

    /* ***** IFLUIDHANDLER */

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {

        return inventoryCrafting.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {

        return inventoryCrafting.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {

        return inventoryCrafting.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {

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
    public int[] getSlotsForFace(EnumFacing side) {

        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

        return inventoryCrafting.canInsertItem(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

        return inventoryCrafting.canExtractItem(index, stack);

    }
}
