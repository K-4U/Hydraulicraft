package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.items.ItemDusts;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidCraftingMachine;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidInventory;
import k4unl.minecraft.Hydraulicraft.lib.recipes.InventoryFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.ItemStackUtils;
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

import java.util.Random;

public class TileHydraulicCrusher extends TileHydraulicBase implements IInventory, IHydraulicConsumer, IFluidInventory, IFluidCraftingMachine {

    private InventoryFluidCrafting    inventoryCrafting;
    private ItemStack                 crushingItem;
    private ItemStack                 targetItem;

    private IFluidRecipe recipe;

    private final float requiredPressure = 5F;
    private       int   crushingTicks    = 0;
    private       int   maxCrushingTicks = 0;
    private int oldScaledCrushTime;
    private float pressurePerTick = 0F;

    //TODO: Rewrite me to have a different slot as an output slot

    public TileHydraulicCrusher() {

        super(5);
        super.init(this);
        inventoryCrafting = new InventoryFluidCrafting(this, 2, null, null);
    }

    public int getCrushingTicks() {

        return crushingTicks;
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {

        if (canRun() || isCrushing()) {
            if (!simulate) {
                doCrush();
            }
            //The higher the pressure
            //The higher the speed!
            //But also the more it uses..

            return 0.1F + pressurePerTick;
        } else {
            return 0F;
        }
    }

    private void doCrush() {
        if (isCrushing()) {
            float maxPressureThisTier = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
            float ratio = getPressure(ForgeDirection.UP) / maxPressureThisTier;
            crushingTicks = crushingTicks + 1 + (int) ((pNetwork.getLowestTier().ordinal() * 4) * ratio);// + (int)(getPressure(ForgeDirection.UNKNOWN) / 1000 * 0.005F);

            if(crushingTicks >= maxCrushingTicks) {
                inventoryCrafting.setInventorySlotContents(1, ItemStackUtils.mergeStacks(inventoryCrafting.getStackInSlot(1), targetItem));
                targetItem = null;
                crushingItem = null;
                crushingTicks = 0;
            }
        } else {
            if(canRun()) {
                targetItem = recipe.getRecipeOutput().copy();
                if(new Random().nextFloat() > 0.80F) {
                	if(!(targetItem.getItem() instanceof ItemDusts)){
                		targetItem.stackSize+=1;
                	}
                }
                crushingItem = getStackInSlot(0).copy();

                inventoryCrafting.decrStackSize(0, 1);
                crushingTicks = 0;
                
                pressurePerTick = (Functions.getMaxGenPerTier(pNetwork.getLowestTier(), true) * 0.75F) * recipe.getPressure();
            }
            
        }
    }

    public ItemStack getCrushingItem(){
        return crushingItem;
    }

    public ItemStack getTargetItem(){
        return targetItem;
    }

    public boolean isCrushing(){
        return crushingItem != null && targetItem != null;
    }

    /*!
     * Checks if the outputslot is free, if there's enough pressure in the system
     * and if the item is Crusheable
     */
    private boolean canRun(){
        if(inventoryCrafting.getStackInSlot(0) == null) return false;
        if(getPressure(ForgeDirection.UNKNOWN) < requiredPressure) return false;
        if(recipe == null) return false;
        if(getStackInSlot(1) != null){
            ItemStack target = recipe.getRecipeOutput();
            if(!getStackInSlot(1).isItemEqual(target)) return false;
            int newItemStackSize = getStackInSlot(1).stackSize + target.stackSize + 1; //The random chance..
            if(getStackInSlot(1).getItem() instanceof ItemDusts){
                newItemStackSize--;
            }
            boolean ret = newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize();
            return ret;
        } else {
            return true;
        }
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
    public boolean isUseableByPlayer(EntityPlayer player){
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord, yCoord, zCoord) < 64;
    }

    @Override
    public int getInventoryStackLimit() {
        return inventoryCrafting.getInventoryStackLimit();
    }


    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return inventoryCrafting.isItemValidForSlot(slot, itemStack);
    }


    @Override
    public void onBlockBreaks(){
        dropItemStackInWorld(inventoryCrafting.getStackInSlot(0));
        dropItemStackInWorld(inventoryCrafting.getStackInSlot(1));
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote && oldScaledCrushTime != getScaledCrushTime()) {//TODO refactor it so updates only will be made when a player has a GUI open of this block.
            oldScaledCrushTime = getScaledCrushTime();
            getHandler().updateBlock();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
    	super.readFromNBT(tagCompound);
        NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");

        inventoryCompound = tagCompound.getCompoundTag("crushingItem");
        crushingItem = ItemStack.loadItemStackFromNBT(inventoryCompound);

        inventoryCompound = tagCompound.getCompoundTag("targetItem");
        targetItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
        
        crushingTicks = tagCompound.getInteger("crushingTicks");
        maxCrushingTicks = tagCompound.getInteger("maxCrushingTicks");

        inventoryCrafting.load(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
    	super.writeToNBT(tagCompound);

        if(crushingItem != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            crushingItem.writeToNBT(inventoryCompound);
            tagCompound.setTag("crushingItem", inventoryCompound);
        }
        if(targetItem != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            targetItem.writeToNBT(inventoryCompound);
            tagCompound.setTag("targetItem", inventoryCompound);
        }
        
        tagCompound.setInteger("crushingTicks", crushingTicks);
        tagCompound.setInteger("maxCrushingTicks", maxCrushingTicks);

        inventoryCrafting.save(tagCompound);
    }

    public int getScaledCrushTime(){
        return maxCrushingTicks == 0 || !isCrushing() ? 0 : 34 * crushingTicks / maxCrushingTicks;
    }

    
	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean canWork(ForgeDirection dir) {
		if(getNetwork(dir) == null){
			return false;
		}
		return dir.equals(ForgeDirection.UP);
	}
	
	@Override
	public void invalidate(){
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
	public void onFluidLevelChanged(int old) { }

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
    public InventoryCrafting getInventoryCrafting() {

        return null;
    }

    @Override
    public void eatFluids(IFluidRecipe recipe, float percent) {

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
        recipe = HydraulicRecipes.getCrusherRecipe(inventoryCrafting);
        if (recipe != null) {
            maxCrushingTicks = recipe.getCraftingTime();
            crushingTicks = 0;
        }
    }

    @Override
    public void spawnOverflowItemStack(ItemStack stack) {
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, stack));
    }
}
