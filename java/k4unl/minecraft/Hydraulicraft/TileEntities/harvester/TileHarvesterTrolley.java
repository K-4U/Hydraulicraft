package k4unl.minecraft.Hydraulicraft.TileEntities.harvester;

import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileHarvesterTrolley extends TileEntity {
	private float extendedLength;
	private float maxLength = 4F;
	private float maxSide = 8F;
	private float extendTarget = 0F;
	private float sideTarget = 0F;
	private float sideLength;
	private float movingSpeedExtending = 0.05F;
	private static final float movingSpeedSideways = 0.05F;
	private static final float movingSpeedSidewaysBack = 0.1F;
	private int dir = 0;
	private boolean harvesterPart = false;
	
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
		
		float movingSpeedPercentage = 0F;
		if(isRetracting){
			movingSpeedPercentage = movingSpeedSidewaysBack / blocksToMoveSideways;
		}else{
			movingSpeedPercentage = movingSpeedSideways / blocksToMoveSideways;
		}
		movingSpeedExtending = movingSpeedPercentage * blocksToExtendDown;
		
		if(movingSpeedExtending > 500F){ //Which is just absurd..
			if(isRetracting){
				movingSpeedExtending = movingSpeedSidewaysBack;
			}else{
				movingSpeedExtending = movingSpeedSideways;
			}
		}
		
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
			sideLength -= movingSpeedSidewaysBack;
		}else{
			sideLength = sideTarget;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void updateEntity() {
		if(!harvesterPart){
			doMove();
		}
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
		
		harvesterPart = tagCompound.getBoolean("harvesterPart");
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
		tagCompound.setBoolean("harvesterPart", harvesterPart);
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

	public boolean canPlantSeed(ItemStack seed){
		int metadata = getBlockMetadata();
		for(Seed s : Config.harvestableItems){
			if(s.getSeedId() == seed.itemID){
				if(s.getHarvesterMeta() == metadata){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
    public AxisAlignedBB getRenderBoundingBox(){
		float extendedLength = getExtendedLength();
        float sidewaysMovement = getSideLength();

        //Get rotation:
        int dir = getDir();
        float minX = 0.0F + xCoord;
        float minY = 0.0F + yCoord;
        float minZ = 0.0F + zCoord;
        float maxX = 1.0F + xCoord;
        float maxY = 1.0F + yCoord;
        float maxZ = 1.0F + zCoord;
        
        
        int dirXMin = (dir == 1 ? -1  : 0);
        int dirZMin = (dir == 0 ? -1  : 0);
        int dirXMax = (dir == 3 ? 1  : 0);
        int dirZMax = (dir == 2 ? 1  : 0);
        minX += sidewaysMovement * dirXMin;
        minY -= extendedLength;
        minZ += sidewaysMovement * dirZMin;
        
        maxX += sidewaysMovement * dirXMax;
        //maxY += extendedLength;
        maxZ += sidewaysMovement * dirZMax;
        
        return AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

	public void setDir(int dir2) {
		this.dir = dir2;
	}

	public int getDir() {
		return dir;
	}

	public void setIsHarvesterPart(boolean isit){
		harvesterPart = isit;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
}
