package k4unl.minecraft.Hydraulicraft.TileEntities.consumers;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

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
    private static final float movingSpeed = 0.01F;
    
    public TileMovingPane(){
    	connectedSides = new ArrayList<ForgeDirection>();
    }

	@Override
	public int getMaxStorage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxPressure(boolean isOil, ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
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
		prevMovedPercentage = movedPercentage;
		movedPercentage += movingSpeed;
		if(movedPercentage > 1.0F){
			movedPercentage = 0.0F;
			prevMovedPercentage = 0.0F;
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
		// TODO Auto-generated method stub

	}

	@Override
	public PressureNetwork getNetwork(ForgeDirection side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNetwork(ForgeDirection side, PressureNetwork toSet) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getPressure(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPressure(float newPressure, ForgeDirection side) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getFluidInNetwork(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFluidCapacity(ForgeDirection from) {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return false;
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
	
}
