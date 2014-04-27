package k4unl.minecraft.Hydraulicraft.TileEntities.consumers;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileMovingPane extends TileEntity implements IHydraulicConsumer {
    private Location parent;
    private Location child;
    private ForgeDirection facing = ForgeDirection.UP;
    private ForgeDirection paneFacing = ForgeDirection.NORTH;
    
    private boolean isPane = false;
    private boolean isRotating = false;
    
    private IBaseClass baseHandler;

    private PressureNetwork pNetwork;
    private List<ForgeDirection> connectedSides;
    
    private float movedPercentage = 0.0F;
    private float prevMovedPercentage = 0.0F;
    private float movingSpeed = 0.01F;
    private float target = 1.0F;
    
    public TileMovingPane(){
    	connectedSides = new ArrayList<ForgeDirection>();
    }

	@Override
	public int getMaxStorage() {
		return FluidContainerRegistry.BUCKET_VOLUME * 5;
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		if(isOil){
			return Constants.MAX_MBAR_OIL_TIER_2;
			
		}else{
			return Constants.MAX_MBAR_WATER_TIER_2;
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
		isPane = tagCompound.getBoolean("isPane");
		if(isPane){
			parent = new Location(tagCompound.getIntArray("parent"));
		}else{
			child = new Location(tagCompound.getIntArray("child"));
		}
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		paneFacing = ForgeDirection.getOrientation(tagCompound.getInteger("paneFacing"));
		
		if(isPane){
			isRotating = tagCompound.getBoolean("isRotating");
			movedPercentage = tagCompound.getFloat("movedPercentage");
			movingSpeed = tagCompound.getFloat("movingSpeed");
			target = tagCompound.getFloat("target");
		}
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		if(isPane){
			tagCompound.setIntArray("parent", parent.getLocation());;
		}else{
			tagCompound.setIntArray("child", child.getLocation());;
		}
		tagCompound.setBoolean("isPane", isPane);
		tagCompound.setInteger("facing", facing.ordinal());
		tagCompound.setInteger("paneFacing", paneFacing.ordinal());
		
		tagCompound.setBoolean("isRotating", isRotating);
		tagCompound.setFloat("movedPercentage", movedPercentage);
		tagCompound.setFloat("movingSpeed", movingSpeed);
		tagCompound.setFloat("target", target);
	}

	@Override
	public void onDataPacket(NetworkManager net,
			S35PacketUpdateTileEntity packet) {
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
		
		
		if(isRotating && isPane){
			prevMovedPercentage = movedPercentage;
			movedPercentage += movingSpeed;
			if(Float.compare(movedPercentage, target) >= 0 && movingSpeed > 0.0F){
				isRotating = false;
				prevMovedPercentage = target;
				movedPercentage = target;
				getHandler().updateBlock();
			}else if(Float.compare(movedPercentage, target) <= 0 && movingSpeed <= 0.0F){
				isRotating = false;
				movingSpeed = 0.0F;
				prevMovedPercentage = target;
				movedPercentage = target;
				getHandler().updateBlock();
			}
		}
		if(!worldObj.isRemote && isPane == false){
			int targetIsNul = Float.compare(target, 0.0F);
			int movingSpeedIsNul = Float.compare(movingSpeed, 0.0F);
			if(getHandler().getRedstonePowered()){
				if(Float.compare(target, 0.0F) != 0 && Float.compare(movingSpeed, 0.0F) >= 0){
					movingSpeed = -0.01F;
					target = 0.0F;
					isRotating = true;
					getChild().setSpeed(movingSpeed);
					getChild().setTarget(target);
					getHandler().updateBlock();
				}
			}else if(Float.compare(target, 1.0F) != 0 && Float.compare(movingSpeed, 0.0F) < 0){
				movingSpeed = 0.01F;
				target = 1.0F;
				isRotating = true;
				getChild().setSpeed(movingSpeed);
				getChild().setTarget(target);
				getHandler().updateBlock();
			}
		}
	}

	@Override
	public void validate() {
		super.validate();
	}

	@Override
	public void onPressureChanged(float old) {

	}

	@Override
	public void onFluidLevelChanged(int old) {

	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		if(isPane){
			return false;
		}
		return !side.equals(getFacing());
	}

	public ForgeDirection getFacing() {
		return facing;
	}
	
	public void setFacing(ForgeDirection n) {
		facing = n;
		getHandler().updateBlock();
	}

	@Override
	public void firstTick() {

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
		return getNetwork(from).getPressure();
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		getNetwork(side).setPressure(newPressure);

	}

	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		if(worldObj.isRemote){
			return 0;
		}else{
			return getNetwork(from).getFluidInNetwork();
		}
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		if(worldObj.isRemote){
			return 0;
		}else{
			return getNetwork(from).getFluidCapacity();
		}
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onBlockBreaks() {

	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return true;
	}
	
	public void setIsPane(boolean isIt){
		isPane = isIt;
		getHandler().updateBlock();
	}
	
	public boolean getIsPane(){
		return isPane;
	}
	
	public void setParentLocation(Location p){
		parent = p;
	}
	
	public Location getParentLocation(){
		return parent;
	}
	
	public void setChildLocation(Location c){
		child = c;
	}
	
	public Location getChildLocation(){
		return child;
	}
	
	public float getMovedPercentage(){
		return movedPercentage;
	}

	public float getMovedPercentageForRender(float f) { 
		return getMovedPercentage() + (getMovedPercentage() - prevMovedPercentage) * f;
	}
	
	public void setPaneFacing(ForgeDirection n){
		paneFacing = n;
	}
	public ForgeDirection getPaneFacing(){
		return paneFacing;
	}
	
	public void setTarget(float nTarget){
		target = nTarget;
		isRotating = true;
		getHandler().updateBlock();
	}
	public void setSpeed(float nSpeed){
		movingSpeed = nSpeed;
		getHandler().updateBlock();
	}
	
	public TileMovingPane getChild(){
		return (TileMovingPane)worldObj.getTileEntity(child.getX(), child.getY(), child.getZ());
	}
	
	public TileMovingPane getParent(){
		return (TileMovingPane)worldObj.getTileEntity(parent.getX(), parent.getY(), parent.getZ());
	}
}
