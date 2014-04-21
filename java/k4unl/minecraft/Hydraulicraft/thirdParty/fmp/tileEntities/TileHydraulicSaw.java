package k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileHydraulicSaw extends TileEntity implements IHydraulicConsumer, ISidedInventory {
	private ItemStack inputUpDownInventory;
	private ItemStack inputLeftRightInventory;
    private ItemStack sawUpDownItem;
    private ItemStack sawLeftRightItem;
    private ItemStack targetUpDownItem;
    private ItemStack targetLeftRightItem;
    private ItemStack outputUpDownInventory;
    private ItemStack outputLeftRightInventory;
    private boolean isSawingUpDown;
    private boolean isSawingLeftRight;
    
    private int sawingTicksUpDown = 0;
    private int maxSawingTicksUpDown = 0;
    
    private int sawingTicksLeftRight = 0;
    private int maxSawingTicksLeftRight = 0;
    
    private final float requiredPressure = 5F;
    private float pressurePerTick = 0F;
    private IBaseClass baseHandler;
	
    private PressureNetwork pNetwork;
    private List<ForgeDirection> connectedSides;
    private ItemStack saw = GameRegistry.findItemStack("ForgeMicroblock","sawDiamond", 1);
    
    
    public TileHydraulicSaw(){
    	connectedSides = new ArrayList<ForgeDirection>();
    }
	
	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 10;
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
	}
	
	public ForgeDirection getFacing(){
		if(worldObj != null){
			return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
		}else{
			return ForgeDirection.UNKNOWN;
		}
		
	}

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
        return baseHandler;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		getHandler().readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		getHandler().writeToNBT(tagCompound);
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
		inputUpDownInventory = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("inputUpDownInventory"));
		sawUpDownItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("sawUpDownItem"));
		sawLeftRightItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("sawLeftRightItem"));
		targetUpDownItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("targetUpDownItem"));
		targetLeftRightItem = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("targetLeftRightItem"));
		outputUpDownInventory = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("outputUpDownInventory"));
		outputLeftRightInventory = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("outputLeftRightInventory"));
		
		
	    isSawingUpDown = tagCompound.getBoolean("isSawingUpDown");
	    isSawingLeftRight = tagCompound.getBoolean("isSawingLeftRight");
	    
	    sawingTicksUpDown = tagCompound.getInteger("sawingTicksUpDown");
	    maxSawingTicksUpDown = tagCompound.getInteger("maxSawingTicksUpDown");
	    sawingTicksLeftRight = tagCompound.getInteger("sawingTicksLeftRight");
	    maxSawingTicksLeftRight = tagCompound.getInteger("maxSawingTicksLeftRight");
	}
	

	
	private void writeItemStack(NBTTagCompound tagCompound, String tag, ItemStack stack){
		if(stack != null){
			NBTTagCompound nTag = new NBTTagCompound();
			stack.writeToNBT(nTag);
			tagCompound.setTag(tag, nTag);
		}
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		writeItemStack(tagCompound, "inputUpDownInventory", inputUpDownInventory);
		writeItemStack(tagCompound, "inputLeftRightInventory", inputLeftRightInventory);
		writeItemStack(tagCompound, "sawUpDownItem", sawUpDownItem);
		writeItemStack(tagCompound, "sawLeftRightItem", sawLeftRightItem);
		writeItemStack(tagCompound, "targetUpDownItem", targetUpDownItem);
		writeItemStack(tagCompound, "targetLeftRightItem", targetLeftRightItem);
		writeItemStack(tagCompound, "outputUpDownInventory", outputUpDownInventory);
		writeItemStack(tagCompound, "outputLeftRightInventory", outputLeftRightInventory);
		
	    tagCompound.setBoolean("isSawingUpDown", isSawingUpDown);
	    tagCompound.setBoolean("isSawingLeftRight", isSawingLeftRight);
	    
	    
	    tagCompound.setInteger("sawingTicksUpDown", sawingTicksUpDown);
	    tagCompound.setInteger("maxSawingTicksUpDown", maxSawingTicksUpDown);
	    tagCompound.setInteger("sawingTicksLeftRight", sawingTicksLeftRight);
	    tagCompound.setInteger("maxSawingTicksLeftRight", maxSawingTicksLeftRight);
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
	public void updateEntity() {
		super.updateEntity();
		getHandler().updateEntity();
	}

	@Override
	public void validate() {
		super.validate();
		getHandler().validate();
	}

	@Override
	public void onPressureChanged(float old) {}

	@Override
	public void onFluidLevelChanged(int old) {}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public void firstTick() {
		// TODO Auto-generated method stub

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
	public PressureNetwork getNetwork(ForgeDirection side) {
		return pNetwork;
	}

	@Override
	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		pNetwork = toSet;
	}

	@Override
	public float getPressure(ForgeDirection from) {
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}else{
			return getNetwork(from).getPressure();
		}
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		getNetwork(side).setPressure(newPressure);
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
	
	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		switch(i){
		case 0:
			return inputUpDownInventory;
		case 1:
			return inputLeftRightInventory;
		case 2:
			return outputUpDownInventory;
		case 3:
			return outputLeftRightInventory;
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack inventory = getStackInSlot(i);

        ItemStack ret = null;
        if(inventory.stackSize < j) {
            ret = inventory;
            inventory = null;

        } else {
            ret = inventory.splitStack(j);
            if(inventory.stackSize <= 0) {
                switch(i){
                case 1:
                	inputUpDownInventory = null;
                	break;
                case 2:
                	inputLeftRightInventory = null;
                	break;
                case 3:
                	outputUpDownInventory = null;
                	break;
                case 4:
                	outputLeftRightInventory = null;
                	break;
                }
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack stack = getStackInSlot(i);
        if(stack != null) {
            setInventorySlotContents(i, null);
        }
        return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		switch(i){
		case 0:
			inputUpDownInventory = itemstack;
			break;
		case 1:
			inputLeftRightInventory = itemstack;
			break;
		case 2:
			outputUpDownInventory = itemstack;
			break;
		case 3:
			outputLeftRightInventory = itemstack;
		}
		getHandler().updateBlock();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq(xCoord, yCoord, zCoord) < 64;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if(i == 0 || i == 1){
			//TODO: Do fancy check here if we can make a microblock
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		int otherSlot = -1;
		ForgeDirection dir = ForgeDirection.getOrientation(var1);
		if(dir.equals(ForgeDirection.UP)){
			otherSlot = 0;
		}
		ForgeDirection rotated = getFacing().getRotation(ForgeDirection.UP); 
		if(dir.equals(rotated)){
			otherSlot = 1;
		}
		if(otherSlot == -1){
			int[] ret = new int[]{2,3}; //Output slots can be accessed from every side!
			return ret;
		}else{
			int[] ret = new int[]{otherSlot, 2,3}; //Output slots can be accessed from every side!
			return ret;
		}
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		ForgeDirection side = ForgeDirection.getOrientation(j);
		ForgeDirection rotated = getFacing().getRotation(ForgeDirection.UP);
		if((side.equals(ForgeDirection.UP) && i == 0) || (side.equals(rotated) && i == 1)){
			//TODO: Fancy check if there's a microblock recipe
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(i == 2 || i == 3){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun() || isSawing()) {
            if(!simulate) {
                doSaw();
            }
            //The higher the pressure
            //The higher the speed!
            //But also the more it uses..
            
            return 0.1F + pressurePerTick * (1+(getPressure(from) / getMaxPressure(getHandler().isOilStored(), from)));
        } else {
            return 0F;
        }
	}
	
	private ItemStack getThinningRecipe(ItemStack input){
		//FMP
		InventoryCrafting ic = new InventoryCrafting(null, 3, 3);

		
		int posOfSaw = 0 + 0 * 3;
		int posOfBlock = 0 + 1 * 3;
		ic.setInventorySlotContents(posOfSaw, saw);
		ic.setInventorySlotContents(posOfBlock, input);
		/*ItemStack thinResult = MicroRecipe.getThinningResult(ic);
		return thinResult;*/
		return null;
	}
	
	private ItemStack getSplittingRecipe(ItemStack input){
		// FMP
		InventoryCrafting ic = new InventoryCrafting(null, 3, 3);

		
		int posOfSaw = 0 + 0 * 3;
		int posOfBlock = 1 + 0 * 3;
		ic.setInventorySlotContents(posOfSaw, saw);
		ic.setInventorySlotContents(posOfBlock, input);
		/*ItemStack splitResult = MicroRecipe.getSplittingResult(ic);
		return splitResult;*/
		return null;
	}
	
	private void doSaw(){
		maxSawingTicksUpDown = 200;
		maxSawingTicksLeftRight = 200;
        if(getIsSawingUpDown()) {
            sawingTicksUpDown = sawingTicksUpDown + 1 + (int)(getPressure(ForgeDirection.UNKNOWN) / 1000 * 0.005F);
            //Log.info(crushingTicks+ "");
            if(sawingTicksUpDown >= maxSawingTicksUpDown) {
                //sawing done!
        		if(outputUpDownInventory == null) {
                    outputUpDownInventory = targetUpDownItem.copy();
                } else {
                    outputUpDownInventory.stackSize += targetUpDownItem.stackSize;
                }
        		sawUpDownItem = null;
                targetUpDownItem = null;
                isSawingUpDown = false;
            }
        }
        
        if(getIsSawingLeftRight()) {
            sawingTicksLeftRight = sawingTicksLeftRight + 1 + (int)(getPressure(ForgeDirection.UNKNOWN) / 1000 * 0.005F);
            if(sawingTicksLeftRight >= maxSawingTicksLeftRight) {
                //sawing done!
        		if(outputLeftRightInventory == null) {
                    outputLeftRightInventory = targetLeftRightItem.copy();
                } else {
                    outputLeftRightInventory.stackSize += targetLeftRightItem.stackSize;
                }
        		sawLeftRightItem = null;
                targetLeftRightItem = null;
                isSawingLeftRight = false;
            }
        }
        
        
        boolean scanForNext = false;
        if(Config.get("canSawTwoMicroblocksAtOnce")){
        	scanForNext = (!getIsSawingLeftRight() || !getIsSawingUpDown());
        }else{
        	scanForNext = (!getIsSawingLeftRight() && !getIsSawingLeftRight());
        }
    	if(!scanForNext) return;
    	
        if(canRun()) {
        	if(inputUpDownInventory != null && !getIsSawingUpDown()){
        		targetUpDownItem = getThinningRecipe(inputUpDownInventory);
        		if(targetUpDownItem != null){
        			sawUpDownItem = inputUpDownInventory.copy();
        			inputUpDownInventory.stackSize--;
        			if(inputUpDownInventory.stackSize <= 0){
        				inputUpDownInventory = null;
        			}
        			sawingTicksUpDown = 0;
                    pressurePerTick = 100F;
                    if(!getHandler().isOilStored()){
                    	pressurePerTick /= 10;
                    }            			
                    isSawingUpDown = true;
        		}
        	}else if(inputLeftRightInventory != null && !getIsSawingLeftRight()){
        		targetLeftRightItem = getSplittingRecipe(inputLeftRightInventory);
        		if(targetLeftRightItem != null){
        			sawLeftRightItem = inputLeftRightInventory.copy();
        			inputLeftRightInventory.stackSize--;
        			if(inputLeftRightInventory.stackSize <= 0){
        				inputLeftRightInventory = null;
        			}
        			sawingTicksLeftRight = 0;
                    pressurePerTick = 100F;
                    if(!getHandler().isOilStored()){
                    	pressurePerTick /= 10;
                    }            			
                    isSawingLeftRight = true;
        		}
        	}
        }
    }

    public ItemStack getSawingUpDownItem(){
        return sawUpDownItem;
    }
    public ItemStack getSawingLeftRightItem(){
        return sawLeftRightItem;
    }

    public ItemStack getTargetUpDownItem(){
        return targetUpDownItem;
    }
    
    public ItemStack getTargetLeftRightItem(){
        return targetLeftRightItem;
    }

    public boolean getIsSawingUpDown(){
    	return isSawingUpDown;
    }
    
    public boolean getIsSawingLeftRight(){
    	return isSawingLeftRight;
    }
    
    public boolean isSawing(){
        return sawUpDownItem != null || sawLeftRightItem != null;
    }

    /*!
     * Checks if the outputslot is free, if there's enough pressure in the system
     * and if the item is smeltable
     */
    private boolean canRun(){
        if((inputUpDownInventory == null && inputLeftRightInventory == null) || getPressure(ForgeDirection.UNKNOWN) < requiredPressure) {
            return false;
        } else {
        	if(inputUpDownInventory != null){
        		ItemStack target = getThinningRecipe(inputUpDownInventory);
        		if(target == null) return false;
                if(outputUpDownInventory != null) {
                    if(!outputUpDownInventory.isItemEqual(target)) return false; 
                    int newItemStackSize = outputUpDownInventory.stackSize + target.stackSize;
                    boolean ret = newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize();
                    return ret;
                } else {
                    return true;
                }
        	}
        	if(inputLeftRightInventory != null){
        		ItemStack target = getSplittingRecipe(inputLeftRightInventory);
        		if(target == null) return false;
                if(outputLeftRightInventory != null) {
                    if(!outputLeftRightInventory.isItemEqual(target)) return false; 
                    int newItemStackSize = outputLeftRightInventory.stackSize + target.stackSize;
                    boolean ret = newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize();
                    return ret;
                } else {
                    return true;
                }
        	}
        	return false;
        }
    }

    private static boolean canSplit(ItemStack inv){
        return Config.canBeCrushed(inv);
    }
    private static boolean canThin(ItemStack inv){
        return Config.canBeCrushed(inv);
    }
    
	@Override
	public void onBlockBreaks() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}

	public int getSawingTicksUpDown() {
		return sawingTicksUpDown;
	}
	
	public int getSawingTicksLeftRight() {
		return sawingTicksLeftRight;
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
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicSaw.unlocalized);
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
