package k4unl.minecraft.Hydraulicraft.TileEntities.consumers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.baseClasses.IMachineMultiBlock;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.WashingRecipes.WashingRecipe;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
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

public class TileHydraulicWasher extends TileEntity implements
		ISidedInventory, IFluidHandler, IHydraulicConsumer, IMachineMultiBlock {
	private ItemStack inputInventory;
	private ItemStack washingItem;
	private ItemStack targetItem;
	private ItemStack outputInventory;
	private ItemStack fluidInputInventory;
	private ItemStack fluidOutputInventory;
	private int washingTicks = 0;
	private int maxWashingTicks = 0;
	private float requiredPressure = 5F;
	private IBaseClass baseHandler;
	
	private boolean isValidMultiblock;
	
	private PressureNetwork pNetwork;
	private List<TileHydraulicValve> valves;
	private List<ForgeDirection> connectedSides;
	private TileInterfaceValve fluidValve;
	private TileInterfaceValve itemValve;
	private int tier = 0;
	private float pressurePerTick = 0F;
	
	
	public TileHydraulicWasher(){
		connectedSides = new ArrayList<ForgeDirection>();
		valves = new ArrayList<TileHydraulicValve>();
	}
	
	public boolean getIsValidMultiblock(){
		return isValidMultiblock;
	}
	
	private FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 50);
	
	
	public int getWashingTicks(){
		return washingTicks;
	}

	public boolean isWashing() {
		return (washingItem != null && targetItem != null);
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
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun() || isWashing()){
			if(!simulate){
				doWash();
			}
			//The higher the pressure
			//The higher the speed!
			//But also the more it uses..
			return 0.1F + pressurePerTick + (1+(getPressure(from) / getMaxPressure(getHandler().isOilStored(), from)));
		}else{
			return 0F;
		}
	}
	
	
	private void doWash(){
		if(isWashing()){
			washingTicks = washingTicks + 1 + (int)((getPressure(ForgeDirection.UNKNOWN)/100) * 0.0005F);
			if(washingTicks >= maxWashingTicks){
				//washing done!
				if(outputInventory == null){
					outputInventory = targetItem.copy(); 
				}else{
					outputInventory.stackSize += targetItem.stackSize;
				}
				tank.drain(Constants.MIN_REQUIRED_WATER_FOR_WASHER, true);
				
				washingItem = null;
				targetItem = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}else{
			if(canRun()){
				WashingRecipe recipe = WashingRecipes.getWashingRecipe(inputInventory);
				targetItem = recipe.getOutput();
				pressurePerTick = recipe.pressure;
				if(new Random().nextFloat() > 0.70F) {
                	targetItem.stackSize+=1;
                }
				washingItem = inputInventory.copy();
				
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				washingTicks = 0;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			//Start washing
			maxWashingTicks = 200;
		}
	}
	
	public ItemStack getWashingItem(){
		return washingItem;
	}
	
	public ItemStack getTargetItem(){
		return targetItem;
	}
	
	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(!getIsValidMultiblock()){
			return false;
		}
		if(inputInventory == null || (getPressure(ForgeDirection.UNKNOWN) < requiredPressure) || tank.getFluidAmount() < Constants.MIN_REQUIRED_WATER_FOR_WASHER){
			return false;
		}else{
			//Get smelting result:
			//ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inputInventory);
			ItemStack target = WashingRecipes.getWashingRecipeOutput(inputInventory);
			if(target == null) return false;
			if(outputInventory != null){
				if(!outputInventory.isItemEqual(target)) return false;
				int newItemStackSize = outputInventory.stackSize + target.stackSize;
				
				return (newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize());
			}else{
				return true;
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		switch(i){
		case 0:
			return inputInventory;
		case 1:
			return outputInventory;
		case 2:
			return fluidInputInventory;
		case 3:
			return fluidOutputInventory;
		default:
			return null;
			
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(i == 0){
			ItemStack ret = null;
			if(inputInventory.stackSize < j){
				ret = inputInventory;
				inputInventory = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}else{
				ret = inputInventory.splitStack(j);
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}
		}else if(i == 1){
			ItemStack ret = null;
			if(outputInventory.stackSize < j){
				ret = outputInventory;
				outputInventory = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}else{
				ret = outputInventory.splitStack(j);
				if(outputInventory.stackSize <= 0){
					outputInventory = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}
		}else if(i == 2){
			ItemStack ret = null;
			if(fluidInputInventory.stackSize < j){
				ret = fluidInputInventory;
				fluidInputInventory = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}else{
				ret = fluidInputInventory.splitStack(j);
				if(fluidInputInventory.stackSize <= 0){
					fluidInputInventory = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}
		}else{
			ItemStack ret = null;
			if(fluidOutputInventory.stackSize < j){
				ret = fluidOutputInventory;
				fluidOutputInventory = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}else{
				ret = fluidOutputInventory.splitStack(j);
				if(fluidOutputInventory.stackSize <= 0){
					fluidOutputInventory = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return ret;
			}
		}
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
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if(i == 1){
			outputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if(i == 2){
			fluidInputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if(i == 3){
			fluidOutputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public String getInvName() {
		return Names.blockHydraulicWasher.unlocalized;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
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
		if(i == 0 && Config.canBeWashed(itemStack)){
			return true;
		}else if(i == 2){
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
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {1, 0};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i == 0 && Config.canBeWashed(itemStack)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(i == 1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 10;
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		int filled = tank.fill(resource, doFill); 
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
		FluidStack drained = tank.drain(maxDrain, doDrain); 
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
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
		return tankInfo;
		
	}

	@Override
	public void onBlockBreaks() {
		getHandler().dropItemStackInWorld(inputInventory);
		getHandler().dropItemStackInWorld(outputInventory);
	}

	@Override
	public void onInventoryChanged() {
		if(fluidInputInventory != null){
			FluidStack input = FluidContainerRegistry.getFluidForFilledItem(fluidInputInventory);
			if(fill(ForgeDirection.UNKNOWN, input, false) == input.amount){
				Item outputI = fluidInputInventory.getItem().getContainerItem();
				if(outputI != null && fluidOutputInventory != null){
					ItemStack output = new ItemStack(outputI);
					if(fluidOutputInventory.isItemEqual(output)){
						if(fluidOutputInventory.stackSize < output.getMaxStackSize()){
							fluidOutputInventory.stackSize += output.stackSize;
						}else{
							return;
						}
					}else{
						return;
					}
				}else if(fluidOutputInventory == null && outputI != null){
					fluidOutputInventory = new ItemStack(outputI);
				}else if(outputI == null){
					
				}else{
					return;
				}
				fill(ForgeDirection.UNKNOWN, input, true);
				
				decrStackSize(2, 1);
			}
		}
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			switch(tier){
			case 0:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_OIL_TIER_3;
			default:
				return 0F;
			}
			
		}else{
			switch(tier){
			case 0:
				return Constants.MAX_MBAR_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_WATER_TIER_3;
			default:
				return 0F;
			}
		}
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
		
		tank.readFromNBT(tagCompound.getCompoundTag("tank"));
		
		washingTicks = tagCompound.getInteger("washingTicks");
		
		washingItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("washingItem"));
		targetItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("targetItem"));
		
		isValidMultiblock = tagCompound.getBoolean("isValidMultiblock");
		
		tier = tagCompound.getInteger("tier");
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
		
		if(washingItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			washingItem.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("washingItem", inventoryCompound);
		}
		
		if(targetItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			targetItem.writeToNBT(inventoryCompound);
			tagCompound.setCompoundTag("targetItem", inventoryCompound);
		}
		
		NBTTagCompound tankCompound = new NBTTagCompound();
		tank.writeToNBT(tankCompound);
		tagCompound.setCompoundTag("tank", tankCompound);
		
		tagCompound.setInteger("washingTicks",washingTicks);
		
		tagCompound.setBoolean("isValidMultiblock",isValidMultiblock);
		tagCompound.setInteger("tier", tier);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		getHandler().onDataPacket(net, packet);
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
	}

	@Override
	public void updateEntity() {
		getHandler().updateEntity();
		if(!worldObj.isRemote){
			if(worldObj.getTotalWorldTime() % 10 == 0 && !getIsValidMultiblock()){
				if(checkMultiblock()){
					isValidMultiblock = true;
					convertMultiblock();
				}
			}
		}else{
			if(isValidMultiblock && valves.size() == 0){
				convertMultiblock();
			}
		}
	}

	public void invalidateMultiblock() {
		int dir = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		
		for(int horiz = -1; horiz <= 1; horiz++) {
			for(int vert = -1; vert <= 1; vert++) {
				for(int depth = 0; depth <= 2; depth++)	{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
					
					int blockId = worldObj.getBlockId(x, y, z);
					/*
					if(horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;*/
					
					if(blockId == Ids.blockValve.act){
						TileHydraulicValve temp = (TileHydraulicValve) worldObj.getBlockTileEntity(x, y, z);
						temp.resetTarget();
					}
					//if(blockId != Ids.blockDummyWasher.act)
					//	continue;
					
					//worldObj.setBlock(x, y, z, Ids.blockHydraulicPressureWall.act);
					worldObj.markBlockForUpdate(x, y, z);
				}
			}
		}
		isValidMultiblock = false;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public boolean checkMultiblock(){
		//So, it should be this:
		// 1       2      3
		//W W W  W W W  W W W
		//W W W  W F W  W W W
		//W W W  W C W  W W W
		
		int dir = worldObj.getBlockMetadata(xCoord, yCoord,zCoord);
		
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		
		boolean hasAtLeastOneValve = false;
		boolean hasFluidValve = false;
		boolean hasItemValve = false;
		for(int horiz = -1; horiz <= 1; horiz++) {
			for(int vert = -1; vert <= 1; vert++){
				for(int depth = 0; depth <= 2; depth++) {
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
					
					int blockId = worldObj.getBlockId(x, y, z);
					int meta = worldObj.getBlockMetadata(x, y, z);
					
					if(horiz == 0 && vert == 0){
						if(depth == 0){ //Looking at self.
							continue;
						}
						
						if(depth == 1)	{
							if(blockId != Ids.blockCore.act){
								return false;
							}else{
								tier = meta;
								continue;
							}
						}
					}
					
					if(blockId != Ids.blockHydraulicPressureWall.act){
						if(blockId == Ids.blockValve.act){
							hasAtLeastOneValve = true;
						}else if(blockId == Ids.blockInterfaceValve.act){
							if(hasFluidValve = true){
								hasItemValve = true;
							}else{
								hasFluidValve = true;
							}
						}else{
							return false;
						}
					}
				}
			}
		}
		return (hasAtLeastOneValve && hasFluidValve && hasItemValve);
	}
	
	public void convertMultiblock(){
		isValidMultiblock = true;
		
		int dir = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		
		int depthMultiplier = ((dir == 2 || dir == 4) ? 1 : -1);
		boolean forwardZ = ((dir == 2) || (dir == 3));
		valves.clear();
		
		for(int horiz = -1; horiz <= 1; horiz++) {
			for(int vert = -1; vert <= 1; vert++) {
				for(int depth = 0; depth <= 2; depth++)	{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
					int blockId = worldObj.getBlockId(x, y, z);
					
					if(horiz == 0 && vert == 0 && depth == 0)
						continue;
					
					if(blockId == Ids.blockValve.act){
						TileHydraulicValve dummyTE = (TileHydraulicValve)worldObj.getBlockTileEntity(x, y, z);
						dummyTE.setTarget(xCoord, yCoord, zCoord);
						valves.add(dummyTE);
						dummyTE.getHandler().updateNetworkOnNextTick(0);
					}
					if(blockId == Ids.blockInterfaceValve.act){
						TileInterfaceValve dummyTE = (TileInterfaceValve)worldObj.getBlockTileEntity(x, y, z);
						dummyTE.setTarget(xCoord, yCoord, zCoord);
						if(fluidValve == null){
							fluidValve = dummyTE;
						}else{
							itemValve = dummyTE;
						}
					}
					
					//worldObj.setBlock(x, y, z, Ids.blockDummyWasher.act);
					//worldObj.markBlockForUpdate(x, y, z);
					//TileDummyWasher dummyTE = (TileDummyWasher)worldObj.getBlockTileEntity(x, y, z);
					//dummyTE.setCore(xCoord, yCoord, zCoord);
					
					//dummyTE.setLocationInMultiBlock(dir, horiz, vert, depth);
					worldObj.markBlockForUpdate(x, y, z);
					
				}
			}
		}
		float p = 0;
		if(pNetwork != null){
			getPressure(ForgeDirection.UP);
		}
		getHandler().updateNetworkOnNextTick(p);
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
		if(isValidMultiblock){
			convertMultiblock();
		}
	}
	
	@Override
	public float getPressure(ForgeDirection from) {
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("Harvester at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
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
		if(!isValidMultiblock){
			getHandler().updateNetworkOnNextTick(oldPressure);
			return;
		}else{
			if(valves.size() > 0){

			}else{
				getHandler().updateNetworkOnNextTick(oldPressure);	
			}
			
			
		}
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		this.invalidateMultiblock();
		if(!worldObj.isRemote){
			for(ForgeDirection dir: connectedSides){
				if(getNetwork(dir) != null){
					getNetwork(dir).removeMachine(this);
				}
			}
		}
	}

	@Override
	public List<TileHydraulicValve> getValves() {
		return valves;
	}
	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(worldObj.isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return 0;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(worldObj.isRemote){
			//TODO: Store this in a variable locally. Mostly important for pumps though.
			return 0;
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}
}
