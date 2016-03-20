package k4unl.minecraft.Hydraulicraft.tileEntities.harvester;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterCustomHarvestAction;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterCustomPlantAction;
import k4unl.minecraft.Hydraulicraft.api.IHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.trolleys.TrolleySugarCane;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHarvester;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TileHarvesterTrolley extends TileHydraulicBaseNoPower implements ITickable {

    private float extendedLength;
    private float oldExtendedLength;
    private final float maxLength    = 4F;
    private final float maxSide      = 8F;
    private       float extendTarget = 0F;
    private       float sideTarget   = 0F;
    private float sideLength;
    private float oldSideLength;
    private float      movingSpeedExtending    = 0.05F;
    private float      movingSpeedSideways     = 0.05F;
    private float      movingSpeedSidewaysBack = 0.1F;
    private EnumFacing facing                  = EnumFacing.NORTH;
    private boolean    harvesterPart           = false;

    private boolean isRetracting;
    private boolean isMovingUpDown;
    private boolean isMoving;
    private boolean isMovingSideways;
    private boolean isPlanting;
    private boolean isHarvesting;


    private ItemStack       plantingItem      = null;
    private List<ItemStack> harvestedItems    = new ArrayList<ItemStack>();//starting without being null so the renderer won't NPE.
    private int             locationToPlant   = -1;
    private int             locationYHarvest  = -1;
    private int             locationToHarvest = -1;
    private int harvesterIndex;
    private IHarvester harvester         = null;
    private Location   harvesterLocation = null;
    private IHarvesterTrolley trolley;

    public void setTrolley(IHarvesterTrolley trolley) {

        this.trolley = trolley;
    }

    public IHarvesterTrolley getTrolley() {

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

        markBlockForUpdate();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

        NBTTagCompound tagCompound = packet.getNbtCompound();
        readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {

        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new SPacketUpdateTileEntity(getPos(), 4, tagCompound);
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
                    ((IHarvesterCustomPlantAction) getTrolley()).doPlant(getWorld(), getLocation(locationToPlant, -2).toBlockPos(), plantingItem);
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
                List<ItemStack> dropped;
                if (getTrolley() instanceof IHarvesterCustomHarvestAction) {
                    dropped = ((IHarvesterCustomHarvestAction) getTrolley()).doHarvest(getWorld(), getLocation(locationToHarvest, locationYHarvest).toBlockPos());
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
            markBlockForUpdate();
        }
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void update() {

        if (getWorld().isRemote) {
            doMove();
        }
        recheckSpeed();
    }

    private void recheckSpeed() {

        if (getHarvester() != null) {
            TileHydraulicHarvester harv = (TileHydraulicHarvester) getHarvester();

            float pressurePercentage = harv.getPressure(EnumFacing.UP) / harv.getMaxPressure(harv.isOilStored(), EnumFacing.UP);
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
        facing = EnumFacing.byName(tagCompound.getString("facing"));
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
        tagCompound.setString("facing", facing.toString());
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

        float minX = 0.0F + getPos().getX();
        float minY = 0.0F + getPos().getY();
        float minZ = 0.0F + getPos().getX();
        float maxX = 1.0F + getPos().getY();
        float maxY = 1.0F + getPos().getY();
        float maxZ = 1.0F + getPos().getZ();

        minX += sidewaysMovement * getFacing().getFrontOffsetX();
        minY -= extendedLength;
        minZ += sidewaysMovement * getFacing().getFrontOffsetZ();

        maxX += sidewaysMovement * getFacing().getFrontOffsetZ();
        //maxY += extendedLength;
        maxZ += sidewaysMovement * getFacing().getFrontOffsetX();

        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void setFacing(EnumFacing nFacing) {

        facing = nFacing;
    }

    public EnumFacing getFacing() {

        return facing;
    }

    public void setIsHarvesterPart(boolean isit) {

        harvesterPart = isit;
        markBlockForUpdate();
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

        return new Location(getPos().getX() + getFacing().getFrontOffsetX() * length, getPos().getY() + y, getPos().getZ() + getFacing().getFrontOffsetZ() * length);
    }

    private Block getBlock(Location l) {

        return worldObj.getBlockState(l.toBlockPos()).getBlock();
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
            if (getTrolley().canPlant(getWorld(), l.toBlockPos(), firstSeed)) {
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
        if (getTrolley() instanceof TrolleySugarCane) {
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
        IBlockState plant = getTrolley().getBlockStateForSeed(plantingItem);

        worldObj.setBlockState(l.toBlockPos(), plant);
    }

    private List<ItemStack> getDroppedItems(int h) {

        Location cropLocation = getLocation(h, -3);
        cropLocation.addY(getTrolley().getPlantHeight(getWorld(), cropLocation.toBlockPos()));
        IBlockState toHarvest = worldObj.getBlockState(cropLocation.toBlockPos());
        if (toHarvest != null) {
            return toHarvest.getBlock().getDrops(worldObj, cropLocation.toBlockPos(), toHarvest, 0);
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


    public List<ItemStack> checkHarvest(int maxLen) {

        for (int horiz = 0; horiz <= maxLen; horiz++) {
            Location l = getLocation(horiz, -3);
            int plantHeight = getTrolley().getPlantHeight(getWorld(), l.toBlockPos());
            for (int i = plantHeight; i >= 1; i--) {
                l = getLocation(horiz, -3 + i);
                if (getTrolley().canHarvest(getWorld(), l.toBlockPos())) {
                    List<ItemStack> dropped = getDroppedItems(horiz);
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

    private IHarvester getHarvester() {

        if (harvester == null && harvesterLocation != null) {
            harvester = (IHarvester) harvesterLocation.getTE(getWorld());
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

    private List<ItemStack> actuallyHarvest() {

        List<ItemStack> dropped = getDroppedItems(locationToHarvest);
        Location cropLocation;
        cropLocation = getLocation(locationToHarvest, locationYHarvest);
        worldObj.destroyBlock(cropLocation.toBlockPos(), false);

        return dropped;
    }


    public void onBlockBreaks() {

        ItemStack ourEnt = new ItemStack(HCBlocks.harvesterTrolley, 1);
        NBTTagCompound tCompound = new NBTTagCompound();

        tCompound.setString("name", getTrolley().getName());

        ourEnt.setTagCompound(tCompound);
        EntityItem ei = new EntityItem(getWorld());
        ei.setEntityItemStack(ourEnt);
        ei.setPosition(getPos().getX(), getPos().getY(), getPos().getZ());
        getWorld().spawnEntityInWorld(ei);
    }
}
