package k4unl.minecraft.Hydraulicraft.tileEntities.harvester;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterCustomHarvestAction;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterCustomPlantAction;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities.TrolleyEnderlily;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleyCrops;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleySugarCane;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHarvester;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileHarvesterTrolley extends TileEntity {
    private float extendedLength;
    private float oldExtendedLength;
    private final float maxLength    = 4F;
    private final float maxSide      = 8F;
    private       float extendTarget = 0F;
    private       float sideTarget   = 0F;
    private float sideLength;
    private float oldSideLength;
    private float movingSpeedExtending = 0.05F;
    private float movingSpeedSideways = 0.05F;
    private float movingSpeedSidewaysBack = 0.1F;
    private ForgeDirection facing = ForgeDirection.NORTH;
    private boolean harvesterPart = false;

    private boolean isRetracting;
    private boolean isMovingUpDown;
    private boolean isMoving;
    private boolean isMovingSideways;
    private boolean isPlanting;
    private boolean isHarvesting;


    private ItemStack plantingItem = null;
    private ArrayList<ItemStack> harvestedItems = new ArrayList<ItemStack>();//starting without being null so the renderer won't NPE.
    private int locationToPlant = -1;
    private int locationYHarvest = -1;
    private int locationToHarvest = -1;
    private int harvesterIndex;
    private IHarvester harvester         = null;
    private Location   harvesterLocation = null;
    private IHarvesterTrolley trolley;

    public void setTrolley(IHarvesterTrolley trolley) {
        this.trolley = trolley;
    }

    public IHarvesterTrolley getTrolley() {
        if (trolley == null) {//if trolley is null here the NBT didn't contain the new tag, and this is a legacy trolley.
            switch (getBlockMetadata()) {
                case 0:
                    trolley = new TrolleyCrops();
                    break;
                case 2:
                    trolley = new TrolleySugarCane();
                    break;
                case 1:
                    trolley = new TrolleyEnderlily();
                    break;
            }
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 2);
        }
        return trolley;
    }

    public void extendTo(float blocksToExtend, float sideExtend) {

        if (blocksToExtend > maxLength) {
            blocksToExtend = maxLength;
        }
        if (blocksToExtend < 0) {
            blocksToExtend = 0;
        }

        if (sideExtend < 0) {
            sideExtend = 0;
        }

        extendTarget = blocksToExtend;
        sideTarget = sideExtend;


        int compResult = Float.compare(extendTarget, extendedLength);
        if (compResult > 0) {
            isRetracting = false;
        } else if (compResult < 0) {
            isRetracting = true;
        }

        compResult = Float.compare(sideTarget, sideLength);
        if (compResult > 0) {
            isMovingSideways = false;
        } else if (compResult < 0) {
            isMovingSideways = true;
        }

        float blocksToMoveSideways = Math.abs(sideTarget - sideLength);
        float blocksToExtendDown = Math.abs(extendTarget - extendedLength);

        float movingSpeedPercentage;
        if (isRetracting) {
            movingSpeedPercentage = movingSpeedSidewaysBack / blocksToMoveSideways;
        } else {
            movingSpeedPercentage = movingSpeedSideways / blocksToMoveSideways;
        }
        movingSpeedExtending = movingSpeedPercentage * blocksToExtendDown;

        if (movingSpeedExtending > 500F) { //Which is just absurd..
            if (isRetracting) {
                movingSpeedExtending = movingSpeedSidewaysBack;
            } else {
                movingSpeedExtending = movingSpeedSideways;
            }
        }
        if (harvester != null) {
            harvester.extendPistonTo(harvesterIndex, sideExtend);
        }

        isMoving = true;
        isMovingUpDown = true;

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        NBTTagCompound tagCompound = packet.func_148857_g();
        readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 4, tagCompound);
    }

    public void doMove() {
        oldExtendedLength = extendedLength;
        oldSideLength = sideLength;

        int compResult = Float.compare(extendTarget, extendedLength);
        if (compResult > 0 && !isRetracting) {
            extendedLength += movingSpeedExtending;
        } else if (compResult < 0 && isRetracting) {
            extendedLength -= movingSpeedExtending;
        } else if (isMovingUpDown) {
            extendedLength = extendTarget;
            isMovingUpDown = false;
        }

        compResult = Float.compare(sideTarget, sideLength);
        if (compResult > 0 && !isMovingSideways) {
            sideLength += movingSpeedSideways;
        } else if (compResult < 0 && isMovingSideways) {
            sideLength -= movingSpeedSidewaysBack;
        } else if (isMoving) {
            //Arrived at location!
            sideLength = sideTarget;
            isMoving = false;
        }
        if (!worldObj.isRemote && harvester != null && !isMoving && !isMovingUpDown) {
            if (isPlanting) {
                if (getTrolley() instanceof IHarvesterCustomPlantAction) {
                    Location location = getLocation(locationToPlant, -2);

                    ((IHarvesterCustomPlantAction) getTrolley()).doPlant(getWorldObj(), location.getX(), location.getY(), location.getZ(), plantingItem);
                } else {
                    actuallyPlant();
                }
                plantingItem = null;
                isHarvesting = false;
                isPlanting = false;
                if (HCConfig.INSTANCE.getBool("shouldDolleyInHarvesterGoBack")) {
                    extendTo(0, 0);
                } else {
                    extendTo(0, locationToPlant);
                }
            } else if (isHarvesting) {
                ArrayList<ItemStack> dropped;
                if (getTrolley() instanceof IHarvesterCustomHarvestAction) {
                    Location l = getLocation(locationToHarvest, locationYHarvest);
                    dropped = ((IHarvesterCustomHarvestAction)getTrolley()).doHarvest(getWorldObj(), l.getX(), l.getY(), l.getZ());
                } else {
                    dropped = actuallyHarvest();
                }
                isHarvesting = false;
                isPlanting = false;
                if (HCConfig.INSTANCE.getBool("shouldDolleyInHarvesterGoBack")) {
                    harvestedItems = dropped;
                    extendTo(0, 0);
                } else {
                    harvester.putInInventory(dropped);
                    extendTo(0, locationToHarvest);
                }
            } else if (HCConfig.INSTANCE.getBool("shouldDolleyInHarvesterGoBack") && harvestedItems != null) {
                if (harvestedItems.size() > 0) {
                    harvester.putInInventory(harvestedItems);
                }
                harvestedItems = new ArrayList<ItemStack>();
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void updateEntity() {
        if (getWorldObj().isRemote) {
            doMove();
        }
        recheckSpeed();
    }

    private void recheckSpeed() {
        if (getHarvester() != null) {
            TileHydraulicHarvester harv = (TileHydraulicHarvester) getHarvester();

            float pressurePercentage = harv.getPressure(ForgeDirection.UP) / harv.getMaxPressure(harv.isOilStored(), ForgeDirection.UP);
            float factor = (harv.getPressureTier().ordinal() + 1) * (harv.isOilStored() ? 3.0F : 1.5F) * pressurePercentage;
            movingSpeedSideways = 0.05F * factor;
            movingSpeedSidewaysBack = 0.1F * factor;
            movingSpeedExtending = 0.05F * factor;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        extendedLength = tagCompound.getFloat("extendedLength");
        extendTarget = tagCompound.getFloat("extendTarget");
        sideTarget = tagCompound.getFloat("sideTarget");
        isRetracting = tagCompound.getBoolean("isRetracting");
        isMovingSideways = tagCompound.getBoolean("isMoving");
        sideLength = tagCompound.getFloat("sideLength");

        movingSpeedExtending = tagCompound.getFloat("movingSpeedExtending");
        facing = ForgeDirection.getOrientation(tagCompound.getInteger("facing"));
        harvesterPart = tagCompound.getBoolean("harvesterPart");

        movingSpeedSideways = tagCompound.getFloat("movingSpeedSideways");
        movingSpeedSidewaysBack = tagCompound.getFloat("movingSpeedSidewaysBack");

        harvesterLocation = new Location(tagCompound.getIntArray("harvesterLocation"));

        harvestedItems.clear();
        NBTTagList tagList = tagCompound.getTagList("HarvestedItems", 9);
        for (int i = 0; i < tagList.tagCount(); i++) {
            harvestedItems.add(ItemStack.loadItemStackFromNBT(tagList.getCompoundTagAt(i)));
        }
        NBTTagCompound plantingTag = tagCompound.getCompoundTag("PlantingItem");
        if (plantingTag != null) {
            plantingItem = ItemStack.loadItemStackFromNBT(plantingTag);
        } else {
            plantingItem = null;
        }
        trolley = Hydraulicraft.trolleyRegistrar.getTrolley(tagCompound.getString("trolley"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setFloat("extendedLength", extendedLength);
        tagCompound.setFloat("extendTarget", extendTarget);
        tagCompound.setBoolean("isRetracting", isRetracting);
        tagCompound.setBoolean("isMoving", isMovingSideways);
        tagCompound.setFloat("sideLength", sideLength);
        tagCompound.setFloat("sideTarget", sideTarget);
        tagCompound.setInteger("facing", facing.ordinal());
        tagCompound.setFloat("movingSpeedExtending", movingSpeedExtending);
        tagCompound.setBoolean("harvesterPart", harvesterPart);
        tagCompound.setFloat("movingSpeedSideways", movingSpeedSideways);
        tagCompound.setFloat("movingSpeedSidewaysBack", movingSpeedSidewaysBack);
        if (harvesterLocation != null) {
            tagCompound.setIntArray("harvesterLocation", harvesterLocation.getIntArray());
        }

        NBTTagList tagList = new NBTTagList();
        for (ItemStack harvestedItem : harvestedItems) {
            NBTTagCompound itemTag = new NBTTagCompound();
            harvestedItem.writeToNBT(itemTag);
            tagList.appendTag(itemTag);
        }
        tagCompound.setTag("HarvestedItems", tagList);

        if (plantingItem != null) {
            NBTTagCompound tag = new NBTTagCompound();
            plantingItem.writeToNBT(tag);
            tagCompound.setTag("PlantingItem", tag);
        }
        tagCompound.setString("trolley", getTrolley().getName());
    }


    public float getSideLength() {
        return sideLength;
    }

    public float getExtendedLength() {
        return extendedLength;
    }

    public float getOldSideLength() {
        return oldSideLength;
    }

    public float getOldExtendedLength() {
        return oldExtendedLength;
    }


    public float getMaxLength() {
        return maxLength;
    }

    public float getExtendTarget() {
        return extendTarget;
    }

    public float getSideTarget() {
        return sideTarget;
    }


    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        float extendedLength = getExtendedLength();
        float sidewaysMovement = getSideLength();

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

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void setFacing(ForgeDirection nFacing) {
        facing = nFacing;
    }

    public ForgeDirection getFacing() {
        return facing;
    }

    public void setIsHarvesterPart(boolean isit) {
        harvesterPart = isit;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }


    public boolean canHandleSeed(ItemStack seed) {
        ArrayList<ItemStack> seedsToHandle = getTrolley().getHandlingSeeds();
        for (ItemStack s : seedsToHandle) {
            if (s.isItemEqual(seed)) {
                return true;
            }
        }
        return false;
    }


    public Location getLocation(int length, int y) {
        return new Location(xCoord + getFacing().offsetX * length, yCoord + y, zCoord + getFacing().offsetZ * length);
    }

    private Block getBlock(Location l) {
        return worldObj.getBlock(l.getX(), l.getY(), l.getZ());
    }

    public int canPlantSeed(ItemStack[] seeds, int maxLength) {
        //Todo: Change me
        ItemStack firstSeed = null;
        int seedLocation = 0;
        for (int i = 0; i < 9; i++) {
            if (seeds[i] != null) {
                if (canHandleSeed(seeds[i])) {
                    seedLocation = i;
                    firstSeed = seeds[i];
                    break;
                }
            }
        }
        if (firstSeed == null) {
            return -1;
        }

        for (int horiz = 0; horiz <= maxLength; horiz++) {
            Block block = getBlock(getLocation(horiz, -2));
            //Block soil = getBlock(getLocation(horiz, -3));
            boolean canIPlantHere = false;

            Location l = getLocation(horiz, -2);
            if (getTrolley().canPlant(getWorldObj(), l.getX(), l.getY(), l.getZ(), firstSeed)) {
                canIPlantHere = true;
            }

            if (block.equals(Blocks.air) && canIPlantHere) {
                locationToPlant = horiz;
                return seedLocation;
            }
        }

        return -1;
    }

    public void doPlant(ItemStack seed) {
        plantingItem = seed;
        if (getBlockMetadata() == 2) {//FIXME 2 == sugar cane id, needs fixing
            extendTo(2F, locationToPlant);
        } else {
            extendTo(2.7F, locationToPlant);
        }

        isPlanting = true;
        isHarvesting = false; //Just to be safe
    }

    private void actuallyPlant() {
        //The trolley has arrived at the location and should plant.
        Location l = getLocation(locationToPlant, -2);
        Block plant = getTrolley().getBlockForSeed(plantingItem);

        worldObj.setBlock(l.getX(), l.getY(), l.getZ(), plant);
    }

    private ArrayList<ItemStack> getDroppedItems(int h) {
        Location cropLocation = getLocation(h, -3);
        cropLocation.addY(getTrolley().getPlantHeight(getWorldObj(), cropLocation.getX(), cropLocation.getY(), cropLocation.getZ()));
        Block toHarvest = worldObj.getBlock(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
        int metaData = worldObj.getBlockMetadata(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ());
        if (toHarvest != null) {
            return toHarvest.getDrops(worldObj, cropLocation.getX(), cropLocation.getY(), cropLocation.getZ(), metaData, 0);
        } else {
            return null;
        }
    }

    public List<ItemStack> getRenderedItems() {
        if (plantingItem != null) {
            return Collections.singletonList(plantingItem);
        } else {
            return harvestedItems;
        }
    }


    public ArrayList<ItemStack> checkHarvest(int maxLen) {
        for (int horiz = 0; horiz <= maxLen; horiz++) {
            Location l = getLocation(horiz, -3);
            int plantHeight = getTrolley().getPlantHeight(getWorldObj(), l.getX(), l.getY(), l.getZ());
            for (int i = plantHeight; i >= 1; i--) {
                l = getLocation(horiz, -3 + i);
                if (getTrolley().canHarvest(getWorldObj(), l.getX(), l.getY(), l.getZ())) {
                    ArrayList<ItemStack> dropped = getDroppedItems(horiz);
                    locationToHarvest = horiz;
                    locationYHarvest = -3 + i;
                    return dropped;
                }
            }

        }
        return null;
    }

    public void setHarvester(IHarvester nHarvester, int harvesterIndex) {
        harvesterLocation = ((TileHydraulicHarvester) nHarvester).getBlockLocation();
        harvester = nHarvester;
        this.harvesterIndex = harvesterIndex;
    }

    private IHarvester getHarvester(){
        if(harvester == null && harvesterLocation != null){
            harvester = (IHarvester)harvesterLocation.getTE(getWorldObj());
        }
        return harvester;
    }


    public boolean isMoving() {
        return isMovingSideways;
    }


    public boolean isWorking() {
        return isPlanting || isHarvesting || isMoving || isMovingUpDown;
    }


    public void doHarvest() {
        plantingItem = null;
        extendTo(0 - locationYHarvest, locationToHarvest);
        isPlanting = false; //Just to be safe
        isHarvesting = true;
    }

    private ArrayList<ItemStack> actuallyHarvest() {
        ArrayList<ItemStack> dropped = getDroppedItems(locationToHarvest);
        Location cropLocation;
        cropLocation = getLocation(locationToHarvest, locationYHarvest);
        worldObj.func_147480_a(cropLocation.getX(), cropLocation.getY(), cropLocation.getZ(), false);

        return dropped;
    }


    public void onBlockBreaks() {
        ItemStack ourEnt = new ItemStack(HCBlocks.harvesterTrolley, 1);
        NBTTagCompound tCompound = new NBTTagCompound();

        tCompound.setString("name", getTrolley().getName());

        ourEnt.setTagCompound(tCompound);
        EntityItem ei = new EntityItem(getWorldObj());
        ei.setEntityItemStack(ourEnt);
        ei.setPosition(xCoord, yCoord, zCoord);
        getWorldObj().spawnEntityInWorld(ei);
    }
}
