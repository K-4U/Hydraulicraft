package k4unl.minecraft.Hydraulicraft.TileEntities;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IBaseGenerator;
import k4unl.minecraft.Hydraulicraft.api.IBaseStorage;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorage;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileHydraulicPressureVat extends TileEntity implements IInventory, IFluidHandler, IHydraulicStorageWithTank {
	private ItemStack inputInventory;
	private ItemStack outputInventory;
	private IBaseStorage baseHandler;

	
	
	private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
	
	
	public TileHydraulicPressureVat(){
		
	}
	
	public void setTier(){
		tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (getTier()+1)));
	}
	
	
	public int getTier() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		getHandler().onDataPacket(net, packet);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		return getHandler().getDescriptionPacket();
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		getHandler().readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		getHandler().writeToNBT(tagCompound);
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
		}else{
			//Err...
			
		}
	}

	@Override
	public String getInvName() {
		// TODO Localization
		return Names.blockHydraulicPressurevat[getTier()].localized;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Localization
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return ((worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this) && 
				player.getDistanceSq(xCoord, yCoord, zCoord) < 64);
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
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
		int filled = tank.fill(resource, doFill);
		if(doFill && filled > 10){
			Functions.checkAndFillSideBlocks(worldObj, xCoord, yCoord, zCoord);
			//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if((getHandler().getFluidInSystem() + resource.amount) < getHandler().getTotalFluidCapacity()){
			if(doFill){
				Functions.checkAndSetSideBlocks(worldObj, xCoord, yCoord, zCoord, getHandler().getFluidInSystem() + resource.amount, getHandler().isOilStored());
			}
			filled = resource.amount;
		}else if(getHandler().getFluidInSystem() < getHandler().getTotalFluidCapacity()) {
			if(doFill){
				Functions.checkAndSetSideBlocks(worldObj, xCoord, yCoord, zCoord, getHandler().getTotalFluidCapacity(), getHandler().isOilStored());
			}
			filled = getHandler().getTotalFluidCapacity() - getHandler().getFluidInSystem();
		}else{
			filled = 0;
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
			Functions.checkAndFillSideBlocks(worldObj, xCoord, yCoord, zCoord);
			//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
		return tank.getCapacity();
	}

	@Override
	public int getStored() {
		return tank.getFluidAmount();
	}

	@Override
	public void setStored(int i, boolean isOil) {
		if(isOil){
			//tank.setFluid(new FluidStack(fluid, amount));
		}else{
			tank.setFluid(new FluidStack(FluidRegistry.WATER, i));
			//Log.info("Fluid in tank: " + tank.getFluidAmount() + "x" + FluidRegistry.getFluidName(tank.getFluid().fluidID));
			//if(!worldObj.isRemote){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			//}
		}
	}

	@Override
	public void onBlockBreaks() {
		
	}

	@Override
    public float getMaxPressure(){
        if(getHandler().isOilStored()) {
            switch(getTier()){
                case 0:
                    return Constants.MAX_MBAR_OIL_TIER_1;
                case 1:
                    return Constants.MAX_MBAR_OIL_TIER_2;
                case 2:
                    return Constants.MAX_MBAR_OIL_TIER_3;
            }
        } else {
            switch(getTier()){
                case 0:
                    return Constants.MAX_MBAR_WATER_TIER_1;
                case 1:
                    return Constants.MAX_MBAR_WATER_TIER_2;
                case 2:
                    return Constants.MAX_MBAR_WATER_TIER_3;
            }
        }
        return 0;
    }

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getStorageClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		tank.readFromNBT(tagCompound.getCompoundTag("tank"));
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		if(inputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("inputInventory", inventoryCompound);
		}
		if(outputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			outputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("outputInventory", inventoryCompound);
		}
		NBTTagCompound tankCompound = new NBTTagCompound();
		tank.writeToNBT(tankCompound);
		tagCompound.setCompoundTag("tank", tankCompound);	
	}

	@Override
	public void updateEntity() {
		getHandler().updateEntity();
		
	}

	@Override
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
}
