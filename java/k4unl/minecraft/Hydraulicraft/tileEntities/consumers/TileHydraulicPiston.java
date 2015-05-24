package k4unl.minecraft.Hydraulicraft.tileEntities.consumers;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileHydraulicPiston extends TileHydraulicBase implements IHydraulicConsumer {
    private float oldExtendedLength;
    private float extendedLength;
    private float   maxLength       = 4F;
    private float   extendTarget    = 0F;
    private float   movingSpeed     = 0.05F;
    private float   movingSpeedBack = 0.1F;
    private boolean harvesterPart   = false;
    private Location harvesterLocation;

    private boolean isRetracting;
    private ForgeDirection facing = ForgeDirection.NORTH;

    public TileHydraulicPiston() {

        super(5);
        super.init(this);
    }

    public float getExtendTarget() {

        return extendTarget;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        extendedLength = tagCompound.getFloat("extendedLength");
        maxLength = tagCompound.getFloat("maxLength");
        extendTarget = tagCompound.getFloat("extendTarget");
        harvesterPart = tagCompound.getBoolean("harvesterPart");
        isRetracting = tagCompound.getBoolean("isMoving");
        oldExtendedLength = tagCompound.getFloat("oldExtendedLength");

        harvesterLocation = new Location(tagCompound.getIntArray("harvesterLocation"));
        facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setFloat("extendedLength", extendedLength);
		tagCompound.setFloat("maxLength", maxLength);
		tagCompound.setFloat("extendTarget", extendTarget);
		tagCompound.setBoolean("harvesterPart", harvesterPart);
		tagCompound.setBoolean("isMoving", isRetracting);
		tagCompound.setFloat("oldExtendedLength", oldExtendedLength);
		if(harvesterLocation != null){
			tagCompound.setIntArray("harvesterLocation", harvesterLocation.getLocation());
		}
		tagCompound.setInteger("facing", facing.ordinal());
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
		oldExtendedLength = extendedLength;
		
		int compResult = Float.compare(extendTarget, extendedLength);
		if(simulate == false){
			if(compResult > 0 && !isRetracting){
				extendedLength += movingSpeed;
			}else if(compResult < 0 && isRetracting){
				extendedLength -= movingSpeedBack;
			}else{
				extendedLength = extendTarget;
				oldExtendedLength = extendedLength;
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		if(compResult >= 0){
			return Constants.PRESSURE_USAGE_PISTON;
		}
		return 0;
	}
	
/*
	@Override
	public IBaseClass getHandler() {
		if(harvesterPart && getHarvester() != null){
			if(baseHandler == null) baseHandler = getHarvester().getHandler();
		}else{
			if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getBaseClass(this);
		}
        return baseHandler;
	}*/

	public TileHydraulicHarvester getHarvester(){
		if(harvesterPart){
			if(harvesterLocation != null){
				TileEntity t = worldObj.getTileEntity(harvesterLocation.getX(), harvesterLocation.getY(), harvesterLocation.getZ());
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
			//getHandler().updateEntity();
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
        
        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
	
	@Override
	public void validate(){
		super.validate();
	}

	@Override
	public void onFluidLevelChanged(int old) { }
	
	@Override
	public boolean canConnectTo(ForgeDirection side) {
		return true;
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}
	
	public float getOldExtendedLength(){
	    return oldExtendedLength;
	}
	
	public ForgeDirection getFacing(){
		return facing;
	}
	
	public void setFacing(ForgeDirection nFacing){
		facing = nFacing;
	}
}
