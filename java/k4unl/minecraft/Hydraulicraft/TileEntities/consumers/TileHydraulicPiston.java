package k4unl.minecraft.Hydraulicraft.TileEntities.consumers;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicPiston extends TileEntity implements
		IHydraulicConsumer {

	private IBaseClass baseHandler;
	
	private float extendedLength;
	private float maxLength = 4F;
	private float extendTarget = 0F;
	private float movingSpeed = 0.05F;
	private boolean harvesterPart = false;
	private Location harvesterLocation;
	
	private boolean isRetracting;

	private PressureNetwork pNetwork;
	private List<ForgeDirection> connectedSides;
	
	public TileHydraulicPiston(){
		connectedSides = new ArrayList<ForgeDirection>();
	}
	
	public float getExtendTarget(){
		return extendTarget;
	}
	
	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 5;
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
	public void readNBT(NBTTagCompound tagCompound) {
		extendedLength = tagCompound.getFloat("extendedLength");
		maxLength = tagCompound.getFloat("maxLength");
		extendTarget = tagCompound.getFloat("extendTarget");
		harvesterPart = tagCompound.getBoolean("harvesterPart");
		isRetracting = tagCompound.getBoolean("isMoving");
		
		harvesterLocation = new Location(tagCompound.getIntArray("harvesterLocation"));
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		tagCompound.setFloat("extendedLength", extendedLength);
		tagCompound.setFloat("maxLength", maxLength);
		tagCompound.setFloat("extendTarget", extendTarget);
		tagCompound.setBoolean("harvesterPart", harvesterPart);
		tagCompound.setBoolean("isMoving", isRetracting);
		if(harvesterLocation != null){
			tagCompound.setIntArray("harvesterLocation", harvesterLocation.getLocation());
		}
	}

	public float getExtendedLength(){
		return extendedLength;
	}
	

	public float getMaxLength(){
		return maxLength;
	}
	
	public void setIsHarvesterPart(boolean isit, Location _harvesterLocation){
		harvesterLocation = _harvesterLocation;
		harvesterPart = isit;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void setIsHarvesterPart(boolean isit){
		harvesterPart = isit;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void setMaxLength(float newMaxLength){
		maxLength = newMaxLength;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public boolean getIsHarvesterPart(){
		return harvesterPart;
	}
	
	public void extendTo(float blocksToExtend){
		if(blocksToExtend > maxLength){
			blocksToExtend = maxLength;
		}
		if(blocksToExtend < 0){
			blocksToExtend = 0;
		}
		
		extendTarget = blocksToExtend;
		
		int compResult = Float.compare(extendTarget, extendedLength); 
		if(compResult > 0){
			isRetracting = false;
		}else if(compResult < 0){
			isRetracting = true;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	
	
	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		int compResult = Float.compare(extendTarget, extendedLength);
		if(compResult > 0 && !isRetracting){
			extendedLength += movingSpeed;
		}else if(compResult < 0 && isRetracting){
			extendedLength -= movingSpeed;
		}else{
			extendTarget = extendedLength;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
		if(compResult >= 0){
			return Constants.PRESSURE_USAGE_PISTON;
		}
		return 0;
	}

	@Override
	public void onBlockBreaks() {

	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		getHandler().readFromNBT(tagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		getHandler().writeToNBT(tagCompound);
	}
	
	@Override
	public IBaseClass getHandler() {
		if(harvesterPart && getHarvester() != null){
			if(baseHandler == null) baseHandler = getHarvester().getHandler();
		}else{
			if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
		}
        return baseHandler;
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		getHandler().onDataPacket(net, packet);
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
	}

	public TileHydraulicHarvester getHarvester(){
		if(harvesterPart){
			if(harvesterLocation != null){
				TileEntity t = worldObj.getBlockTileEntity(harvesterLocation.getX(), harvesterLocation.getY(), harvesterLocation.getZ());
				if(t instanceof TileHydraulicHarvester){
					return (TileHydraulicHarvester)t;
				}
			}
		}
		return null;
	}
	
	@Override
	public void updateEntity() {
		if(!harvesterPart){
			getHandler().updateEntity();
		}
	}
	
	@Override
    public AxisAlignedBB getRenderBoundingBox(){
		int metadata = getBlockMetadata();
		float extendedLength = getExtendedLength();
        float minX = 0.0F + xCoord;
        float minY = 0.0F + yCoord;
        float minZ = 0.0F + zCoord;
        float maxX = 1.0F + xCoord;
        float maxY = 1.0F + yCoord;
        float maxZ = 1.0F + zCoord;
        
        ForgeDirection dir = ForgeDirection.getOrientation(metadata);
        minX += extendedLength * (dir.offsetX < 0 ? dir.offsetX : 0);
        minY += extendedLength * (dir.offsetY < 0 ? dir.offsetY : 0);
        minZ += extendedLength * (dir.offsetZ < 0 ? dir.offsetZ : 0);
        
        maxX += extendedLength * (dir.offsetX > 0 ? dir.offsetX : 0);
        maxY += extendedLength * (dir.offsetY > 0 ? dir.offsetY : 0);
        maxZ += extendedLength * (dir.offsetZ > 0 ? dir.offsetZ : 0);
        
        return AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ);
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
			pNetwork.addMachine(this, oldPressure);
			Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}else{
			pNetwork = new PressureNetwork(this, oldPressure);
			Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
		}		
	}
	
	@Override
	public void invalidate(){
		super.invalidate();
		for(ForgeDirection dir: connectedSides){
			getNetwork(dir).removeMachine(this);
		}
	}
}
