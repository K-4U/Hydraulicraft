package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileMovingPane extends TileHydraulicBase implements IHydraulicConsumer {
    private Location parent;
    private Location child;
    private ForgeDirection facing = ForgeDirection.UP;
    private ForgeDirection paneFacing = ForgeDirection.NORTH;
    
    private boolean isPane = false;
    private boolean isRotating = false;
    
    private float movedPercentage = 0.0F;
    private float prevMovedPercentage = 0.0F;
    private float movingSpeed = 0.01F;
    private float target = 1.0F;
    
    public TileMovingPane(){
    	super(PressureTier.HIGHPRESSURE, 5);
    	super.init(this);
    }

    public boolean getIsRotating(){
    	return isRotating;
    }
    
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
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
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if(isPane){
			if(parent != null){
				tagCompound.setIntArray("parent", parent.getLocation());;
			}
		}else{
			if(child != null){
				tagCompound.setIntArray("child", child.getLocation());;
			}
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
	public void updateEntity() {
		super.updateEntity();
		
		if(isRotating && isPane){
			if(getParent().hasEnoughPressure()){
				prevMovedPercentage = movedPercentage;
				movedPercentage += (movingSpeed * getParent().getPressureFactor());
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
		}
		if(!worldObj.isRemote && isPane == false){
			int targetIsNul = Float.compare(target, 0.0F);
			int movingSpeedIsNul = Float.compare(movingSpeed, 0.0F);
			if(getRedstonePowered(this)){
				if(Float.compare(target, 1.0F) != 0 && Float.compare(movingSpeed, 0.0F) < 0){
					movingSpeed = 0.1F;
					target = 1.0F;
					isRotating = true;
					getChild().setSpeed(movingSpeed);
					getChild().setTarget(target);
					getHandler().updateBlock();
				}
			}else if(Float.compare(target, 0.0F) != 0 && Float.compare(movingSpeed, 0.0F) >= 0){
				movingSpeed = -0.1F;
				target = 0.0F;
				isRotating = true;
				getChild().setSpeed(movingSpeed);
				getChild().setTarget(target);
				getHandler().updateBlock();
			}
		}
	}

	private boolean hasEnoughPressure() {
		return getPressure(ForgeDirection.UNKNOWN) > Constants.MIN_PRESSURE_PANE;
	}
	
	private float getPressureFactor(){
		return getPressure(ForgeDirection.UNKNOWN) / getMaxPressure(isOilStored(), ForgeDirection.UNKNOWN);
	}

	@Override
	public void onFluidLevelChanged(int old) { }

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
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(!isPane){
			if(getChild() != null){
				if(getChild().getIsRotating()){
					return Constants.PRESSURE_PANE_PER_TICK * (10 * getPressureFactor());
				}
			}
		}
		return 0f;
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
		if(isRotating){
			return getMovedPercentage() + (getMovedPercentage() - prevMovedPercentage) * f;
		}else{
			return getMovedPercentage();
		}
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
	
	@Override
	public boolean getRedstonePowered(){
		return super.getRedstonePowered();
	}
	
	public boolean getRedstonePowered(Object caller){
		List<Object> called = new ArrayList<Object>();
		return getRedstonePowered(called);
	}
	
	public boolean getRedstonePowered(List<Object> called){
		called.add(this);
		
		boolean allFalse = true;
		boolean oneTrue = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity te = Functions.getTEInDir(getWorldObj(), xCoord, yCoord, zCoord, dir);
			if(te instanceof TileMovingPane){
				if(!called.contains(te)){
					if(((TileMovingPane) te).getRedstonePowered(called) == true){
						oneTrue = true;
						allFalse = false;
					}
				}
			}
		}
		if(oneTrue){
			return true;
		}
		if(allFalse){
			return super.getRedstonePowered();
		}
		return super.getRedstonePowered();
	}
	
	@Override
	public boolean shouldRenderInPass(int pass){
        return true;
    }
}
