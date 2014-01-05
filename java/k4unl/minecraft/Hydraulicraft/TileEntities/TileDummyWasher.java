package k4unl.minecraft.Hydraulicraft.TileEntities;

import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileDummyWasher extends TileEntity implements ISidedInventory, IFluidHandler, IHydraulicConsumer {
	private int coreX;
	private int coreY;
	private int coreZ;
	private TileHydraulicWasher core;
	private int dir;
	private int horiz;
	private int vert;
	private int depth;
	
	public void setCore(int x, int y, int z){
		coreX = x;
		coreY = y;
		coreZ = z;
	}
	
	public void setLocationInMultiBlock(int dir, int horiz, int vert, int depth){
		this.horiz = horiz;
		this.vert = vert;
		this.depth = depth;
		this.dir = dir;
	}
	
	public boolean getIsCorner(){
		return getIsEdge() && (getIsBottom() || getIsTop());
	}
	
	public boolean getIsEdge(){
		return vert == -1 || vert == 1;
	}
	
	public boolean getIsBottom(){
		return horiz == -1;
	}
	
	public boolean getIsTop(){
		return horiz == 1;
	}
	
	public int getHoriz(){
		return horiz;
	}
	public int getVert(){
		return vert;
	}
	public int getDepth(){
		return depth;
	}
	public int getDir() {
		return dir;
	}
	
	public TileHydraulicWasher getCore(){
		if(core == null){
			TileEntity t = worldObj.getBlockTileEntity(coreX, coreY, coreZ);
			if(t instanceof TileHydraulicWasher){
				core = (TileHydraulicWasher) t;
			}
		}
		return core;
	}
	
	@Override
	public int getSizeInventory() {
		return getCore().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return getCore().getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return getCore().decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return getCore().getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		getCore().setInventorySlotContents(i, itemStack);
	}

	@Override
	public String getInvName() {
		return getCore().getInvName();
	}

	@Override
	public boolean isInvNameLocalized() {
		return getCore().isInvNameLocalized();
	}

	@Override
	public int getInventoryStackLimit() {
		return getCore().getInventoryStackLimit();
	}

	@Override
	public void onInventoryChanged() {
		getCore().onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return getCore().isUseableByPlayer(player);
	}

	@Override
	public void openChest() {
		getCore().openChest();
	}

	@Override
	public void closeChest() {
		getCore().closeChest();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return getCore().isItemValidForSlot(i, itemStack);
	}

	@Override
	public int getMaxStorage() {
		return getCore().getMaxStorage();
	}

	@Override
	public float getMaxPressure() {
		return getCore().getMaxPressure();
	}

	@Override
	public IBaseClass getHandler() {
		return getCore().getHandler();
		//return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		coreX = tagCompound.getInteger("coreX");
		coreY = tagCompound.getInteger("coreY");
		coreZ = tagCompound.getInteger("coreZ");
		
		depth = tagCompound.getInteger("depth");
		horiz = tagCompound.getInteger("horiz");
		vert = tagCompound.getInteger("vert");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("coreX", coreX);
		tagCompound.setInteger("coreY", coreY);
		tagCompound.setInteger("coreZ", coreZ);
		
		tagCompound.setInteger("depth", depth);
		tagCompound.setInteger("horiz", horiz);
		tagCompound.setInteger("vert", vert);
	}

	@Override
	public void readNBT(NBTTagCompound tagCompound) {
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		NBTTagCompound tagCompound = packet.data;
		this.readFromNBT(tagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 4, tagCompound);
	}

	@Override
	public void updateEntity() {
		//Nothing to be done here.
	}

	@Override
	public float workFunction(boolean simulate) {
		//Also don't do anything here.
		return 0;
	}

	@Override
	public void onBlockBreaks() {
		if(getCore() != null){
			getCore().invalidateMultiblock();
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(getCore() != null){
			return getCore().fill(from, resource, doFill);
		}else{
			return 0;
		}
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		return getCore().drain(from, resource, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return getCore().drain(from, maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return getCore().canFill(from, fluid);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return getCore().canDrain(from, fluid);
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return getCore().getTankInfo(from);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return getCore().getAccessibleSlotsFromSide(var1);
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		return getCore().canInsertItem(i, itemStack, j);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		return getCore().canExtractItem(i, itemStack, j);
	}

	
}
