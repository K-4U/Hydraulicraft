package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidCraftingMachine;
import k4unl.minecraft.Hydraulicraft.api.recipes.IFluidRecipe;
import k4unl.minecraft.Hydraulicraft.lib.recipes.InventoryFluidCrafting;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Orientation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileHydraulicFilter extends TileHydraulicBase implements
  IInventory, IFluidHandler, IHydraulicConsumer, IFluidCraftingMachine {

    private ItemStack outputInventory;

    InventoryFluidCrafting    inventoryCrafting;
    IFluidRecipe              recipe;

    private int     maxTicks         = 500;
    private int     ticksDone        = 0;

    private FluidTank inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
    private FluidTank outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 8);

    public TileHydraulicFilter() {

        super(6);
        super.init(this);
        FluidTank[] inputTanks = new FluidTank[1];
        FluidTank[] outputTanks = new FluidTank[1];

        inputTanks[0] = inputTank;
        outputTanks[0] = outputTank;

        inventoryCrafting = new InventoryFluidCrafting(this, 2, inputTanks, outputTanks);
    }

    private boolean canRun() {
        if(maxTicks == 0) return false;
        if(recipe == null) return false;
        FluidTankInfo[] info = inventoryCrafting.getTankInfo();
        if(info[0].fluid == null) return false;
        if(info[0].fluid.amount < recipe.getInputFluids().get(0).amount) return false;
        if(info[1].fluid != null && info[1].fluid.amount + recipe.getOutputFluids().get(0).amount > info[1].capacity) return false;
        return true;
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from) {

        if (canRun()) {
            if (!simulate) {
                doConvert();
            }
            if(recipe == null) return 0F;
            return recipe.getPressure();
        } else {
            return 0F;
        }
    }

    public void doConvert() {

        inventoryCrafting.craftingDrain(recipe.getInputFluids().get(0), true);
        inventoryCrafting.craftingFill(recipe.getOutputFluids().get(0), true);

        ticksDone--;
        if(ticksDone == 0){
            //Output the item
            inventoryCrafting.setInventorySlotContents(1, recipe.getRecipeOutput().copy());
            recipe = null;
        }
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		return inventoryCrafting.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return inventoryCrafting.decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return inventoryCrafting.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		inventoryCrafting.setInventorySlotContents(i, itemStack);
	}

	@Override
	public int getInventoryStackLimit() {
		return inventoryCrafting.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this) && 
				player.getDistanceSq(xCoord, yCoord, zCoord) < 64);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return inventoryCrafting.isItemValidForSlot(i, itemStack);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return inventoryCrafting.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		
		return inventoryCrafting.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return inventoryCrafting.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {

        Orientation orientation = Orientation.calculateOrientation(from, getBlockMetadata());
        if(orientation == Orientation.LEFT){
            return inventoryCrafting.canFill(fluid);
        }else{
            return false;
        }
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
        Orientation orientation = Orientation.calculateOrientation(from, getBlockMetadata());
        if(orientation == Orientation.RIGHT || orientation == Orientation.LEFT){
            return inventoryCrafting.canDrain(fluid);
        }else{
            return false;
        }
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {

        Orientation orientation = Orientation.calculateOrientation(from, getBlockMetadata());

        if(orientation == Orientation.LEFT){
            return new FluidTankInfo[] {inventoryCrafting.getTankInfo()[0]};
        }else if(orientation == Orientation.RIGHT){
            return new FluidTankInfo[] {inventoryCrafting.getTankInfo()[1]};
        }
        return inventoryCrafting.getTankInfo();
	}


	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

        inventoryCrafting.load(tagCompound);

		ticksDone = tagCompound.getInteger("ticksDone");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

        inventoryCrafting.save(tagCompound);

		tagCompound.setInteger("ticksDone", ticksDone);
	}

	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public void onFluidLevelChanged(int old) {}
	
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
	
	public float getScaledFilterTime() {
		if(maxTicks > 0){
			return (float)ticksDone / (float)maxTicks;			
		}else{
			return 0;
		}
		
	}

	@Override
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicFilter.unlocalized);
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}
	
	@Override
	public void onBlockBreaks() {
		dropItemStackInWorld(inventoryCrafting.getStackInSlot(0));
	}

    @Override
    public void onCraftingMatrixChanged() {
        if(ticksDone == 0 || recipe == null) {
            recipe = HydraulicRecipes.getFilterRecipe(inventoryCrafting);
            if (recipe != null) {
                ticksDone = maxTicks;
                //Consume the item
                inventoryCrafting.decrStackSize(0, 1);
            }
        }
    }

    @Override
    public void spawnOverflowItemStack(ItemStack stack) {
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, stack));
    }

    public int getFilterTicks() {

        return ticksDone;
    }

    public float getMaxFilterTicks() {

        return maxTicks;
    }
}
