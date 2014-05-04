package k4unl.minecraft.Hydraulicraft.tileEntities.storage;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHydraulicStorageWithTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileHydraulicPressureVat extends TileHydraulicBase implements IInventory, IFluidHandler, IHydraulicStorageWithTank {
	private ItemStack inputInventory;
	private ItemStack outputInventory;

	
	private FluidTank tank = null;//
	private int tier = -1;
	private int prevRedstoneLevel = 0;
	
	public TileHydraulicPressureVat(){
		super(PressureTier.HIGHPRESSURE, 48);
		super.init(this);
		if(tank == null){
			tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
		}
	}
	
	public TileHydraulicPressureVat(int _tier){
		super(PressureTier.fromOrdinal(_tier), 16 * (_tier+1));
		super.init(this);
		tier = _tier;
		if(tank == null){
			tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier+1)));
		}
	}
	
	public void setTier(int newTier){
		super.setMaxStorage(16 * (tier + 1));
		super.setPressureTier(PressureTier.fromOrdinal(newTier));
		if(tank == null){
			tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier+1)));
		}
	}
	
	public int getTier() {
		return tier;
	}

	public void newFromNBT(NBTTagCompound tagCompound){
		readFromNBT(tagCompound);
	}


	@Override
	public int getSizeInventory() {
		return 2;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		switch(i){
		case 0:
			return inputInventory;
		case 1:
			return outputInventory;
		default:
			return null;
			
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack inventory = getStackInSlot(i);
		
		ItemStack ret = null;
		if(inventory.stackSize < j){
			ret = inventory;
			if(i == 0){
				inputInventory = null;
			}else if(i == 1){
				outputInventory = null;
			}
		}else{
			ret = inventory.splitStack(j);
			if(inventory.stackSize == 0){
				if(i == 0){
					inputInventory = null;
				}else if(i == 1){
					outputInventory = null;
				}
			}
		}
		
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack stack = getStackInSlot(i);
		if(stack != null){
			setInventorySlotContents(i, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		if(i == 0){
			inputInventory = itemStack;
			onInventoryChanged();
		}else{
			//Err...
			
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ((worldObj.getTileEntity(xCoord, yCoord, zCoord) == this) && 
				player.getDistanceSq(xCoord, yCoord, zCoord) < 64);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		if(i == 0){
			if(FluidContainerRegistry.isFilledContainer(itemStack)){
				if(FluidContainerRegistry.getFluidForFilledItem(itemStack).isFluidEqual(new FluidStack(FluidRegistry.WATER, 1))){
					return true;
				}else if(FluidContainerRegistry.getFluidForFilledItem(itemStack).isFluidEqual(new FluidStack(Fluids.fluidOil, 1))){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(worldObj.isRemote) return 0;
		if(resource == null) //What!?
			return 0;
		if(resource.getFluid() == null)
			return 0;
		
		if(tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0){
			if(resource.getFluid().getID() != tank.getFluid().getFluid().getID()){
				return 0;
			}
		}
		
		int filled = tank.fill(resource, doFill);
		if(resource.getFluid().getID() == Fluids.fluidOil.getID()){
			getHandler().setIsOilStored(true);
		}else{
			getHandler().setIsOilStored(false);
		}
		if(doFill && filled > 10){
			getHandler().updateFluidOnNextTick();
		}else if(getNetwork(from) != null){
			if((getNetwork(from).getFluidInNetwork() + resource.amount) < getNetwork(from).getFluidCapacity()){
				if(doFill){
					getHandler().updateFluidOnNextTick();
				}
				filled = resource.amount;
			}else if(getNetwork(from).getFluidInNetwork() < getNetwork(from).getFluidCapacity()) {
				if(doFill){
					getHandler().updateFluidOnNextTick();
				}
				filled = getNetwork(from).getFluidCapacity() - getNetwork(from).getFluidInNetwork();
			}
		}
		return filled;
		
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		FluidStack drained = tank.drain(maxDrain, doDrain); 
		if(doDrain && drained.amount > 0){
			//Functions.checkAndFillSideBlocks(worldObj, xCoord, yCoord, zCoord);
			getHandler().updateFluidOnNextTick();
		}
		
		return drained;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(fluid.getID() == FluidRegistry.WATER.getID() ||
				fluid.getID() == Fluids.fluidOil.getID()){
			return true;
		}else{
			return false;
		}
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
	public int getMaxStorage() {
		if(tank == null){
			return 0;
		}
		return tank.getCapacity();
	}

	@Override
	public int getStored() {
		if(tank == null){
			return 0;
		}
		return tank.getFluidAmount();
	}
	
	@Override
	public void setStored(int i, boolean isOil) {
		if(isOil){
			if(i == 0){
				tank.setFluid(null);
			}else{
				tank.setFluid(new FluidStack(Fluids.fluidOil, i));
			}
		}else{
			if(i == 0){
				tank.setFluid(null);
			}else{
				tank.setFluid(new FluidStack(FluidRegistry.WATER, i));
			}
			//Log.info("Fluid in tank: " + tank.getFluidAmount() + "x" + FluidRegistry.getFluidName(tank.getFluid().fluidID));
			//if(!worldObj.isRemote){
			
			//}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public void onBlockBreaks() {
		ItemStack ourEnt = new ItemStack(HCBlocks.hydraulicPressurevat, 1, getTier());
		NBTTagCompound tCompound = new NBTTagCompound();
		writeToNBT(tCompound);
		tCompound.removeTag("x");
		tCompound.removeTag("y");
		tCompound.removeTag("z");
		tCompound.removeTag("id");
		
		tCompound.setBoolean("hasBeenPlaced", true);
		tCompound.setInteger("maxStorage", getMaxStorage());
		tCompound.setFloat("maxPressure", getMaxPressure(getHandler().isOilStored(), ForgeDirection.UP));
		tCompound.setFloat("oldPressure", getPressure(ForgeDirection.UP));
		ourEnt.setTagCompound(tCompound);
		dropItemStackInWorld(ourEnt);
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		setTier(tagCompound.getInteger("tier"));
		prevRedstoneLevel = tagCompound.getInteger("prevRedstoneLevel");
		
		NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
		if(tankCompound != null){
			tank.readFromNBT(tankCompound);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if(inputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inputInventory.writeToNBT(inventoryCompound);
			tagCompound.setTag("inputInventory", inventoryCompound);
		}
		if(outputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			outputInventory.writeToNBT(inventoryCompound);
			tagCompound.setTag("outputInventory", inventoryCompound);
		}
		
		tagCompound.setInteger("tier", tier);
		tagCompound.setInteger("prevRedstoneLevel", prevRedstoneLevel);
		
		NBTTagCompound tankCompound = new NBTTagCompound();
		if(tank != null){
			tank.writeToNBT(tankCompound);
			tagCompound.setTag("tank", tankCompound);
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(getNetwork(ForgeDirection.UP) != null && worldObj != null){
			float curPressure = getPressure(ForgeDirection.UP);
			float maxPressure = getMaxPressure(getHandler().isOilStored(), ForgeDirection.UP);
			float percentage = curPressure / maxPressure;
			int newRedstoneLevel = (int)(15 * percentage);
			if(newRedstoneLevel != prevRedstoneLevel){
				prevRedstoneLevel = newRedstoneLevel;
				worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
				getHandler().updateBlock();
			}
		}
	}

	
	public void onInventoryChanged() {
		if(inputInventory != null){
			FluidStack input = FluidContainerRegistry.getFluidForFilledItem(inputInventory);
			if(fill(ForgeDirection.UNKNOWN, input, false) == input.amount){
				Item outputI = inputInventory.getItem().getContainerItem();
				if(outputI != null && outputInventory != null){
					ItemStack output = new ItemStack(outputI);
					if(outputInventory.isItemEqual(output)){
						if(outputInventory.stackSize < output.getMaxStackSize()){
							outputInventory.stackSize += output.stackSize;
						}else{
							return;
						}
					}else{
						return;
					}
				}else if(outputInventory == null && outputI != null){
					outputInventory = new ItemStack(outputI);
				}else if(outputI == null){
					
				}else{
					return;
				}
				fill(ForgeDirection.UNKNOWN, input, true);
				
				decrStackSize(0, 1);
				//Leave an empty container:
				
			}
		}
	}

	@Override
	public void onFluidLevelChanged(int old) {	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}
	
	public int getRedstoneLevel(){
		return prevRedstoneLevel;
	}

	@Override
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicPressurevat[getTier()].unlocalized);
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
}
