package k4unl.minecraft.Hydraulicraft.thirdParty.fmp.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.InventoryCraftingDummy;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileHydraulicSaw extends TileHydraulicBase implements IHydraulicConsumer, ISidedInventory {
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

    private ItemStack saw = new ItemStack(GameRegistry.findItem("ForgeMicroblock","sawDiamond"), 1);

    
    public TileHydraulicSaw(){
    	super(10);
    	super.init(this);
    }
	
	public EnumFacing getFacing(){
		if(worldObj != null){
			return (EnumFacing) worldObj.getBlockState(getPos()).getValue(Properties.ROTATION);
		}else{
			return EnumFacing.NORTH;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
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
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
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
	public void onFluidLevelChanged(int old) {}

	@Override
	public boolean canConnectTo(EnumFacing side) {
		return true;
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

        ItemStack ret;
        if(inventory.stackSize < j) {
            ret = inventory;

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
        worldObj.markBlockForUpdate(getPos());

        return ret;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return decrStackSize(index, getStackInSlot(index).stackSize);
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
		return worldObj.getTileEntity(getPos()) == this && entityplayer.getDistanceSq(getPos()) < 64;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		//TODO: Do fancy check here if we can make a microblock
		return i == 0 || i == 1;
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
	public int[] getSlotsForFace(EnumFacing dir) {
		int otherSlot = -1;
		if(dir.equals(EnumFacing.UP)){
			otherSlot = 0;
		}
		EnumFacing rotated = getFacing().rotateAround(EnumFacing.UP.getAxis());
		if(dir.equals(rotated)){
			otherSlot = 1;
		}
		if(otherSlot == -1){
			return new int[]{2,3};
		}else{
			return new int[]{otherSlot, 2,3};
		}
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing side) {
		EnumFacing rotated = getFacing().rotateAround(EnumFacing.UP.getAxis());
		//TODO: Fancy check if there's a microblock recipe
		return (side.equals(EnumFacing.UP) && i == 0) || (side.equals(rotated) && i == 1);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing j) {
		return i == 2 || i == 3;
	}

	@Override
	public float workFunction(boolean simulate, EnumFacing from) {
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
		InventoryCrafting ic = new InventoryCraftingDummy();

		
		int posOfSaw = 0 + 0 * 3;
		int posOfBlock = 0 + 1 * 3;
		ic.setInventorySlotContents(posOfSaw, saw);
		ic.setInventorySlotContents(posOfBlock, input);
		//return MicroRecipe.getThinningResult(ic);
		return null;
	}
	
	private ItemStack getSplittingRecipe(ItemStack input){
		// FMP
		InventoryCrafting ic = new InventoryCraftingDummy();

		
		int posOfSaw = 0 + 0 * 3;
		int posOfBlock = 1 + 0 * 3;
		ic.setInventorySlotContents(posOfSaw, saw);
		ic.setInventorySlotContents(posOfBlock, input);
		//return MicroRecipe.getSplittingResult(ic);
		return null;
	}
	
	private void doSaw(){
		maxSawingTicksUpDown = 200;
		maxSawingTicksLeftRight = 200;
        if(getIsSawingUpDown()) {
            sawingTicksUpDown = sawingTicksUpDown + 1 + (int)(getPressure(EnumFacing.UP) / 1000 * 0.005F);
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
            sawingTicksLeftRight = sawingTicksLeftRight + 1 + (int)(getPressure(EnumFacing.UP) / 1000 * 0.005F);
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
        
        
        boolean scanForNext;
        if(HCConfig.INSTANCE.getBool("canSawTwoMicroblocksAtOnce")){
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
        if((inputUpDownInventory == null && inputLeftRightInventory == null) || getPressure(EnumFacing.UP) < requiredPressure) {
            return false;
        } else {
        	if(inputUpDownInventory != null){
        		ItemStack target = getThinningRecipe(inputUpDownInventory);
        		if(target == null) return false;
                if(outputUpDownInventory != null) {
                    if(!outputUpDownInventory.isItemEqual(target)) return false; 
                    int newItemStackSize = outputUpDownInventory.stackSize + target.stackSize;
					return newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize();
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
					return newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize();
                } else {
                    return true;
                }
        	}
        	return false;
        }
    }
    
	@Override
	public void onBlockBreaks() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canWork(EnumFacing dir) {
		return dir.equals(EnumFacing.UP);
	}

	public int getSawingTicksUpDown() {
		return sawingTicksUpDown;
	}
	
	public int getSawingTicksLeftRight() {
		return sawingTicksLeftRight;
	}

	@Override
	public String getName() {
		return Localization.getLocalizedName(Names.blockHydraulicSaw.unlocalized);
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentTranslation(Names.blockHydraulicSaw.unlocalized);
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

}
