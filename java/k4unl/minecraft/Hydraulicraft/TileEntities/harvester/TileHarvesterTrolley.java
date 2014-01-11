package k4unl.minecraft.Hydraulicraft.TileEntities.harvester;

import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

public class TileHarvesterTrolley extends TileEntity {
	private float extendedLength;
	private float maxLength = 2F;
	private float maxSide = 8F;
	private float extendTarget = 0F;
	private float sideTarget = 0F;
	private float sideLength;
	private float movingSpeedExtending = 0.05F;
	private static final float movingSpeedSideways = 0.05F;
	private int dir = 0;
	
	private boolean isRetracting;
	private boolean isMoving;

	public void extendTo(float blocksToExtend, float sideExtend){
		if(blocksToExtend > maxLength){
			blocksToExtend = maxLength;
		}
		if(blocksToExtend < 0){
			blocksToExtend = 0;
		}

		if(sideExtend < 0){
			sideExtend = 0;
		}
		
		extendTarget = blocksToExtend;
		sideTarget = sideExtend;
		
		
		int compResult = Float.compare(extendTarget, extendedLength); 
		if(compResult > 0){
			isRetracting = false;
		}else if(compResult < 0){
			isRetracting = true;
		}
		
		compResult = Float.compare(sideTarget, sideLength); 
		if(compResult > 0){
			isMoving = false;
		}else if(compResult < 0){
			isMoving = true;
		}
		
		float blocksToMoveSideways = Math.abs(sideTarget - sideLength);
		float blocksToExtendDown = Math.abs(extendTarget - extendedLength);
		float movingSpeedPercentage = movingSpeedSideways / blocksToMoveSideways;
		movingSpeedExtending = movingSpeedPercentage * blocksToExtendDown;
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
	
	public void doMove(){
		int compResult = Float.compare(extendTarget, extendedLength);
		if(compResult > 0 && !isRetracting){
			extendedLength += movingSpeedExtending;
		}else if(compResult < 0 && isRetracting){
			extendedLength -= movingSpeedExtending;
		}else{
			extendTarget = extendedLength;
		}
		
		compResult = Float.compare(sideTarget, sideLength);
		if(compResult > 0 && !isMoving){
			sideLength += movingSpeedSideways;
		}else if(compResult < 0 && isMoving){
			sideLength -= movingSpeedSideways;
		}else{
			sideLength = sideTarget;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		extendedLength = tagCompound.getFloat("extendedLength");
		extendTarget = tagCompound.getFloat("extendTarget");
		isRetracting = tagCompound.getBoolean("isRetracting");
		isMoving = tagCompound.getBoolean("isMoving");
		sideLength = tagCompound.getFloat("sideLength");
		sideTarget = tagCompound.getFloat("sideTarget");
		dir = tagCompound.getInteger("dir");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setFloat("extendedLength", extendedLength);
		tagCompound.setFloat("extendTarget", extendTarget);
		tagCompound.setBoolean("isRetracting", isRetracting);
		tagCompound.setBoolean("isMoving", isMoving);
		tagCompound.setFloat("sideLength", sideLength);
		tagCompound.setFloat("sideTarget", sideTarget);
		tagCompound.setInteger("dir", dir);
	}
	
	
	public float getSideLength(){
		return sideLength;
	}
	
	public float getExtendedLength(){
		return extendedLength;
	}
	

	public float getMaxLength(){
		return maxLength;
	}
	
	public float getExtendTarget(){
		return extendTarget;
	}
	
	public float getSideTarget(){
		return sideTarget;
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

	public void setDir(int dir2) {
		this.dir = dir2;
	}

	public int getDir() {
		return dir;
	}
	
	
}
