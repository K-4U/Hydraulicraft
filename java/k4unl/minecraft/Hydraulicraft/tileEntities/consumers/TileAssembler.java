package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;


import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.recipes.*;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileAssembler extends TileHydraulicBase implements IHydraulicConsumer, IFluidInventory, IFluidCraftingMachine {
    InventoryFluidCrafting inventoryCrafting;
    InventoryCraftResult   inventoryResult;

    public TileAssembler() {
        super(10);
        super.init(this);
        FluidTank[] tanks = new FluidTank[1];

        // TODO size of assembler's crafting tank
        tanks[0] = new FluidTank(4000);
        inventoryCrafting = new InventoryFluidCrafting(this, 3, tanks, null);
        inventoryResult = new InventoryCraftResult();
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {
        return 0;
    }

    @Override
    public boolean canWork(ForgeDirection dir) {
        return false;
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

    @Override
    public boolean drainFluid(FluidStack fluidStack, boolean pretend) {
        return inventoryCrafting.drainFluid(fluidStack, pretend);
    }

    @Override
    public boolean fillFluid(FluidStack fluidStack, boolean pretend) {
        return inventoryCrafting.fillFluid(fluidStack, pretend);
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
        //inventoryResult.setRecipe(HydraulicRecipes.getAssemblerRecipe(inventoryCrafting));
        IFluidRecipe recipe = HydraulicRecipes.getAssemblerRecipe(inventoryCrafting);
        if (recipe == null)
            inventoryResult.setInventorySlotContents(0, null);
        else
            inventoryResult.setInventorySlotContents(0, recipe.getRecipeOutput());
    }
}
