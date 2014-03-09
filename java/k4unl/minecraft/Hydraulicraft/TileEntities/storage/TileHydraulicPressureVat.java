package k4unl.minecraft.Hydraulicraft.TileEntities.storage;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.blocks.Blocks;
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
	private IBaseClass baseHandler;

	private PressureNetwork pNetwork;
	
	private FluidTank tank = null;//
	private int tier = -1;
	private List<ForgeDirection> connectedSides;
	
	public TileHydraulicPressureVat(){
		connectedSides = new ArrayList<ForgeDirection>();
	}
	
	public void setTier(int tier){
		this.tier = tier;
		if(tank == null){
			Log.info("Started a new tank");
			tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * (16 * (tier+1)));
		}
		
	}
	
	
	public int getTier() {
		if(tier == -1 && worldObj != null){
			tier = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		}
		return tier;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		getHandler().onDataPacket(net, packet);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		return getHandler().getDescriptionPacket();
	}
	
	
	public void newFromNBT(NBTTagCompound tagCompound){
		getHandler().readFromNBT(tagCompound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		getHandler().readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
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
			//Functions.checkAndFillSideBlocks(worldObj, xCoord, yCoord, zCoord);
			//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if((getHandler().getFluidInSystem() + resource.amount) < getHandler().getTotalFluidCapacity()){
			if(doFill){
				getHandler().updateFluidOnNextTick();
				//Functions.checkAndSetSideBlocks(worldObj, xCoord, yCoord, zCoord, getHandler().getFluidInSystem() + resource.amount, getHandler().isOilStored());
			}
			filled = resource.amount;
		}else if(getHandler().getFluidInSystem() < getHandler().getTotalFluidCapacity()) {
			if(doFill){
				getHandler().updateFluidOnNextTick();
				//Functions.checkAndSetSideBlocks(worldObj, xCoord, yCoord, zCoord, getHandler().getTotalFluidCapacity(), getHandler().isOilStored());
			}
			filled = getHandler().getTotalFluidCapacity() - getHandler().getFluidInSystem();
		}else{
			//filled = 0;
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
	public int getStored(ForgeDirection from) {
		if(tank == null){
			return 0;
		}
		return tank.getFluidAmount();
	}

	@Override
	public void setStored(int i, boolean isOil) {
		if(isOil){
			tank.setFluid(new FluidStack(Fluids.fluidOil, i));
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
		ItemStack ourEnt = new ItemStack(Blocks.hydraulicPressurevat, 1, getTier());
		NBTTagCompound tCompound = new NBTTagCompound();
		writeToNBT(tCompound);
		tCompound.removeTag("x");
		tCompound.removeTag("y");
		tCompound.removeTag("z");
		tCompound.removeTag("id");
		
		ourEnt.setTagCompound(tCompound);
		getHandler().dropItemStackInWorld(ourEnt);
	}

	@Override
    public float getMaxPressure(boolean isOil, ForgeDirection from){
        if(isOil) {
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
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
        return baseHandler;
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		setTier(tagCompound.getInteger("tier"));
		
		
		NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
		if(tankCompound != null){
			tank.readFromNBT(tankCompound);
		}
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
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
		
		tagCompound.setInteger("tier", tier);
		
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
	
	@Override
	public void validate(){
		super.validate();
		getHandler().validate();
	}

	@Override
	public void onPressureChanged(float old) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFluidLevelChanged(int old) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public PressureNetwork getNetwork(ForgeDirection side) {
		return pNetwork;
	}

	@Override
	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		pNetwork = toSet;
	}

	@Override
	public void firstTick() {
		//setTier();
	}
	
	@Override
	public float getPressure(ForgeDirection from) {
		if(getNetwork(from) == null){
			return 0;
		}
		return getNetwork(from).getPressure();
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		getNetwork(side).setPressure(newPressure);
	}
	
	@Override
	public void updateNetwork(float oldPressure) {
		PressureNetwork newNetwork = null;
		PressureNetwork foundNetwork = null;
		PressureNetwork endNetwork = null;
		//This block can merge networks!
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			foundNetwork = PressureNetwork.getNetworkInDir(worldObj, xCoord, yCoord, zCoord, dir);
			if(foundNetwork != null){
				if(endNetwork == null){
					endNetwork = foundNetwork;
				}else{
					newNetwork = foundNetwork;
				}
				connectedSides.add(dir);
			}
			
			if(newNetwork != null && endNetwork != null){
				//Hmm.. More networks!? What's this!?
				endNetwork.mergeNetwork(newNetwork);
				newNetwork = null;
			}
		}
			
		if(endNetwork != null){
			pNetwork = endNetwork;
			pNetwork.addMachine(this, oldPressure);
			Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure);
			Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		for(ForgeDirection dir: connectedSides){
			getNetwork(dir).removeMachine(this);
		}
	}
}
