package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileInfiniteSource extends TileHydraulicBase implements IFluidHandler,
		IInventory, IHydraulicGenerator {
	private ItemStack itemInventory;
	private FluidTank tank = new FluidTank(2000);

	public TileInfiniteSource(){
		super(PressureTier.HIGHPRESSURE, 1);
		super.init(this);
	}
	
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return itemInventory;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return itemInventory;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		itemInventory = var2;
	}

	@Override
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockInfiniteSource.unlocalized);
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return (itemInventory != null);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, false);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
		return tankInfo;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
		if(tankCompound != null){
			tank.readFromNBT(tankCompound);
		}
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inventory");
		itemInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		if(tank != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			tank.writeToNBT(inventoryCompound);
			tagCompound.setTag("tank", inventoryCompound);
		}
		
		if(itemInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			itemInventory.writeToNBT(inventoryCompound);
			tagCompound.setTag("inventory", inventoryCompound);
		}
	}

	@Override
	public void onFluidLevelChanged(int old) { }

	@Override
	public void workFunction(ForgeDirection from) {
        getHandler().setStored(getNetwork(from).getFluidCapacity() - getNetwork(from).getFluidInNetwork(), true, true);
        getHandler().setPressure(getMaxPressure(getHandler().isOilStored(), from), from);
        getHandler().updateFluidOnNextTick();
	}

	@Override
	public int getMaxGenerating(ForgeDirection from) {
		return 0;
	}

	@Override
	public float getGenerating(ForgeDirection from) {
		return 0;
	}

	
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}
}
