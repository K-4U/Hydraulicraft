package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileHydraulicValve extends TileHydraulicBase implements IHydraulicMachine {
	private int targetX;
	private int targetY;
	private int targetZ;
	private boolean targetHasChanged = true;
	private IHydraulicConsumer target;
	private boolean clientNeedsToResetTarget = false;
	private boolean clientNeedsToSetTarget = false;

	
	public TileHydraulicValve(){
		super(PressureTier.HIGHPRESSURE, 1);
		super.validateI(this);
	}
	
	public void resetTarget(){
		target = null;
		targetX = xCoord;
		targetY = yCoord;
		targetZ = zCoord;
		targetHasChanged = true;
		if(pNetwork != null){
			pNetwork.removeMachine(this);
		}
		if(!worldObj.isRemote){
			clientNeedsToResetTarget = true;
		}
		getHandler().updateBlock();
		getHandler().updateNetworkOnNextTick(0);
	}
	
	public void setTarget(int x, int y, int z){
		targetX = x;
		targetY = y;
		targetZ = z;
		targetHasChanged = true;
		if(pNetwork != null){
			pNetwork.removeMachine(this);
		}
		if(!worldObj.isRemote){
			clientNeedsToSetTarget = true;
		}
		//getHandler().updateBlock();
	}
	
	public IHydraulicConsumer getTarget(){
		if(targetHasChanged == true && (targetX != xCoord || targetY != yCoord || targetZ != zCoord)){
			TileEntity t = worldObj.getTileEntity(targetX, targetY, targetZ);
			if(t instanceof IHydraulicConsumer){
				target = (IHydraulicConsumer) t;
				targetHasChanged = false;
			}
		}else if(targetHasChanged == true && targetX == xCoord && targetY == yCoord && targetZ == zCoord){
			target = null;
			//targetHasChanged = false;
		}
		return target;
	}
	
	@Override
	public int getMaxStorage() {
		if(getTarget() == null){
			return 0;
		}else{
			return getTarget().getHandler().getMaxStorage();
		}
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(getTarget() == null){
			return 0F;
		}else{
			return getTarget().getHandler().getMaxPressure(isOil, from);
		}
	}

	@Override
	public void onBlockBreaks() {
		
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
	public void onFluidLevelChanged(int old) {
		if(getTarget() != null){
			getTarget().getHandler().setStored(getHandler().getStored(), getHandler().isOilStored(), false);
		}
	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		if(getTarget() != null){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void updateNetwork(float oldPressure) {
		if(getTarget() == null){
			pNetwork = null;
			getHandler().updateBlock();
			return;
		}
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
			if(getTarget() != null){
				((TileHydraulicBase)getTarget().getHandler()).setNetwork(ForgeDirection.UP, pNetwork);
				pNetwork.addMachine(getTarget(), oldPressure, ForgeDirection.UP);
			}
			for(ForgeDirection dir:connectedSides){
				Location hoseLocation = new Location(xCoord, yCoord, zCoord, dir);
				TileEntity ent = getBlockTileEntity(hoseLocation);
				/*
				if(ent instanceof TileMultipart && Multipart.hasTransporter((TileMultipart)ent)){
					IHydraulicTransporter hose = Multipart.getTransporter((TileMultipart)ent);
					hose.checkConnectedSides(this);
				}*/
			}
			//Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure, ForgeDirection.UP);
			if(getTarget() != null){
				((TileHydraulicBase)getTarget().getHandler()).setNetwork(ForgeDirection.UP, pNetwork);
				pNetwork.addMachine(getTarget(), oldPressure, ForgeDirection.UP);
			}
			//Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}
	}
	
	private TileEntity getBlockTileEntity(Location l) {
		return worldObj.getTileEntity(l.getX(), l.getY(), l.getZ());
	}
}
