package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.*;

public class TileInfiniteSource extends TileHydraulicBase implements IFluidHandler, IInventory, IHydraulicMachine {
	private ItemStack itemInventory;
	private FluidTank tank = new FluidTank(20000);

	public TileInfiniteSource(){
		super(1);
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
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		itemInventory = var2;
	}

	@Override
	public String getName() {
		return Localization.getLocalizedName(Names.blockInfiniteSource.unlocalized);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentTranslation(Names.blockInfiniteSource.unlocalized);
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
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return (itemInventory != null);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource,
			boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, false);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		return new FluidTankInfo[]{new FluidTankInfo(tank)};
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
	public void onFluidLevelChanged(int old) {

	}

	@Override
	public boolean canConnectTo(EnumFacing side) {
		return false;
	}
}
