package k4unl.minecraft.Hydraulicraft.TileEntities.harvester;

import java.util.ArrayList;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Seed;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class TileHarvesterTrolley extends TileEntity implements IHarvesterTrolley {
	private float extendedLength;
	private float maxLength = 4F;
	private float maxSide = 8F;
	private float extendTarget = 0F;
	private float sideTarget = 0F;
	private float sideLength;
	private float movingSpeedExtending = 0.05F;
	private static final float movingSpeedSideways = 0.05F;
	private static final float movingSpeedSidewaysBack = 0.1F;
	private ForgeDirection facing = ForgeDirection.NORTH;
	private boolean harvesterPart = false;
	
	private boolean isRetracting;
	private boolean isMovingUpDown;
	private boolean isMoving;
	private boolean isMovingSideways;
	private boolean isPlanting;
	private boolean isHarvesting;
	
	
	private ItemStack plantingItem = null;
	private ArrayList<ItemStack> harvestedItems = null;
	private int locationToPlant = -1;
	private int locationToHarvest = -1;
	private TileHydraulicHarvester harvester = null;
	private TileHydraulicPiston piston = null;

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
			isMovingSideways = false;
		}else if(compResult < 0){
			isMovingSideways = true;
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
		if(piston != null){
			piston.extendTo(sideExtend);
		}
		isMoving = true;
		isMovingUpDown = true;
		
		
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
	
	@Override
	public void doMove(){
		int compResult = Float.compare(extendTarget, extendedLength);
		if(compResult > 0 && !isRetracting){
			extendedLength += movingSpeedExtending;
		}else if(compResult < 0 && isRetracting){
			extendedLength -= movingSpeedExtending;
		}else{
			extendTarget = extendedLength;
			isMovingUpDown = false;
		}
		
		compResult = Float.compare(sideTarget, sideLength);
		if(compResult > 0 && !isMovingSideways){
			sideLength += movingSpeedSideways;
		}else if(compResult < 0 && isMovingSideways){
			sideLength -= movingSpeedSidewaysBack;
		}else{
			//Arrived at location!
			sideLength = sideTarget;
			isMoving = false;
		}
		if(!isMoving && !isMovingUpDown){
			if(isPlanting){
				actuallyPlant();
			}else if(isHarvesting){
				actuallyHarvest();
			}else if(Config.get("shouldDolleyInHarvesterGoBack") && harvestedItems != null){
				harvester.putInInventory(harvestedItems);
			}
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void updateEntity() {
		if(harvester != null){
			doMove();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		extendedLength = tagCompound.getFloat("extendedLength");
		extendTarget = tagCompound.getFloat("extendTarget");
		isRetracting = tagCompound.getBoolean("isRetracting");
		isMovingSideways = tagCompound.getBoolean("isMoving");
		sideLength = tagCompound.getFloat("sideLength");
		sideTarget = tagCompound.getFloat("sideTarget");
		facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
		
		harvesterPart = tagCompound.getBoolean("harvesterPart");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setFloat("extendedLength", extendedLength);
		tagCompound.setFloat("extendTarget", extendTarget);
		tagCompound.setBoolean("isRetracting", isRetracting);
		tagCompound.setBoolean("isMoving", isMovingSideways);
		tagCompound.setFloat("sideLength", sideLength);
		tagCompound.setFloat("sideTarget", sideTarget);
		tagCompound.setInteger("facing", facing.ordinal());
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


	
	@Override
    public AxisAlignedBB getRenderBoundingBox(){
		float extendedLength = getExtendedLength();
        float sidewaysMovement = getSideLength();

        //Get rotation:
        int dir = getFacing().ordinal();
        float minX = 0.0F + xCoord;
        float minY = 0.0F + yCoord;
        float minZ = 0.0F + zCoord;
        float maxX = 1.0F + xCoord;
        float maxY = 1.0F + yCoord;
        float maxZ = 1.0F + zCoord;
        
        minX += sidewaysMovement * getFacing().offsetX;
        minY -= extendedLength;
        minZ += sidewaysMovement * getFacing().offsetZ;
        
        maxX += sidewaysMovement * getFacing().offsetZ;
        //maxY += extendedLength;
        maxZ += sidewaysMovement * getFacing().offsetX;
        
        return AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

	public void setFacing(ForgeDirection nFacing) {
		facing = nFacing;
	}

	public ForgeDirection getFacing() {
		return facing;
	}

	public void setIsHarvesterPart(boolean isit){
		harvesterPart = isit;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	
	@Override
	public boolean canHandleSeed(ItemStack seed){
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
	
	
	public Location getLocation(int length, int y){
		Location l = new Location(xCoord + getFacing().offsetX * length, yCoord + y, zCoord + getFacing().offsetZ * length);
		return l;
	}
	
	private int getBlockId(Location l){
		return worldObj.getBlockId(l.getX(), l.getY(), l.getZ());
	}
	
	@Override
	public int canPlantSeed(ItemStack[] seeds, int maxLength){
		ItemStack firstSeed = null;
		int seedLocation = 0;
		for(int i = 0; i < 9; i++){
			if(seeds[i] != null){
				if(canHandleSeed(seeds[i])){
					seedLocation = i;
					firstSeed = seeds[i];
					break;
				}
			}
		}
		if(firstSeed == null){
			return -1;
		}
		
		for(int horiz = 0; horiz <= maxLength; horiz++){
			int blockId = getBlockId(getLocation(horiz, -2));
			int soilId = getBlockId(getLocation(horiz, -3));
			boolean canIPlantHere = false;
			if(getBlockMetadata() == 0){
				//For "vanilla" plants:
				canIPlantHere = canPlantSeed(getLocation(horiz, -2), firstSeed);
			}else if(getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE){
				Location toPlaceLocation = getLocation(horiz, -2);
				canIPlantHere = Block.reed.canPlaceBlockAt(worldObj, toPlaceLocation.getX(), toPlaceLocation.getY(), toPlaceLocation.getZ());
			}else if(getBlockMetadata() == Constants.HARVESTER_ID_ENDERLILY){
				if(soilId == Block.dirt.blockID || soilId == Block.grass.blockID || soilId == Block.whiteStone.blockID){
					canIPlantHere = true;
				}else{
					canIPlantHere = false;
				}
			}
			
			if(blockId == 0 && canIPlantHere){
				locationToPlant = horiz;
				return seedLocation;
			}
		}
		
		return -1;
	}
	
	@Override
	public void doPlant(ItemStack seed){
		plantingItem = seed;
		if(getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE){
			extendTo(2F, locationToPlant);
		}else{
			extendTo(2.7F, locationToPlant);
		}
		
		isPlanting = true;
		isHarvesting = false; //Just to be safe
	}
	
	private void actuallyPlant(){
		//The trolley has arrived at the location and should plant.
		int plantId = 0;
		int plantMeta = 0;
		Location l = getLocation(locationToPlant, -2);
		if(plantingItem.getItem() instanceof IPlantable && getBlockMetadata() != Constants.HARVESTER_ID_ENDERLILY){
			IPlantable seed = (IPlantable)plantingItem.getItem();
			plantId = seed.getPlantID(worldObj, l.getX(), l.getY(), l.getZ());
			plantMeta = seed.getPlantMetadata(worldObj, l.getX(), l.getY(), l.getZ());
		}else if(getBlockMetadata() == Constants.HARVESTER_ID_ENDERLILY){
			//It probably is a ender lilly we want to plant..
			plantId = plantingItem.itemID;
			plantMeta = 0;
		}else if(getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE){
			plantId = Block.reed.blockID;
			plantMeta = 0;
		}
		
		worldObj.setBlock(l.getX(), l.getY(), l.getZ(), plantId, plantMeta, 2);
		
		plantingItem = null;
		isHarvesting = false;
		isPlanting = false;
		if(Config.get("shouldDolleyInHarvesterGoBack")){
			extendTo(0, 0);
		}else{
			extendTo(0, locationToPlant);
		}
	}
	
	private boolean canPlantSeed(Location l, ItemStack seed) {
		if(seed.getItem() instanceof IPlantable){
			int x = l.getX();
			int y = l.getY();
			int z = l.getZ();
			Block soil = Block.blocksList[worldObj.getBlockId(x, y - 1, z)];
		    return (worldObj.getFullBlockLightValue(x, y, z) >= 8 ||
		            worldObj.canBlockSeeTheSky(x, y, z)) &&
		            (soil != null && soil.canSustainPlant(worldObj, x, y - 1, z,
		                  ForgeDirection.UP, (IPlantable)seed.getItem()));			
		}else{
			return false;
		}
	    
	}
	
	private ArrayList<ItemStack> getDroppedItems(int h){
		Location cropLocation = getLocation(h, -2);
		int id = worldObj.getBlockId(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
		int metaData = worldObj.getBlockMetadata(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
		if(id > 0){
			Block toHarvest = Block.blocksList[id];
			return toHarvest.getBlockDropped(worldObj, cropLocation.getX(), cropLocation.getY(), cropLocation.getZ(), metaData, 0);
		}else{
			return null;
		}
	}
	
	@Override
	public ArrayList<ItemStack> checkHarvest(int maxLen){
		for(int horiz = 0; horiz <= maxLen; horiz++){
			Location l;
			if(getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE){
				l = getLocation(horiz, -1);
			}else{
				l = getLocation(horiz, -2);
			}
			int x = l.getX();
			int y = l.getY();
			int z = l.getZ();
			
			int blockId = worldObj.getBlockId(x, y, z);
			int metaData = worldObj.getBlockMetadata(x, y, z);
			
			for(Seed harvestable : Config.harvestableItems){
				boolean idCorrect = harvestable.getItemId() == blockId;
				boolean fullGrown = harvestable.getFullGrown() == metaData || getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE;
				boolean correctDolley = harvestable.getHarvesterMeta() == getBlockMetadata();
				if(idCorrect && fullGrown && correctDolley){
					ArrayList<ItemStack> dropped = getDroppedItems(horiz);
					locationToHarvest = horiz;
					return dropped;
				}
			}
		}
		return null;
	}


	@Override
	public void setPiston(TileHydraulicPiston nPiston) {
		piston = nPiston;
	}
	
	@Override
	public void setHarvester(TileHydraulicHarvester nHarvester){
		harvester = nHarvester;
	}

	@Override
	public boolean isMoving() {
		return isMovingSideways;
	}
	
	@Override
	public boolean isWorking() {
		return isPlanting || isHarvesting || isMoving || isMovingUpDown;
	}

	@Override
	public void doHarvest() {
		plantingItem = null;
		if(getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE){
			extendTo(1.0F, locationToHarvest);
		}else{
			extendTo(2.0F, locationToHarvest);
		}
		isPlanting = false; //Just to be safe
		isHarvesting = true; 
	}
	
	private void actuallyHarvest(){
		ArrayList<ItemStack> dropped = getDroppedItems(locationToHarvest);
		Location cropLocation;
		if(getBlockMetadata() == Constants.HARVESTER_ID_SUGARCANE){
			cropLocation = getLocation(locationToHarvest, -1);
		}else{
			cropLocation = getLocation(locationToHarvest, -2);
		}
		worldObj.destroyBlock(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ(), false);
		//worldObj.setBlockToAir(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
		
		
		isHarvesting = false;
		isPlanting = false;
		if(Config.get("shouldDolleyInHarvesterGoBack")){
			harvestedItems = dropped;
			extendTo(0, 0);
		}else{
			harvester.putInInventory(dropped);
			extendTo(0, locationToHarvest);
		}
	}
}
