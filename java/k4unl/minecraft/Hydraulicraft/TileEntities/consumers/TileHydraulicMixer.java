package k4unl.minecraft.Hydraulicraft.TileEntities.consumers;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
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

public class TileHydraulicMixer extends TileEntity implements
		ISidedInventory, IFluidHandler, IHydraulicConsumer {

	private ItemStack inputInventory;
	//private ItemStack outputInventory;
	
	private boolean isWorking = false;
	private int maxTicks = 500;
	private int ticksDone = 0;
	private float requiredPressure = 5.0F;
	
	private FluidTank inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);
	private FluidTank outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 8);

	private IBaseClass baseHandler;

	private PressureNetwork pNetwork;
	private List<ForgeDirection> connectedSides;
	
	public TileHydraulicMixer(){
		connectedSides = new ArrayList<ForgeDirection>();
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
		super.readFromNBT(tagCompound);
		getHandler().readFromNBT(tagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		getHandler().writeToNBT(tagCompound);
	}

	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputInventory == null || (getPressure(ForgeDirection.UNKNOWN) < requiredPressure) || inputTank == null || inputTank.getFluid() == null){
			return false;
		}else{
			if(outputTank.getFluidAmount() + Constants.OIL_FOR_ONE_SEED < outputTank.getCapacity()){
				if(inputInventory.itemID == Item.seeds.itemID){
					if(inputTank.getFluid().isFluidEqual(new FluidStack(FluidRegistry.WATER.getID(),0)) && inputTank.getFluidAmount() > Constants.WATER_FOR_ONE_SEED){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun() || isWorking){
			if(!simulate){
				doConvert();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 5F + (getPressure(ForgeDirection.UNKNOWN) * 0.00005F);
		}else{
			return 0F;
		}
	}
	
	public void doConvert(){
		if(isWorking){
			ticksDone = ticksDone + 1 + (int)((getPressure(ForgeDirection.UNKNOWN)/100) * 0.00005F);
			//Log.info(ticksDone+ "");
			if(ticksDone >= maxTicks){
				if(outputTank.getFluidAmount() <= 0){
					outputTank.setFluid(new FluidStack(Fluids.fluidOil, Constants.OIL_FOR_ONE_SEED));
				}else{
					outputTank.getFluid().amount+=Constants.OIL_FOR_ONE_SEED;
				}
				isWorking = false;
			}
		}else{
			if(canRun()){
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				
				inputTank.drain(Constants.WATER_FOR_ONE_SEED, true);
				
				ticksDone = 0;
				isWorking = true;
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		switch(i){
		case 0:
			return inputInventory;
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
			inventory = null;
			
		}else{
			ret = inventory.splitStack(j);
			if(inventory.stackSize <= 0){
				if(i == 0){
					inputInventory = null;
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
		return Names.blockHydraulicMixer.localized;
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
			if(itemStack.itemID == Item.seeds.itemID){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {1, 0};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i == 0 && itemStack.itemID == Item.seeds.itemID){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 6;
	}


	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int filled = inputTank.fill(resource, doFill); 
		if(doFill && filled > 10){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
		FluidStack drained = outputTank.drain(maxDrain, doDrain); 
		if(doDrain && drained.amount > 0){
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		return drained;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(fluid.equals(FluidRegistry.WATER)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(fluid.equals(Fluids.fluidOil)){
			return true;			
		}else{
			return false;
		}
		
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] tankInfo = {new FluidTankInfo(inputTank), new FluidTankInfo(outputTank)};
		return tankInfo;
	}


	@Override
	public void onBlockBreaks() {
		getHandler().dropItemStackInWorld(inputInventory);
	}


	@Override
	public void onInventoryChanged() {
	}


	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
        return baseHandler;
	}


	@Override
	public void updateEntity() {
		getHandler().updateEntity();
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
//		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inputTank.readFromNBT(tagCompound.getCompoundTag("inputTank"));
		outputTank.readFromNBT(tagCompound.getCompoundTag("outputTank"));
		
		ticksDone = tagCompound.getInteger("ticksDone");
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		if(inputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("inputInventory", inventoryCompound);
		}
		/*if(outputInventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			outputInventory.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("outputInventory", inventoryCompound);
		}*/
		
		NBTTagCompound tankCompound = new NBTTagCompound();
		inputTank.writeToNBT(tankCompound);
		tagCompound.setCompoundTag("inputTank", tankCompound);
		
		tankCompound = new NBTTagCompound();
		outputTank.writeToNBT(tankCompound);
		tagCompound.setCompoundTag("outputTank", tankCompound);
		
		tagCompound.setInteger("ticksDone", ticksDone);
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

	}
	
	@Override
	public float getPressure(ForgeDirection from) {
		if(worldObj == null) return 0F;
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("Mixer at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
			return 0;
		}
		return getNetwork(from).getPressure();
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		getNetwork(side).setPressure(newPressure);
	}
	
	@Override
	public boolean canWork(ForgeDirection dir) {
		if(getNetwork(dir) == null){
			return false;
		}
		return dir.equals(ForgeDirection.UP);
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
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure);
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		for(ForgeDirection dir: connectedSides){
			getNetwork(dir).removeMachine(this);
		}
	}

	public float getScaledMixTime() {
		if(maxTicks > 0){
			return (float)ticksDone / (float)maxTicks;			
		}else{
			return 0;
		}
		
	}
}
