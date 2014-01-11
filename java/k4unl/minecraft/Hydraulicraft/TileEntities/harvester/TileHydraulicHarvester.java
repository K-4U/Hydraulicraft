package k4unl.minecraft.Hydraulicraft.TileEntities.harvester;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class TileHydraulicHarvester extends TileEntity implements IHydraulicConsumer, ISidedInventory {
	private ItemStack[] seedsStorage;
	private ItemStack[] outputStorage;
	
	private IBaseClass baseHandler;
	private boolean isMultiblock;
	private int harvesterLength;
	private int harvesterWidth;
	private int dir = -1;
	private boolean firstRun = true;
	
	private boolean isPlanting = false;
	private boolean isHarvesting = false;
	
	private int pistonMoving = -1;
	private int harvestLocationH;
	private int harvestLocationW;
	private int plantLocationH;
	private int plantLocationW;
	private ItemStack plantingItem;
	
	private List<Location> pistonList = new ArrayList<Location>();
	private List<Location> trolleyList = new ArrayList<Location>();
	
	
	private static final int idHorizontalFrame = Ids.blockHydraulicHarvester.act;
	private static final int idVerticalFrame = Block.fence.blockID; //TODO: replace this
	private static final int idPiston = Ids.blockHydraulicPiston.act;
	private static final int idEndBlock = Ids.blockHydraulicPressureWall.act;
	private static final int idTrolley = Ids.blockHarvesterTrolley.act;
	
	
	public TileHydraulicHarvester(){
		seedsStorage = new ItemStack[9];
		outputStorage = new ItemStack[9];
	}
	
	@Override
	public int getMaxStorage() {
		int maxStorage =FluidContainerRegistry.BUCKET_VOLUME * 16;
		for(Location l : pistonList){
			TileHydraulicPiston p = getPistonFromCoords(l);
			if(p!=null){
				maxStorage += p.getMaxStorage();
			}
		}
		return maxStorage;
	}

	@Override
	public float getMaxPressure() {
		if(getHandler().isOilStored()){
			return Constants.MAX_MBAR_OIL_TIER_3;
		}else{
			return Constants.MAX_MBAR_WATER_TIER_3;
		}
	}

	@Override
	public IBaseClass getHandler() {
		if(baseHandler == null) baseHandler = HydraulicBaseClassSupplier.getConsumerClass(this);
        return baseHandler;
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
	public void readNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		isMultiblock = tagCompound.getBoolean("isMultiblock");
		harvesterLength = tagCompound.getInteger("harvesterLength");
		harvesterWidth = tagCompound.getInteger("harvesterWidth");
		dir = tagCompound.getInteger("dir");
		readPistonListFromNBT(tagCompound);
		for(int i = 0; i<9; i++){
			NBTTagCompound tc = tagCompound.getCompoundTag("seedsStorage"+i);
			seedsStorage[i] = ItemStack.loadItemStackFromNBT(tc);
			
			tc = tagCompound.getCompoundTag("outputStorage"+i);
			outputStorage[i] = ItemStack.loadItemStackFromNBT(tc);
		}
		
		//harvestLocationH = tagCompound.getInteger("harvestLocationH");
		//harvestLocationW = tagCompound.getInteger("harvestLocationW");
		isHarvesting = tagCompound.getBoolean("isHarvesting");
		
		//plantLocationH = tagCompound.getInteger("plantLocationH");
		//plantLocationW = tagCompound.getInteger("plantLocationW");
		isPlanting = tagCompound.getBoolean("isPlanting");
		
	}

	@Override
	public void writeNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setBoolean("isMultiblock", isMultiblock);
		tagCompound.setInteger("harvesterLength", harvesterLength);
		tagCompound.setInteger("harvesterWidth", harvesterWidth);
		tagCompound.setInteger("dir", dir);
		writePistonListToNBT(tagCompound);

		for(int i = 0; i<9; i++){
			if(seedsStorage[i] != null){
				NBTTagCompound tc = new NBTTagCompound();
				seedsStorage[i].writeToNBT(tc);
				tagCompound.setCompoundTag("seedsStorage"+i, tc);
			}
			if(outputStorage[i] != null){
				NBTTagCompound tc = new NBTTagCompound();
				outputStorage[i].writeToNBT(tc);
				tagCompound.setCompoundTag("outputStorage"+i, tc);
			}
		}
		
		tagCompound.setInteger("harvestLocationH", harvestLocationH);
		tagCompound.setInteger("harvestLocationW", harvestLocationW);
		tagCompound.setBoolean("isHarvesting", isHarvesting);
		
		tagCompound.setInteger("plantLocationH", plantLocationH);
		tagCompound.setInteger("plantLocationW", plantLocationW);
		tagCompound.setBoolean("isPlanting", isPlanting);
		
	}
	
	public void writePistonListToNBT(NBTTagCompound tagCompound){
		NBTTagCompound tagList = new NBTTagCompound();
		
		tagList.setInteger("length", pistonList.size());
		int index = 0;
		for(Location l : pistonList){
			tagList.setIntArray(index + "", l.getLocation());
			index++;
		}
		tagCompound.setCompoundTag("pistonList", tagList);
	}
	
	public void writeTrolleyListToNBT(NBTTagCompound tagCompound){
		NBTTagCompound tagList = new NBTTagCompound();
		
		tagList.setInteger("length", trolleyList.size());
		int index = 0;
		for(Location l : trolleyList){
			tagList.setIntArray(index + "", l.getLocation());
			index++;
		}
		tagCompound.setCompoundTag("trolleyList", tagList);
	}
	
	public void readTrolleyListFromNBT(NBTTagCompound tagCompound){
		NBTTagCompound tagList = tagCompound.getCompoundTag("trolleyList");
		pistonList.clear();
		if(tagList != null){
			int length = tagList.getInteger("length");
			for(int i = 0; i < length; i++){
				Location l = new Location(tagList.getIntArray(i+""));
				trolleyList.add(l);
			}
		}
	}
	
	public void readPistonListFromNBT(NBTTagCompound tagCompound){
		NBTTagCompound tagList = tagCompound.getCompoundTag("pistonList");
		pistonList.clear();
		if(tagList != null){
			int length = tagList.getInteger("length");
			for(int i = 0; i < length; i++){
				Location l = new Location(tagList.getIntArray(i+""));
				pistonList.add(l);
			}
		}
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		getHandler().onDataPacket(net, packet);
	}

	@Override
	public Packet getDescriptionPacket() {
		return getHandler().getDescriptionPacket();
	}

	@Override
	public void updateEntity() {
		//Every half second.. Or it should be..
		if(!worldObj.isRemote){
			if(worldObj.getTotalWorldTime() % 60 == 0){
				if(firstRun){
					firstRun = false;
					if(isMultiblock){
						convertMultiblock();
					}
				}
				if(!isMultiblock){
					int dir = 0;
					while(dir < 4){
						if(checkMultiblock(dir)){
							this.dir = dir;
							isMultiblock = true;
							Functions.showMessageInChat("Width of harvester("+dir+"): " + harvesterWidth);
							Functions.showMessageInChat("Length of harvester("+dir+"): " + harvesterLength);
							convertMultiblock();
							break;
						}
						dir++;
					}
				}else{
					if(!checkMultiblock(dir)){
						//Multiblock no longer valid!
						isMultiblock = false;
						invalidateMultiblock();	
					}
				}
				
				if(isMultiblock){
					//TileHydraulicPiston p = getPistonFromList(0);
					//float pExtended = p.getExtendedLength();
					//p.extendTo(pExtended + 1);
				}
			}
		}
		getHandler().updateEntity();
	}
	
	private TileHydraulicPiston getPistonFromList(int index){
		Location v = pistonList.get(index);
		return (TileHydraulicPiston) worldObj.getBlockTileEntity(v.getX(), v.getY(), v.getZ());
	}
	
	private TileHydraulicPiston getPistonFromCoords(Location v){
		return (TileHydraulicPiston) worldObj.getBlockTileEntity(v.getX(), v.getY(), v.getZ());
	}

	private TileHarvesterTrolley getTrolleyFromList(int index){
		Location v = trolleyList.get(index);
		return (TileHarvesterTrolley) worldObj.getBlockTileEntity(v.getX(), v.getY(), v.getZ());
	}
	
	private TileHarvesterTrolley getTrolleyFromCoords(Location v){
		return (TileHarvesterTrolley) worldObj.getBlockTileEntity(v.getX(), v.getY(), v.getZ());
	}
	
	@Override
	public float workFunction(boolean simulate) {
		if(isMultiblock){
			if(canRun() && checkHarvest(true)) {
				if(simulate == false){
					if(!isHarvesting && !isPlanting){
						checkHarvest(false);
						if(!isHarvesting){
							checkPlantable();
						}
						return 1F;
					}else{
						if(pistonMoving != -1){
							TileHydraulicPiston p = getPistonFromList(pistonMoving);
							if(Float.compare(p.getExtendedLength(), p.getExtendTarget()) == 0){
								if((int)p.getExtendTarget() == 0){
									isHarvesting = false;
									isPlanting = false;
									pistonMoving = -1;
								}else{
									if(isHarvesting){
										doHarvest();
									}else if(isPlanting){
										doPlant();
									}
								}
							}
							updateTrolleys();
							return p.workFunction(simulate);
						}else{
							return 1F;
						}
					}
				}else{
					return 1F;
				}
	        } else {
	            return 0F;
	        }
		}else{
			return 0F;
		}
	}
	
	private void updateTrolleys(){
		for(Location l: trolleyList){
			getTrolleyFromCoords(l).doMove();
		}
	}
	
	private void checkPlantable(){
		//Pick an item, any item!
		ItemStack firstSeed = null;
		int seedLocation = 0;
		for(int i = 0; i < 9; i++){
			if(seedsStorage[i] != null){
				seedLocation = i;
				firstSeed = seedsStorage[i];
				break;
			}
		}
		if(firstSeed == null){
			return;
		}
		
		for(int w = 0; w < harvesterWidth; w++){
			for(int horiz = 0; horiz < harvesterLength; horiz++){
				Location l = getLocationInHarvester(horiz, w);
				int x = l.getX();
				int y = l.getY();
				int z = l.getZ();
				
				int blockId = worldObj.getBlockId(x, y, z);
				boolean canPlant = canPlantSeed(x, y, z, firstSeed);
				int metaData = worldObj.getBlockMetadata(x, y, z);
				
				if(blockId == 0 && canPlant){
					//Tell it to plant!
					decrStackSize(seedLocation, 1);
					plantingItem = firstSeed;
					plantingItem.stackSize = 1;
					isPlanting = true;
					moveTrolley(horiz, w);
					return;
				}
			}
		}
	}
	
	private void doPlant(){
		//The trolley has arrived at the location and should plant.
		IPlantable seed = (IPlantable)plantingItem.getItem();
		Location l = getLocationInHarvester(plantLocationH, plantLocationW);
		int plantId = seed.getPlantID(worldObj, l.getX(), l.getY(), l.getZ());
		int plantMeta = seed.getPlantMetadata(worldObj, l.getX(), l.getY(), l.getZ());
		
		worldObj.setBlock(l.getX(), l.getY(), l.getZ(), plantId, plantMeta, 2);
		
		moveTrolley(0, plantLocationW);
	}
	
	public boolean canPlantSeed(int x, int y, int z, ItemStack seed) {
		if(seed.getItem() instanceof IPlantable){
			Block soil = Block.blocksList[worldObj.getBlockId(x, y - 1, z)];
		    return (worldObj.getFullBlockLightValue(x, y, z) >= 8 ||
		            worldObj.canBlockSeeTheSky(x, y, z)) &&
		            (soil != null && soil.canSustainPlant(worldObj, x, y - 1, z,
		                  ForgeDirection.UP, (IPlantable)seed.getItem()));			
		}else{
			return false;
		}
	    
	}
	
	
	private boolean checkHarvest(boolean simulate){
		//For now, only crops!
		for(int w = 0; w < harvesterWidth; w++){
			for(int horiz = 0; horiz < harvesterLength; horiz++){
				Location l = getLocationInHarvester(horiz, w);
				int x = l.getX();
				int y = l.getY();
				int z = l.getZ();
				
				int blockId = worldObj.getBlockId(x, y, z);
				int metaData = worldObj.getBlockMetadata(x, y, z);
				
				for(int[] harvestable : Config.harvestableItems){
					if(harvestable[0] == blockId && harvestable[1] == metaData){
						if(!simulate){
							isHarvesting = true;
							moveTrolley(horiz, w);
							return true;
						}else{
							ArrayList<ItemStack> dropped = getDroppedItems(horiz, w);
							//Check to see if there is place for these items!
							if(dropped == null){
								return true;
							}
							boolean placeForAll = true;
							for(ItemStack st: dropped){
								if(!isPlaceForItems(st)){
									placeForAll = false;
								}
							}
							return placeForAll;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean isPlaceForItems(ItemStack itemStack){
		//First of all:
		
		if(itemStack.getItem() instanceof ItemSeeds){
			//Check all the locations!
			for(ItemStack st : seedsStorage){
				if(itemStack.stackSize > 0){
					if(st != null){
						if(st.isItemEqual(itemStack)){
							if(st.stackSize < 64){
								if(st.stackSize + itemStack.stackSize <= 64){
									return true;
								}else{
									itemStack.stackSize = 64 - st.stackSize;
								}
							}
						}
					}else{
						return true;
					}
				}
			}
		}
		for(ItemStack st : outputStorage){
			if(itemStack.stackSize > 0){
				if(st != null){
					if(st.isItemEqual(itemStack)){
						if(st.stackSize < 64){
							if(st.stackSize + itemStack.stackSize <= 64){
								return true;
							}else{
								itemStack.stackSize = 64 - st.stackSize;
							}
						}
					}
				}else{
					return true;
				}
			}
		}
		
		
		if(itemStack.stackSize > 0){
			return false;
		}else{
			return true;
		}
	}
	
	private Location getLocationInHarvester(int h, int w){
		int nsToAdd = (dir == 1 ? -h : (dir == 3 ? h : 0));
		int ewToAdd = (dir == 0 ? -h : (dir == 2 ? h : 0));
		int nsSide = (dir == 2 ? -w : (dir == 0 ? w : 0));
		int ewSide = (dir == 1 ? -w : (dir == 3 ? w : 0));
		int x = xCoord + nsToAdd + nsSide;
		int y = yCoord;
		int z = zCoord + ewToAdd + ewSide;
		
		return new Location(x, y, z);
	}
	
	private ArrayList<ItemStack> getDroppedItems(int h, int w){
		Location cropLocation = getLocationInHarvester(harvestLocationH, harvestLocationW);
		int id = worldObj.getBlockId(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
		int metaData = worldObj.getBlockMetadata(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
		if(id > 0){
			Block toHarvest = Block.blocksList[id];
			return toHarvest.getBlockDropped(worldObj, cropLocation.getX(), cropLocation.getY(), cropLocation.getZ(), metaData, 0);
		}else{
			return null;
		}
	}
	
	private void doHarvest(){
		/*
		TileHydraulicPiston p = getPistonFromList(pistonMoving);
		if(Float.compare(p.getExtendedLength(), harvestLocationH) == 0){*/
			//It should extend the trolley here. But for now let's just keep it at this!
			//Break the block.
		if(harvestLocationH > 0){
			ArrayList<ItemStack> dropped = getDroppedItems(harvestLocationH, harvestLocationW);
			Location cropLocation = getLocationInHarvester(harvestLocationH, harvestLocationW);
			worldObj.setBlockToAir(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
				
			Location dropLoc = getLocationInHarvester(-1, 0);
			putInInventory(dropped);
			moveTrolley(0, harvestLocationW);
		}
		/*}else{
			if(Float.compare(p.getExtendTarget(), harvestLocationH) != 0){
				p.extendTo(harvestLocationH);
			}
		}*/
	}
	
	private void putInInventory(ArrayList<ItemStack> toPut){
		for (ItemStack itemStack : toPut) {
			for(int i = 0; i < getSizeInventory(); i++){
				if(itemStack.stackSize > 0){
					if(canInsertItem(i, itemStack, -1)){
						ItemStack sis = getStackInSlot(i);
						if(sis == null){
							sis = itemStack.copy();
							itemStack.stackSize = 0;
							setInventorySlotContents(i, sis);
							break;
						}
						if((sis.stackSize + itemStack.stackSize) <= 64){
							sis.stackSize+= itemStack.stackSize;
							itemStack.stackSize = 0;
						}else{
							itemStack.stackSize = itemStack.stackSize % 64;
							sis.stackSize = 64;
						}
						setInventorySlotContents(i, sis);
					}
				}
			}
		}
	}
	
	private void moveTrolley(int h, int w){
		if(w > harvesterWidth){
			return;
		}
		
		if(h > harvesterLength){
			return;
		}
		
		pistonMoving = w;
		if(isHarvesting){
			harvestLocationH = h;
			harvestLocationW = w;
		}else if(isPlanting){
			plantLocationH = h;
			plantLocationW = w;
			
		}
		TileHydraulicPiston p = getPistonFromList(w);
		p.extendTo(h-1);
		
		if(h == 0){
			getTrolleyFromList(w).extendTo(0, h-1);			
		}else{
			getTrolleyFromList(w).extendTo(2, h-1);
		}
	}
	
	private boolean canRun(){
		if(!isMultiblock) return false;
		if(Float.compare(getHandler().getPressure(), Constants.MIN_REQUIRED_PRESSURE_HARVESTER) < 0) return false;
		if(pistonList.size() == 0) return false;
		//Check all the output slots:
		boolean allFull = true;
		for(int i = 0; i< 9; i++){
			if(outputStorage[i] == null){
				allFull = false;
			}else{
				if(outputStorage[i].stackSize < 64){
					allFull = false;
				}
			}
		}
		if(allFull){
			return false;
		}
		
		return true;
	}

	@Override
	public void onBlockBreaks() {
		if(isMultiblock){
			invalidateMultiblock();
		}
	}
	
	public void convertMultiblock(){
		//Build piston list.
		int x = xCoord;
		int y = yCoord + 3;
		int z = zCoord;
		int horiz = 0;
		
		//Check width:
		int width = 0;
		pistonList.clear();
		trolleyList.clear();
		while(getBlockId(x, y, z) == idPiston){
			
			x = xCoord + (dir == 2 ? -horiz : (dir == 0 ? horiz : 0));
			y = yCoord + 3;
			z = zCoord + (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
			
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if(tile instanceof TileHydraulicPiston){
				TileHydraulicPiston p = (TileHydraulicPiston)tile;
				p.setIsHarvesterPart(true);
				p.setMaxLength((float)harvesterLength-1);
				Location l = new Location(p.xCoord, p.yCoord, p.zCoord);
				pistonList.add(l);
			}
			horiz+=1;
			if(horiz > 10){
				break;
			}
		}
		
		horiz = 0;
		Location l = getLocationInHarvester(1, horiz);
		x = l.getX();
		z = l.getZ();
		y-=1;
		
		while(getBlockId(x, y, z) == idTrolley){
			l = getLocationInHarvester(1, horiz);
			x = l.getX();
			z = l.getZ();
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if(tile instanceof TileHarvesterTrolley){
				TileHarvesterTrolley t = (TileHarvesterTrolley)tile;

				
				Location l2 = new Location(t.xCoord, t.yCoord, t.zCoord);
				t.setDir(dir);
				trolleyList.add(l2);
			}
			horiz+=1;
			if(horiz > 10){
				break;
			}
		}
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	
	public void invalidateMultiblock(){
		Functions.showMessageInChat("Harvester invalidated!");
		dir = 0;
		isMultiblock = false;
		for(Location l : pistonList){
			TileHydraulicPiston p = getPistonFromCoords(l);
			if(p!=null){
				p.setIsHarvesterPart(false);
				p.extendTo(0F);
			}
		}
		pistonList.clear();
	}
	
	private int getBlockId(int x, int y, int z){
		return worldObj.getBlockId(x, y, z);
	}
	
	private int getBlockMetaFromCoord(int x, int y, int z){
		return worldObj.getBlockMetadata(x, y, z);
	}

	
	
	private boolean checkMultiblock(int dir){
		//Log.info("------------ Now checking "+dir + "-------------");
		//Go up, check for pistons etc
		if(getBlockId(xCoord, yCoord + 1, zCoord) != idVerticalFrame) return false;
		if(getBlockId(xCoord, yCoord + 2, zCoord) != idVerticalFrame) return false;
		if(getBlockId(xCoord, yCoord + 3, zCoord) != idPiston) return false;
		int x = xCoord;
		int y = yCoord + 3;
		int z = zCoord;
		int horiz = 0;
		
		//Check width:
		int width = 0;
		while(getBlockId(x, y, z) == idPiston){
			width+=1;
			horiz+=1;
			x = xCoord + (dir == 2 ? -horiz : (dir == 0 ? horiz : 0));
			y = yCoord + 3;
			z = zCoord + (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
		}
		
		if(width > 9){
			return false;
		}
		
		
		int f = 0;
		horiz = 1;
		int length = 0;
		int firstLength = 0;
		for(f = 0; f < width; f++){
			if(f == 1){
				firstLength = length;
			}
			horiz = 1;
			length = 0;
			int nsToAdd = (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
			int ewToAdd = (dir == 0 ? -horiz : (dir == 2 ? horiz : 0));
			int nsSide = (dir == 2 ? -f : (dir == 0 ? f : 0));
			int ewSide = (dir == 1 ? -f : (dir == 3 ? f : 0));
			x = xCoord + nsToAdd + nsSide;
			y = yCoord + 3;
			z = zCoord + ewToAdd + ewSide;
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + getBlockId(x, y, z) + " W: " + width);
			while(getBlockId(x, y, z) == idHorizontalFrame && getBlockMetaFromCoord(x, y, z) == 1){
				if(horiz == 1 && f == 0){
					//Check if there's a trolley right there!
					//Log.info("(" + dir + ": " + x + ", " + (y-1) + ", " + z + "; " + f + ") = " + getBlockId(x, y-1, z) + " W: " + width);
					if(getBlockId(x, y-1, z) != idTrolley){
						return false;
					}
				}
				//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + getBlockId(x, y, z) + " W: " + width);
				
				length += 1;
				horiz += 1;
				
				//Check if the frame is rotated:
				TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
				if(tile instanceof TileHarvesterFrame){
					TileHarvesterFrame fr = (TileHarvesterFrame) tile;
					//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + fr.getIsRotated());
					if(dir == 3 || dir == 1){
						if(fr.getIsRotated()){
							return false;
						}
					}else{
						if(!fr.getIsRotated()){
							return false;
						}
					}
				}else{
					return false;
				}
				
				nsToAdd = (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
				ewToAdd = (dir == 0 ? -horiz : (dir == 2 ? horiz : 0));
				x = xCoord + nsToAdd + nsSide;
				y = yCoord + 3;
				z = zCoord + ewToAdd + ewSide;
			}
			if(f > 0){
				if(firstLength != length){
					return false;
				}
			}
		}
		length = firstLength;
		
		if(length == 0){
			return false;
		}
		
		if(length < 4 && length > 0){
			return false;
		}
		if(length > 9){
			return false; //No more than 9!
		}
		
		x = xCoord + (dir == 1 ? -horiz : (dir == 3 ? horiz : 0));
		y = yCoord + 3;
		z = zCoord + (dir == 0 ? -horiz : (dir == 2 ? horiz : 0));
		
		if(getBlockId(x, y, z) == idEndBlock){
			length+=1;
		}else{
			return false;
		}
		
		
		//Check legs!
		horiz = 0;
		for(int vert = 0; vert <= 2; vert++){
			//Farmost left first.
			x = xCoord + (dir == 1 ? (-length) : (dir == 3 ? (length) : 0));
			y = yCoord + vert;
			z = zCoord + (dir == 0 ? (-length) : (dir == 2 ? (length) : 0));
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + ") = " + getBlockId(x, y, z) + " W: " + width);
			int blockId = getBlockId(x, y, z);
			if(x == xCoord && y == yCoord && z == zCoord){
				
			}else{
				if(blockId != idVerticalFrame){
					return false;
				}
				
			}
			
			x+= (dir == 2 ? (-width+1) : (dir == 0 ? (width-1) : 0));
			z+= (dir == 1 ? (-width+1) : (dir == 3 ? (width-1) : 0));
			
			
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + ") = " + getBlockId(x, y, z) + " W: " + width);
			blockId = getBlockId(x, y, z);
			if(x == xCoord && y == yCoord && z == zCoord){
				
			}else{
				if(blockId != idVerticalFrame){
					return false;
				}
				
			}
			
			//Base right
			x = xCoord + (dir == 2 ? (-width+1) : (dir == 0 ? (width-1) : 0));
			y = yCoord + vert;
			z = zCoord + (dir == 1 ? (-width+1) : (dir == 3 ? (width-1) : 0));
			
			//Log.info("(" + dir + ": " + x + ", " + y + ", " + z + ") = " + getBlockId(x, y, z) + " W: " + width);
			blockId = getBlockId(x, y, z);
			if(x == xCoord && y == yCoord && z == zCoord){
				
			}else{
				if(blockId != idVerticalFrame){
					return false;
				}
			}
		}
		
		harvesterLength = length;
		harvesterWidth = width;
		//Log.info("Found at dir " + dir);
		return true;
	}

	@Override
	public int getSizeInventory() {
		//9 input, 9 output.
		return 18;
	}
	@Override
    public ItemStack getStackInSlot(int i){
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        if(i < 9){
        	return seedsStorage[i];
        }else if(i >= 9){
        	return outputStorage[i-9];
        }else{
        	return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j){
        ItemStack inventory = getStackInSlot(i);
        
        if(inventory == null){
        	return null;
        }
        
        ItemStack ret = null;
        if(inventory.stackSize < j) {
            ret = inventory;
            inventory = null;
        } else {
            ret = inventory.splitStack(j);
            if(inventory.stackSize <= 0) {
            	if(i < 9){
            		seedsStorage[i] = null;
            	}else{
            		outputStorage[i-9] = null;
            	}
            }
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

        return ret;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i){
        ItemStack stack = getStackInSlot(i);
        if(stack != null) {
            setInventorySlotContents(i, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack){
    	if(i < 9){
    		seedsStorage[i] = itemStack;
    		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	}else{
    		outputStorage[i-9] = itemStack;
    		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	}
    }

    @Override
    public String getInvName(){
        // TODO Localization
        return Names.blockHydraulicHarvester[0].localized;
    }

    @Override
    public boolean isInvNameLocalized(){
        // TODO Localization
        return true;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player){
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord, yCoord, zCoord) < 64;
    }

    @Override
    public void openChest(){

    }

    @Override
    public void closeChest(){

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack){
        if(i < 9) {
            return itemStack.getItem() instanceof IPlantable;
        } else {
            return false;
        }
    }

    @Override
    public void onInventoryChanged(){
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int j) {
		if(i < 9){
			return isItemValidForSlot(i, itemStack);
		}else if(j == -1){
			if(getStackInSlot(i) != null){
				return getStackInSlot(i).isItemEqual(itemStack);
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int j) {
		if(i >= 9){
			return true;
		}
		return false;
	}
}
