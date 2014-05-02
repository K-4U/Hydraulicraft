package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TileHydraulicFrictionIncinerator extends TileHydraulicBase implements ISidedInventory, IHydraulicConsumer {

	private ItemStack inputInventory;
	private ItemStack smeltingItem;
	private ItemStack targetItem;
	private ItemStack outputInventory;
	private float requiredPressure = 0F;
	private int smeltingTicks = 0;
	private int maxSmeltingTicks = 0;

	
	public TileHydraulicFrictionIncinerator(){
		super(PressureTier.HIGHPRESSURE, 5);
		super.init(this);
	}
	
	public int getSmeltingTicks(){
		return smeltingTicks;
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(canRun() || isSmelting()){
			if(!simulate){
				doSmelt();
			}

			return 0.1F + requiredPressure; 
		}else{
			return 0F;
		}
	}
	
	
	private void doSmelt(){
		if(isSmelting()){
			//The higher the pressure
			//The higher the speed!
			float maxPressureThisTier = Functions.getMaxPressurePerTier(pNetwork.getLowestTier(), true);
			float ratio = getPressure(ForgeDirection.UP) / maxPressureThisTier;
			smeltingTicks = smeltingTicks + 1 + (int)((pNetwork.getLowestTier() * 4) * ratio);
			if(smeltingTicks < 0){
				smeltingTicks = 0;
			}
			if(smeltingTicks >= maxSmeltingTicks){
				//Smelting done!
				if(outputInventory == null){
					outputInventory = targetItem.copy(); 
				}else{
					outputInventory.stackSize++;
				}
				smeltingItem = null;
				targetItem = null;
			}
		}else{
			if(canRun()){
				targetItem = FurnaceRecipes.smelting().getSmeltingResult(inputInventory);
				smeltingItem = inputInventory.copy();
				inputInventory.stackSize--;
				if(inputInventory.stackSize <= 0){
					inputInventory = null;
				}
				smeltingTicks = 0;
			}
			//We need to check on what kind of network we are though..
			//This pressure requirement is only for HP oil.
			requiredPressure = Functions.getMaxGenPerTier(pNetwork.getLowestTier(), true) / 4.0F;
			
			//Start smelting
			maxSmeltingTicks = 200;
		}
	}
	
	public ItemStack getSmeltingItem(){
		return smeltingItem;
	}
	
	public ItemStack getTargetItem(){
		return targetItem;
	}
	
	public boolean isSmelting(){
		return (smeltingItem != null && targetItem != null);
	}
	
	/*!
	 * Checks if the outputslot is free, if there's enough pressure in the system
	 * and if the item is smeltable
	 */
	private boolean canRun(){
		if(inputInventory == null || (getPressure(ForgeDirection.UNKNOWN) < requiredPressure)){
			return false;
		}else{
			//Get smelting result:
			ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inputInventory);
			if(target == null) return false;
			if(outputInventory != null){
				if(!outputInventory.isItemEqual(target)) return false;
				int newItemStackSize = outputInventory.stackSize + inputInventory.stackSize;
				
				return (newItemStackSize <= getInventoryStackLimit() && newItemStackSize <= target.getMaxStackSize());
			}else{
				return true;
			}
		}
	}
	
	private static boolean canSmelt(ItemStack inv){
		//Get smelting result:
		ItemStack target = FurnaceRecipes.smelting().getSmeltingResult(inv);
		if(target == null) return false;
		return true;
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
	public int getSizeInventory() {
		return 2;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
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
				}else{
					outputInventory = null;
				}
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
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
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}else if(i == 1){
			outputInventory = itemStack;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
			if(canSmelt(itemStack)){
				return true;
			}else{
				return false;
			}
		}else{
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
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {1, 0};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i == 0 && canSmelt(itemStack)){
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
	public void onBlockBreaks() {
		dropItemStackInWorld(inputInventory);
		dropItemStackInWorld(outputInventory);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagCompound inventoryCompound = tagCompound.getCompoundTag("inputInventory");
		inputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("outputInventory");
		outputInventory = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("smeltingItem");
		smeltingItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		inventoryCompound = tagCompound.getCompoundTag("targetItem");
		targetItem = ItemStack.loadItemStackFromNBT(inventoryCompound);
		
		smeltingTicks = tagCompound.getInteger("smeltingTicks");
		
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
		if(smeltingItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			smeltingItem.writeToNBT(inventoryCompound);
			tagCompound.setTag("smeltingItem", inventoryCompound);
		}
		if(targetItem != null){
			NBTTagCompound inventoryCompound = new NBTTagCompound();
			targetItem.writeToNBT(inventoryCompound);
			tagCompound.setTag("targetItem", inventoryCompound);
		}
		
		tagCompound.setInteger("smeltingTicks",smeltingTicks);
	}

	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public void onFluidLevelChanged(int old) {
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
	public String getInventoryName() {
		return Localization.getLocalizedName(Names.blockHydraulicFrictionIncinerator.unlocalized);
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}	
}
