package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import java.util.Random;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes.CrushingRecipe;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileHydraulicCrusher extends TileHydraulicBase implements ISidedInventory, IHydraulicConsumer{
    private ItemStack inputInventory;
    private ItemStack crushingItem;
    private ItemStack targetItem;
    private ItemStack outputInventory;
    private final float requiredPressure = 5F;
    private int crushingTicks = 0;
    private int maxCrushingTicks = 0;
    private int oldScaledCrushTime;
    private float pressurePerTick = 0F;
    
    
    public TileHydraulicCrusher(){
    	super(PressureTier.HIGHPRESSURE, 5);
    	super.validateI(this);
    }

    public int getCrushingTicks(){
        return crushingTicks;
    }

    @Override
    public float workFunction(boolean simulate, ForgeDirection from){
        if(canRun() || isCrushing()) {
            if(!simulate) {
                doCrush();
            }
            //The higher the pressure
            //The higher the speed!
            //But also the more it uses..
            
            return 0.1F + pressurePerTick;
        } else {
            return 0F;
        }
    }

    private void doCrush(){
        if(isCrushing()) {
        	float maxPressureThisTier = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
			float ratio = getPressure(ForgeDirection.UP) / maxPressureThisTier;
            crushingTicks = crushingTicks + 1 + (int)((pNetwork.getLowestTier() * 4) * ratio);// + (int)(getPressure(ForgeDirection.UNKNOWN) / 1000 * 0.005F);
            //Log.info(crushingTicks+ "");
            if(crushingTicks >= maxCrushingTicks) {
                //Crushing done!
                if(outputInventory == null) {
                    outputInventory = targetItem.copy();
                } else {
                    outputInventory.stackSize += targetItem.stackSize;
                }

                crushingItem = null;
                targetItem = null;
                pressurePerTick = 0;
            }
        } else {
        	maxCrushingTicks = 200;
            if(canRun()) {
            	CrushingRecipe currentRecipe = CrushingRecipes.getCrushingRecipe(inputInventory); 
                targetItem = currentRecipe.output.copy();
                if(new Random().nextFloat() > 0.80F) {
                	targetItem.stackSize+=1;
                }
                crushingItem = inputInventory.copy();
                inputInventory.stackSize--;
                if(inputInventory.stackSize <= 0) {
                    inputInventory = null;
                }
                crushingTicks = 0;
                
                pressurePerTick = (Functions.getMaxGenPerTier(pNetwork.getLowestTier(), true) * 0.75F) * currentRecipe.pressureRatio;
            }
            
        }
    }

    public ItemStack getCrushingItem(){
        return crushingItem;
    }

    public ItemStack getTargetItem(){
        return targetItem;
    }

    public boolean isCrushing(){
        return crushingItem != null && targetItem != null;
    }

    /*!
     * Checks if the outputslot is free, if there's enough pressure in the system
     * and if the item is smeltable
     */
    private boolean canRun(){
        if(inputInventory == null || getPressure(ForgeDirection.UNKNOWN) < requiredPressure) {
            return false;
        } else {
            //Get crushing result:
            CrushingRecipe targetRecipe = CrushingRecipes.getCrushingRecipe(inputInventory);
            if(targetRecipe == null) return false;
            if(outputInventory != null) {
            	ItemStack target = targetRecipe.getOutput();
                if(!outputInventory.isItemEqual(target)) return false;
                if(Float.compare(getPressure(ForgeDirection.UP), targetRecipe.pressureRatio) < 0) return false; 
                int newItemStackSize = outputInventory.stackSize + target.stackSize + 1; //The random chance..
                boolean ret = newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize();
                return ret;
            } else {
                return true;
            }
        }
    }

    private static boolean canCrush(ItemStack inv){
        return Config.canBeCrushed(inv);
    }

    @Override
    public int getSizeInventory(){
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int i){
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
    public ItemStack decrStackSize(int i, int j){
        ItemStack inventory = getStackInSlot(i);

        ItemStack ret = null;
        if(inventory.stackSize < j) {
            ret = inventory;
            inventory = null;

        } else {
            ret = inventory.splitStack(j);
            if(inventory.stackSize <= 0) {
                if(i == 0) {
                    inputInventory = null;
                } else {
                    outputInventory = null;
                }
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        return ret;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i){
        ItemStack stack = getStackInSlot(i);
        if(stack != null) {
            setInventorySlotContents(i, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack){
        if(i == 0) {
            inputInventory = itemStack;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else if(i == 1) {
            outputInventory = itemStack;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else {
            //Err...
        }
    }



    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player){
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord, yCoord, zCoord) < 64;
    }


    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack){
        if(i == 0) {
            if(canCrush(itemStack)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /* TODO: Fix me
    @Override
    public void onInventoryChanged(){
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    */

    @Override
    public int[] getAccessibleSlotsFromSide(int var1){
        return new int[]{1, 0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int j){
        if(i == 0 && canCrush(itemStack)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j){
        if(i == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBlockBreaks(){
        dropItemStackInWorld(inputInventory);
        dropItemStackInWorld(outputInventory);
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if(!worldObj.isRemote && oldScaledCrushTime != getScaledCrushTime()) {//TODO refactor it so updates only will be made when a player has a GUI open of this block.
            oldScaledCrushTime = getScaledCrushTime();
            getHandler().updateBlock();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
    	super.readFromNBT(tagCompound);
        NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
        inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        inventoryCompound = tagCompound.getCompoundTag("outputInventory");
        outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);

        inventoryCompound = tagCompound.getCompoundTag("crushingItem");
        crushingItem = ItemStack.loadItemStackFromNBT(inventoryCompound);

        inventoryCompound = tagCompound.getCompoundTag("targetItem");
        targetItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
        
        crushingTicks = tagCompound.getInteger("crushingTicks");
        maxCrushingTicks = tagCompound.getInteger("maxCrushingTicks");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
    	super.writeToNBT(tagCompound);
        if(inputInventory != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            inputInventory.writeToNBT(inventoryCompound);
            tagCompound.setTag("inputInventory", inventoryCompound);
        }
        if(outputInventory != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            outputInventory.writeToNBT(inventoryCompound);
            tagCompound.setTag("outputInventory", inventoryCompound);
        }
        if(crushingItem != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            crushingItem.writeToNBT(inventoryCompound);
            tagCompound.setTag("crushingItem", inventoryCompound);
        }
        if(targetItem != null) {
            NBTTagCompound inventoryCompound = new NBTTagCompound();
            targetItem.writeToNBT(inventoryCompound);
            tagCompound.setTag("targetItem", inventoryCompound);
        }
        
        tagCompound.setInteger("crushingTicks", crushingTicks);
        tagCompound.setInteger("maxCrushingTicks", maxCrushingTicks);
    }

    public int getScaledCrushTime(){
        return maxCrushingTicks == 0 || !isCrushing() ? 0 : 34 * crushingTicks / maxCrushingTicks;
    }

    
	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}
	
	@Override
	public boolean canWork(ForgeDirection dir) {
		if(getNetwork(dir) == null){
			return false;
		}
		return dir.equals(ForgeDirection.UP);
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
	}

	@Override
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicCrusher.unlocalized);
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

	@Override
	public void onFluidLevelChanged(int old) { }
}
