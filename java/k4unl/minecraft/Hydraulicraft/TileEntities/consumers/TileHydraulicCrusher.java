package k4unl.minecraft.Hydraulicraft.TileEntities.consumers;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes;
import k4unl.minecraft.Hydraulicraft.lib.CrushingRecipes.CrushingRecipe;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicCrusher extends TileEntity implements ISidedInventory, IHydraulicConsumer{
    private ItemStack inputInventory;
    private ItemStack crushingItem;
    private ItemStack targetItem;
    private ItemStack outputInventory;
    private final float requiredPressure = 5F;
    private int crushingTicks = 0;
    private int maxCrushingTicks = 0;
    private int oldScaledCrushTime;
    private float pressurePerTick = 0F;
    private IBaseClass baseHandler;
    

    private PressureNetwork pNetwork;
    private List<ForgeDirection> connectedSides;
    
    
    public TileHydraulicCrusher(){
    	connectedSides = new ArrayList<ForgeDirection>();
    }

    public int getCrushingTicks(){
        return crushingTicks;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet){
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
    public float getMaxPressure(boolean isOil, ForgeDirection from){
    	if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
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
    public int getMaxStorage(){
        return FluidContainerRegistry.BUCKET_VOLUME * 5;
    }

    @Override
    public void onBlockBreaks(){
        getHandler().dropItemStackInWorld(inputInventory);
        getHandler().dropItemStackInWorld(outputInventory);
    }

    @Override
    public IBaseClass getHandler(){
        if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
        return baseHandler;
    }

    @Override
    public void updateEntity(){
        getHandler().updateEntity();
        
        if(!worldObj.isRemote && oldScaledCrushTime != getScaledCrushTime()) {//TODO refactor it so updates only will be made when a player has a GUI open of this block.
            oldScaledCrushTime = getScaledCrushTime();
            getHandler().updateBlock();
        }
    }

    @Override
    public void readNBT(NBTTagCompound tagCompound){
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
    public void writeNBT(NBTTagCompound tagCompound){
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
		getHandler().validate();
	}

	@Override
	public void onPressureChanged(float old) {
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
		
	}
	
	@Override
	public float getPressure(ForgeDirection from) {
		if(worldObj.isRemote){
			return getHandler().getPressure();
		}
		if(getNetwork(from) == null){
			Log.error("Crusher at " + getHandler().getBlockLocation().printCoords() + " has no pressure network!");
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
}
