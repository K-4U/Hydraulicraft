package k4unl.minecraft.Hydraulicraft.TileEntities.generator;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicPump extends TileEntity implements IInventory, IHydraulicGenerator {
	private ItemStack inventory;
	private int currentBurnTime;
	private int maxBurnTime;
	private boolean isBurning = false;
	private IBaseClass baseHandler;
	
	private int tier = -1;
	
	private PressureNetwork pNetwork;
	private List<ForgeDirection> connectedSides;
	private ForgeDirection facing = ForgeDirection.UNKNOWN;
	
	private int fluidInNetwork;
	private int networkCapacity;
	
	public TileHydraulicPump(){
		connectedSides = new ArrayList<ForgeDirection>();
	}
	
	@Override
	public void updateEntity(){
		getHandler().updateEntity();
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
	public void workFunction(ForgeDirection from) {
		if(from.equals(ForgeDirection.UP)){
			//This function gets called every tick.
			//It should check how much coal is left
			//How long that stuff burns
			//And how long it has left to burn.
			boolean needsUpdate = false;
			isBurning = (currentBurnTime > 0);
			if(isBurning){
				currentBurnTime --;
				needsUpdate = true;
				float gen = getGenerating(from);
				if(gen > 0){
					setPressure(gen + getPressure(from), from);
				}
			}
			if(!worldObj.isRemote){
				if(currentBurnTime <= 0 && TileEntityFurnace.isItemFuel(inventory) && getPressure(from) < getMaxPressure(getHandler().isOilStored(), from)){
					//Put new item in
					currentBurnTime = maxBurnTime = TileEntityFurnace.getItemBurnTime(inventory)+1;
					if(inventory != null){
						inventory.stackSize--;
						if(inventory.stackSize <= 0){
							inventory = null;
						}
						needsUpdate = true;
					}
				}
				
			}
			
			if(needsUpdate){
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	@Override
	public int getMaxGenerating(ForgeDirection from) {
		if(!getHandler().isOilStored()){
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_GEN_WATER_TIER_1;
			case 1:
				return Constants.MAX_MBAR_GEN_WATER_TIER_2;
			case 2:
				return Constants.MAX_MBAR_GEN_WATER_TIER_3;
			}			
		}else{
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_GEN_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_GEN_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_GEN_OIL_TIER_3;
			}
		}
		return 0;
	}

	public float getBurningPercentage() {
		if(maxBurnTime > 0){
			return ((float)currentBurnTime / (float)maxBurnTime);
		}else{
			return 0;
		}
	}
	
	public boolean getIsBurning() {
		return (currentBurnTime > 0);
	}

	@Override
	public float getGenerating(ForgeDirection from) {
		if(getIsBurning()){
			//We can only generate at the percentage the system is filled at.
			float perc = (float)getHandler().getStored() / (float) getMaxStorage();
			//Also.. we can only go to a max of which the system is filled at.
			//So, if the system is 50% full, we only generate at 50% and we can only
			//go to 50% of the max pressure.
			
			float generating = perc;
			float currentPressure = getPressure(ForgeDirection.UNKNOWN);
			float maxPressure = getMaxPressure(getHandler().isOilStored(), ForgeDirection.UNKNOWN);
			
			if(getFluidInNetwork(from) > 0){
				generating = generating * ((float)getFluidInNetwork(from) / (float)getFluidCapacity(from));
				generating = generating * getMaxGenerating(ForgeDirection.UP);
			}else{
				generating = 0;
			}
			if(generating > getMaxGenerating(ForgeDirection.UP))
				generating = getMaxGenerating(ForgeDirection.UP);
			
			if(generating + currentPressure <= (perc * maxPressure)){
				return generating;
			}else{
				generating = (perc * maxPressure) - currentPressure;
				if(generating < 0){
					generating = 0;
				}
				if(generating + currentPressure <= (perc * maxPressure)){
					return generating;
				}else{
					return 0;
				}
			}
		}else{
			return 0;
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(i == 0){
			return inventory;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(i < 1){
			ItemStack ret = null;
			if(inventory.stackSize < j){
				ret = inventory;
				inventory = null;
				
			}else{
				ret = inventory.splitStack(j);
				if(inventory.stackSize == 0){
					inventory = null;
				}
			}
			
			return ret;
			
		}else{
			return null;
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
		if(i < 1){
			inventory = itemStack;
		}
	}
	
    public int getTier(){
    	if(tier == -1 && worldObj != null){
			tier = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		}
		return tier;
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
		if(i < 1){
			if(TileEntityFurnace.isItemFuel(itemStack)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * (2 * (getTier() + 1));
	}

	@Override
	public void onBlockBreaks() {
		getHandler().dropItemStackInWorld(inventory);
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			switch(getTier()){
			case 0:
				return Constants.MAX_MBAR_OIL_TIER_1;
			case 1:
				return Constants.MAX_MBAR_OIL_TIER_2;
			case 2:
				return Constants.MAX_MBAR_OIL_TIER_3;
			}			
		}else{
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
		super.readFromNBT(tagCompound);
		
		
		setTier(tagCompound.getInteger("tier"));
		
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inventory");
		inventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		currentBurnTime = tagCompound.getInteger("currentBurnTime");
		maxBurnTime = tagCompound.getInteger("maxBurnTime");
		
		networkCapacity = tagCompound.getInteger("networkCapacity");
		fluidInNetwork = tagCompound.getInteger("fluidInNetwork");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		
	}

	private void setTier(int newTier) {
		tier = newTier;
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		if(inventory != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			inventory.writeToNBT(inventoryCompound);
			tagCompound.setTag("inventory", inventoryCompound);
		}
		tagCompound.setInteger("currentBurnTime",currentBurnTime);
		tagCompound.setInteger("maxBurnTime",maxBurnTime);
		
		tagCompound.setInteger("tier", getTier());
		tagCompound.setInteger("facing", facing.ordinal());
		
		if(pNetwork != null){
			tagCompound.setInteger("networkCapacity", getNetwork(ForgeDirection.UP).getFluidCapacity());
			tagCompound.setInteger("fluidInNetwork", getNetwork(ForgeDirection.UP).getFluidInNetwork());
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		getHandler().onDataPacket(net, packet);
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
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
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("Pump at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
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
			pNetwork.addMachine(this, oldPressure, ForgeDirection.UP);
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure, ForgeDirection.UP);
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		if(!worldObj.isRemote){
			for(ForgeDirection dir: connectedSides){
				if(getNetwork(dir) != null){
					getNetwork(dir).removeMachine(this);
				}
			}
		}
	}
	
	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(worldObj.isRemote){
			return fluidInNetwork;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(worldObj.isRemote){
			if(networkCapacity > 0){
				return networkCapacity;
			}else{
				return getMaxStorage();
			}
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}

	public ForgeDirection getFacing(){
		return facing;		
	}
	
	public void setFacing(ForgeDirection newDir){
		facing = newDir;
	}

	@Override
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicPump[getTier()].unlocalized);
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
