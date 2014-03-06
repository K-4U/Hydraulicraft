package k4unl.minecraft.Hydraulicraft.TileEntities.misc;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
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

public class TileInterfaceValve extends TileEntity implements IFluidHandler {
	private int targetX;
	private int targetY;
	private int targetZ;
	private boolean targetHasChanged = true;
	private IHydraulicConsumer target;
	private IFluidHandler fluidTarget;
	private boolean clientNeedsToResetTarget = false;
	private boolean clientNeedsToSetTarget = false;
	
	public void resetTarget(){
		target = null;
		targetX = xCoord;
		targetY = yCoord;
		targetZ = zCoord;
		targetHasChanged = true;
		
		if(!worldObj.isRemote){
			clientNeedsToResetTarget = true;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void setTarget(int x, int y, int z){
		targetX = x;
		targetY = y;
		targetZ = z;
		targetHasChanged = true;
		
		if(!worldObj.isRemote){
			clientNeedsToSetTarget = true;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public IHydraulicConsumer getTarget(){
		if(targetHasChanged == true && targetX != xCoord && targetY != yCoord && targetZ != zCoord){
			TileEntity t = worldObj.getBlockTileEntity(targetX, targetY, targetZ);
			if(t instanceof IHydraulicConsumer){
				target = (IHydraulicConsumer) t;
				targetHasChanged = false;
			}
			if(t instanceof IFluidHandler){
				fluidTarget = (IFluidHandler) t;
			}
		}else if(targetHasChanged == true && targetX == xCoord && targetY == yCoord && targetZ == zCoord){
			target = null;
			fluidTarget = null;
		}
		return target;
	}
	
	public IFluidHandler getFluidTarget(){
		if(targetHasChanged == true && (targetX != xCoord || targetY != yCoord || targetZ != zCoord)){
			TileEntity t = worldObj.getBlockTileEntity(targetX, targetY, targetZ);
			if(t instanceof IHydraulicConsumer){
				target = (IHydraulicConsumer) t;
				if(t instanceof IFluidHandler){
					fluidTarget = (IFluidHandler) t;
				}
				targetHasChanged = false;
			}
		}else if(targetHasChanged == true && targetX == xCoord && targetY == yCoord && targetZ == zCoord){
			target = null;
			fluidTarget = null;
		}
		return fluidTarget;
	}
	

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		targetX = tagCompound.getInteger("targetX");
		targetY = tagCompound.getInteger("targetY");
		targetZ = tagCompound.getInteger("targetZ");
		if(tagCompound.getBoolean("isTargetNull")){
			target = null;
		}
		if(worldObj != null && worldObj.isRemote){
			if(tagCompound.getBoolean("clientNeedsToResetTarget")){
				resetTarget();
			}
			if(tagCompound.getBoolean("clientNeedsToSetTarget")){
				targetHasChanged = true;
				getTarget();
			}
		}
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
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("targetX", targetX);
		tagCompound.setInteger("targetY", targetY);
		tagCompound.setInteger("targetZ", targetZ);
		tagCompound.setBoolean("isTargetNull", (target == null));
		if(target == null){
			tagCompound.setBoolean("isTargetNull", (target == null));			
		}
		if(worldObj != null && !worldObj.isRemote){
			tagCompound.setBoolean("clientNeedsToResetTarget", clientNeedsToResetTarget);
			tagCompound.setBoolean("clientNeedsToSetTarget", clientNeedsToSetTarget);
			clientNeedsToResetTarget = false;
			clientNeedsToSetTarget = false;
		}
		tagCompound.setBoolean("targetHasChanged", targetHasChanged);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(getFluidTarget() != null){
			return getFluidTarget().fill(from, resource, doFill);
		}else{
			return 0;			
		}
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(getFluidTarget() != null){
			return getFluidTarget().drain(from, resource, doDrain);
		}else{
			return null;
		}
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(getFluidTarget() != null){
			return getFluidTarget().drain(from, maxDrain, doDrain);
		}else{
			return null;
		}
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(getFluidTarget() != null){
			return getFluidTarget().canFill(from, fluid);
		}else{
			return false;
		}
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(getFluidTarget() != null){
			return getFluidTarget().canDrain(from, fluid);
		}else{
			return false;
		}
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(getFluidTarget() != null){
			return getFluidTarget().getTankInfo(from);
		}else{
			return null;
		}
	}

}
