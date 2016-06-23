package k4unl.minecraft.Hydraulicraft.tileEntities.harvester;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHarvester;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class TileHydraulicHarvester extends TileHydraulicBase implements IHydraulicConsumer, ISidedInventory, IHarvester {

    private ItemStack[] seedsStorage;
    private ItemStack[] outputStorage;

    private boolean isMultiblock;
    private int     harvesterLength;
    private int     harvesterWidth;
    private EnumFacing facing   = EnumFacing.NORTH;
    private boolean    firstRun = true;

    private boolean isPlanting   = false;
    private boolean isHarvesting = false;

    private int pistonMoving = -1;

    private List<Location> pistonList  = new ArrayList<Location>();
    private List<Location> trolleyList = new ArrayList<Location>();


    private static final Block horizontalFrame = HCBlocks.hydraulicHarvesterFrame;
    private static final Block verticalFrame   = Blocks.OAK_FENCE; //TODO: REWRITE TO MAKE SURE ALL FENCES WORK!
    private static final Block piston          = HCBlocks.hydraulicPiston;
    private static final Block endBlock        = HCBlocks.hydraulicPressureWall;

    private HarvesterReasonForNotForming error = HarvesterReasonForNotForming.OTHER;
    private String extraErrorInfo;

    public TileHydraulicHarvester() {

        super(16);
        super.init(this);
        seedsStorage = new ItemStack[9];
        outputStorage = new ItemStack[9];
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        isMultiblock = tagCompound.getBoolean("isMultiblock");
        harvesterLength = tagCompound.getInteger("harvesterLength");
        harvesterWidth = tagCompound.getInteger("harvesterWidth");
        facing = EnumFacing.byName(tagCompound.getString("facing"));
        readPistonListFromNBT(tagCompound);
        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tagCompound.getCompoundTag("seedsStorage" + i);
            seedsStorage[i] = ItemStack.loadItemStackFromNBT(tc);

            tc = tagCompound.getCompoundTag("outputStorage" + i);
            outputStorage[i] = ItemStack.loadItemStackFromNBT(tc);
        }

        isHarvesting = tagCompound.getBoolean("isHarvesting");
        isPlanting = tagCompound.getBoolean("isPlanting");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("isMultiblock", isMultiblock);
        tagCompound.setInteger("harvesterLength", harvesterLength);
        tagCompound.setInteger("harvesterWidth", harvesterWidth);
        tagCompound.setString("facing", facing.toString());
        writePistonListToNBT(tagCompound);

        for (int i = 0; i < 9; i++) {
            if (seedsStorage[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                seedsStorage[i].writeToNBT(tc);
                tagCompound.setTag("seedsStorage" + i, tc);
            }
            if (outputStorage[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                outputStorage[i].writeToNBT(tc);
                tagCompound.setTag("outputStorage" + i, tc);
            }
        }

        tagCompound.setBoolean("isHarvesting", isHarvesting);
        tagCompound.setBoolean("isPlanting", isPlanting);

        return tagCompound;
    }

    public void writePistonListToNBT(NBTTagCompound tagCompound) {

        NBTTagCompound tagList = new NBTTagCompound();

        tagList.setInteger("length", pistonList.size());
        int index = 0;
        for (Location l : pistonList) {
            tagList.setIntArray(index + "", l.getLocation());
            index++;
        }
        tagCompound.setTag("pistonList", tagList);
    }

    public void readPistonListFromNBT(NBTTagCompound tagCompound) {

        NBTTagCompound tagList = tagCompound.getCompoundTag("pistonList");
        pistonList.clear();
        if (tagList != null) {
            int length = tagList.getInteger("length");
            for (int i = 0; i < length; i++) {
                Location l = new Location(tagList.getIntArray(i + ""));
                pistonList.add(l);
            }
        }
    }

    @Override
    public void update() {

        super.update();
        //Every half second.. Or it should be..
        if (!worldObj.isRemote) {
            if (worldObj.getTotalWorldTime() % 60 == 0) {
                if (firstRun) {
                    firstRun = false;
                    if (isMultiblock) {
                        convertMultiblock();
                        pNetwork = null;
                        getHandler().updateNetworkOnNextTick(0);
                        getHandler().updateBlock();
                    }
                }
                if (!isMultiblock) {
                    doMultiBlockChecking();
                } else {
                    if (!checkMultiblock(facing)) {
                        //Multiblock no longer valid!
                        isMultiblock = false;
                        invalidateMultiblock();
                    }
                }
            }
        }
    }

    public void doMultiBlockChecking() {

        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir.equals(EnumFacing.UP) || dir.equals(EnumFacing.DOWN)) continue;
            if (checkMultiblock(dir)) {
                this.facing = dir;
                isMultiblock = true;

                convertMultiblock();
                pNetwork = null;
                getHandler().updateNetworkOnNextTick(0);
                break;
            }
        }
    }

    private TileHydraulicPiston getPistonFromList(int index) {

        Location v = pistonList.get(index);
        return (TileHydraulicPiston) worldObj.getTileEntity(v.toBlockPos());
    }

    private TileHydraulicPiston getPistonFromCoords(Location v) {

        return (TileHydraulicPiston) worldObj.getTileEntity(v.toBlockPos());
    }

    private TileHarvesterTrolley getTrolleyFromList(int index) {

        if (index < trolleyList.size()) {
            Location v = trolleyList.get(index);
            return (TileHarvesterTrolley) worldObj.getTileEntity(v.toBlockPos());
        } else {
            return null;
        }
    }

    private TileHarvesterTrolley getTrolleyFromCoords(Location v) {

        return (TileHarvesterTrolley) worldObj.getTileEntity(v.toBlockPos());
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {

        if (canRun()) {
            if (pistonMoving != -1) {
                if (pistonMoving <= trolleyList.size()) {
                    TileHarvesterTrolley t = getTrolleyFromList(pistonMoving);
                    TileHydraulicPiston p = getPistonFromList(pistonMoving);
                    if (p == null || t == null) {
                        invalidateMultiblock();
                        return 10F;
                    }
                    if (!simulate) {
                        if (t.isWorking()) {
                            updateTrolleys();
                        } else {
                            isHarvesting = false;
                            isPlanting = false;
                            pistonMoving = -1;
                        }
                    }
                    return 0.1F + p.workFunction(simulate, EnumFacing.UP) * 2;
                } else {
                    Log.error("PistonMoving (" + pistonMoving + ") > " + (trolleyList.size() - 1));
                }
            } else if (worldObj.getTotalWorldTime() % 30 == 0) {
                checkHarvest();
                if (!isHarvesting) {
                    checkPlantable();
                }
                return 0.1F;
            }
        } else {
            return 0F;
        }
        return 0F;
    }

    private void updateTrolleys() {

        for (Location l : trolleyList) {
            TileHarvesterTrolley t = getTrolleyFromCoords(l);
            if (t != null) {
                t.doMove();
            } else {
                invalidateMultiblock();
                return;
            }
        }
    }


    private boolean isPlaceForItems(ItemStack itemStack) {
        //First of all:

        if (itemStack.getItem() instanceof ItemSeeds || itemStack.getItem() == Items.REEDS) {
            //Check all the locations!
            for (ItemStack st : seedsStorage) {
                if (itemStack.stackSize > 0) {
                    if (st != null) {
                        if (st.isItemEqual(itemStack)) {
                            if (st.stackSize < 64) {
                                if (st.stackSize + itemStack.stackSize <= 64) {
                                    return true;
                                } else {
                                    itemStack.stackSize = 64 - st.stackSize;
                                }
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        for (ItemStack st : outputStorage) {
            if (itemStack.stackSize > 0) {
                if (st != null) {
                    if (st.isItemEqual(itemStack)) {
                        if (st.stackSize < 64) {
                            if (st.stackSize + itemStack.stackSize <= 64) {
                                return true;
                            } else {
                                itemStack.stackSize = 64 - st.stackSize;
                            }
                        }
                    }
                } else {
                    return true;
                }
            }
        }

        return itemStack.stackSize <= 0;
    }

    private Location getLocationInHarvester(int h, int w, int y) {

        Location l = _getLocationInHarvester(h, w, facing);
        l.setY(l.getY() + y);

        return l;
    }

    private Location getLocationInHarvester(int h, int w, int y, EnumFacing dir) {

        Location l = _getLocationInHarvester(h, w, dir);
        l.setY(l.getY() + y);

        return l;
    }

    private Location _getLocationInHarvester(int h, int w, EnumFacing dir) {

        int nsToAdd = dir.getFrontOffsetZ() * h;
        int ewToAdd = dir.getFrontOffsetX() * h;
        int nsSide = dir.getFrontOffsetX() * w;
        int ewSide = dir.getFrontOffsetZ() * w;
        int x = getPos().getX() + ewToAdd - ewSide;
        int y = getPos().getY();
        int z = getPos().getZ() + nsToAdd + nsSide;

        return new Location(x, y, z);
    }

    public void putInInventory(List<ItemStack> toPut) {

        for (ItemStack itemStack : toPut) {
            for (int i = 0; i < getSizeInventory(); i++) {
                if (itemStack.stackSize > 0) {
                    if (canInsertItem(i, itemStack, EnumFacing.UP)) {
                        ItemStack sis = getStackInSlot(i);
                        if (sis == null) {
                            sis = itemStack.copy();
                            itemStack.stackSize = 0;
                            setInventorySlotContents(i, sis);
                            break;
                        }
                        if ((sis.stackSize + itemStack.stackSize) <= 64) {
                            sis.stackSize += itemStack.stackSize;
                            itemStack.stackSize = 0;
                        } else {
                            itemStack.stackSize = itemStack.stackSize % 64;
                            sis.stackSize = 64;
                        }
                        setInventorySlotContents(i, sis);
                    }
                }
            }
        }
    }

    private boolean canRun() {

        return isMultiblock && Float.compare(getPressure(EnumFacing.UP), Constants.MIN_REQUIRED_PRESSURE_HARVESTER) >= 0 && pistonList.size() != 0;
    }


    @Override
    public void onBlockBreaks() {

        if (isMultiblock) {
            invalidateMultiblock();
        }
        //Drop seeds.. duh
        for (int i = 0; i < seedsStorage.length; i++) {
            dropItemStackInWorld(seedsStorage[i]);
            dropItemStackInWorld(outputStorage[i]);
        }
    }

    public void convertMultiblock() {
        //Build piston list.
        Location l = new Location(getPos(), EnumFacing.UP, 3);
        Location l2;
        int horiz = 0;

        //Check width:
        pistonList.clear();
        trolleyList.clear();
        while (getBlockState(l).getBlock().equals(piston)) {
            l = getLocationInHarvester(0, horiz, 3);
            l2 = getLocationInHarvester(1, horiz, 2);

            TileEntity tilePiston = getTileEntity(l);
            if (tilePiston instanceof TileHydraulicPiston) {
                TileHydraulicPiston p = (TileHydraulicPiston) tilePiston;
                p.setIsHarvesterPart(true);
                p.setHarvester(this);
                p.setMaxLength((float) harvesterLength - 1);
                p.setFacing(getFacing());
                //Location l = new Location(p.getPos().getX(), p.getPos().getY(), p.getPos().getZ());
                pistonList.add(l);


                TileEntity tile = getTileEntity(l2);
                if (tile instanceof TileHarvesterTrolley) {
                    TileHarvesterTrolley t = (TileHarvesterTrolley) tile;
                    t.setFacing(facing);
                    t.setIsHarvesterPart(true);
                    t.setHarvester(this, horiz);
                    trolleyList.add(l2);
                }
            }
            horiz += 1;
            if (horiz > 10) {
                break;
            }
        }

        markBlockForUpdate();
    }


    public void invalidateMultiblock() {
        //Functions.showMessageInChat("Harvester invalidated!");
        facing = EnumFacing.NORTH;
        isMultiblock = false;
        for (Location l : pistonList) {
            TileHydraulicPiston p = getPistonFromCoords(l);
            if (p != null) {
                p.setIsHarvesterPart(false);
                p.extendTo(0F);
            }
        }
        for (Location l : trolleyList) {
            TileHarvesterTrolley t = getTrolleyFromCoords(l);
            if (t != null) {
                t.setIsHarvesterPart(false);
            }
        }
        pistonList.clear();
        trolleyList.clear();
        getHandler().updateBlock();
    }

    private IBlockState getBlockState(Location l) {

        return l.getBlockState(getWorldObj());
    }

    private TileEntity getTileEntity(Location l) {

        return l.getTE(getWorldObj());
    }

    private Block getBlock(BlockPos pos) {

        return getWorldObj().getBlockState(pos).getBlock();
    }

    private boolean checkMultiblock(EnumFacing dir) {
        //Log.info("------------ Now checking "+ dir + "-------------");
        //Go up, check for pistons etc
        if (!getBlock(getPos().up(1)).equals(verticalFrame)) {
            error = HarvesterReasonForNotForming.VERTICAL_FRAME_EXPECTED;
            extraErrorInfo = getPos().getX() + "," + (getPos().getY() + 1) + "," + getPos().getZ();
            return false;
        }
        if (!getBlock(getPos().up(2)).equals(verticalFrame)) {
            error = HarvesterReasonForNotForming.VERTICAL_FRAME_EXPECTED;
            extraErrorInfo = getPos().getX() + "," + (getPos().getY() + 2) + "," + getPos().getZ();
            return false;
        }
        if (!getBlock(getPos().up(3)).equals(piston)) {
            error = HarvesterReasonForNotForming.PISTON_EXPECTED;
            extraErrorInfo = getPos().getX() + "," + (getPos().getY() + 3) + "," + getPos().getZ();
            return false;
        }
        Location l = new Location(getPos().getX(), getPos().getY() + 3, getPos().getZ());

        int horiz = 0;

        //Check width:
        int width = 0;
        while (getBlockState(l).getBlock().equals(piston) && width < 11) {
            width += 1;
            horiz += 1;
            l = getLocationInHarvester(0, horiz, 3, dir);
        }

        if (width > 9) {
            error = HarvesterReasonForNotForming.TOO_WIDE;
            extraErrorInfo = width + "";
            return false;
        } else if (width == 1) {
            //error = HarvesterReasonForNotForming.TOO_SMALL;
            return false;
        }
        //Log.info(dir + " Width= " + width);


        int f;
        int length = 0;
        int firstLength = 0;
        for (f = 0; f < width; f++) {
            if (f == 1) {
                firstLength = length;
            }
            horiz = 1;
            length = 0;
            l = getLocationInHarvester(horiz, f, 3, dir);
            //Log.info("(" + dir + ": " + l.printCoords() + "; " + f + ") = " + getBlockId(l) + " W: " + width + " F");
            //TODO: Rewrite me so that the length will be better checked the second time
            //TODO: Implement a check that will check if the length of the first is the same as the second.
            while (getBlockState(l).getBlock().equals(horizontalFrame)) {
                if (horiz == 1) {
                    //Check if there's a trolley right there!
                    Location trolleyLocation = getLocationInHarvester(horiz, f, 2, dir);
                    //Log.info("(" + dir + ": " + trolleyLocation.printCoords() + "; " + f + ") = " + getBlockId(trolleyLocation) + " W: " + width + " T");

                    if (!(getTileEntity(trolleyLocation) instanceof TileHarvesterTrolley)) {
                        error = HarvesterReasonForNotForming.TROLLEY_EXPECTED;
                        extraErrorInfo = trolleyLocation.printCoords();
                        return false;
                    }
                }

                //Check if the frame is rotated:
                IBlockState frame = getBlockState(l);
                if (frame.getBlock() == HCBlocks.hydraulicHarvesterFrame) {
                    //Log.info("(" + dir + ": " + x + ", " + y + ", " + z + "; " + f + ") = " + fr.getIsRotated());
                    if (dir.equals(EnumFacing.EAST) || dir.equals(EnumFacing.WEST)) {
                        if (!frame.getValue(Properties.HARVESTER_FRAME_ROTATED)) {
                            error = HarvesterReasonForNotForming.FRAME_ROTATION_WRONG;
                            extraErrorInfo = l.printCoords();
                            return false;
                        }
                    } else {
                        if (frame.getValue(Properties.HARVESTER_FRAME_ROTATED)) {
                            error = HarvesterReasonForNotForming.FRAME_ROTATION_WRONG;
                            extraErrorInfo = l.printCoords();
                            return false;
                        }
                    }
                } else {
                    error = HarvesterReasonForNotForming.FRAME_EXPECTED;
                    extraErrorInfo = l.printCoords();
                    return false;
                }
                //Log.info("(" + dir + ": " + l.printCoords() + "; " + f + ") = " + getBlockId(l) + " W: " + width);
                length += 1;
                horiz += 1;

                l = getLocationInHarvester(horiz, f, 3, dir);
            }
            //So.. This should actually be the endBlock!
            //Log.info("(" + dir + ": " + l.printCoords() + "; " + f + ") = " + getBlock(l).getUnlocalizedName() + " W: " + width);

            if (f > 0 && firstLength != length) {
                error = HarvesterReasonForNotForming.FRAME_EXPECTED;
                return false;
            }

            if (!getBlockState(l).getBlock().equals(endBlock)) {
                error = HarvesterReasonForNotForming.END_BLOCK_EXPECTED;
                extraErrorInfo = l.printCoords();
                return false;
            }


        }
        length = firstLength;

        if (length < 4) {
            error = HarvesterReasonForNotForming.TOO_SHORT;
            extraErrorInfo = length + "";
            return false;
        } else if (length > 9) {
            error = HarvesterReasonForNotForming.TOO_LONG;
            extraErrorInfo = length + "";
            return false;
        }


        //Check legs!
        for (int vert = 0; vert <= 2; vert++) {
            //Farmost left first.
            l = getLocationInHarvester(length + 1, 0, vert, dir);
            //Log.info("(" + dir + ": " + l.printCoords() + ") = " + getBlockId(l) + " W: " + width);
            if (!l.compare(getPos().getX(), getPos().getY(), getPos().getZ())) {
                if (!getBlockState(l).getBlock().equals(verticalFrame)) {
                    error = HarvesterReasonForNotForming.VERTICAL_FRAME_EXPECTED;
                    extraErrorInfo = l.printCoords();
                    return false;
                }

            }

            //Farmost right!
            l = getLocationInHarvester(length + 1, width - 1, vert, dir);
            //Log.info("(" + dir + ": " + l.printCoords() + ") = " + getBlockId(l) + " W: " + width);
            if (!l.compare(getPos().getX(), getPos().getY(), getPos().getZ())) {
                if (!getBlockState(l).getBlock().equals(verticalFrame)) {
                    error = HarvesterReasonForNotForming.VERTICAL_FRAME_EXPECTED;
                    extraErrorInfo = l.printCoords();
                    return false;
                }
            }

            //Base right
            l = getLocationInHarvester(0, width - 1, vert, dir);
            //Log.info("(" + dir + ": " + l.printCoords() + ") = " + getBlockId(l) + " W: " + width);
            if (!l.compare(getPos().getX(), getPos().getY(), getPos().getZ())) {
                if (!getBlockState(l).getBlock().equals(verticalFrame)) {
                    error = HarvesterReasonForNotForming.VERTICAL_FRAME_EXPECTED;
                    extraErrorInfo = l.printCoords();
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
    public ItemStack getStackInSlot(int i) {

        markBlockForUpdate();
        if (i < 9) {
            return seedsStorage[i];
        } else if (i >= 9) {
            return outputStorage[i - 9];
        } else {
            return null;
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {

        ItemStack inventory = getStackInSlot(i);
        
        if (inventory == null) {
            return null;
        }
        
        ItemStack ret;
        if (inventory.stackSize < j) {
            ret = inventory;
        } else {
            ret = inventory.splitStack(j);
            if (inventory.stackSize <= 0) {
                if (i < 9) {
                    seedsStorage[i] = null;
                } else {
                    outputStorage[i - 9] = null;
                }
            }
        }
        markBlockForUpdate();

        return ret;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return decrStackSize(index, getStackInSlot(index).stackSize);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

        if (i < 9) {
            seedsStorage[i] = itemStack;
        } else {
            outputStorage[i - 9] = itemStack;
        }
        markBlockForUpdate();
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return worldObj.getTileEntity(getPos()) == this && player.getDistanceSq(getPos().getX(), getPos().getY(), getPos().getZ()) < 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {

        return i < 9 && (seedsStorage[i] == null || (seedsStorage[i].getItem().equals(itemStack.getItem()) && seedsStorage[i].getItemDamage() == itemStack.getItemDamage()));
    }

    @Override
    public int getField(int id) {

        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {

        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing var1) {

        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, EnumFacing j) {

        if (i < 9) {
            return isItemValidForSlot(i, itemStack);
        } else
            return (getStackInSlot(i) == null || getStackInSlot(i).isItemEqual(itemStack));
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, EnumFacing j) {

        return true;
    }

    @Override
    public void onFluidLevelChanged(int old) {

    }

    @Override
    public boolean canConnectTo(EnumFacing side) {

        return isMultiblock;
    }


    @Override
    public boolean canWork(EnumFacing dir) {

        return dir.equals(EnumFacing.UP);
    }

    public boolean getIsMultiblock() {

        return isMultiblock;
    }

    public EnumFacing getFacing() {

        return facing;
    }

    private void checkPlantable() {

        for (int w = 0; w < harvesterWidth; w++) {
            TileHarvesterTrolley t = getTrolleyFromList(w);
            if (t == null) {
                return;
            }
            int theSeedToPlant = t.canPlantSeed(seedsStorage, harvesterLength - 1);
            if (theSeedToPlant != -1) {
                ItemStack toPlant = seedsStorage[theSeedToPlant].copy();
                toPlant.stackSize = 1;
                seedsStorage[theSeedToPlant].stackSize--;
                if (seedsStorage[theSeedToPlant].stackSize <= 0) {
                    seedsStorage[theSeedToPlant] = null;
                }
                t.doPlant(toPlant);
                pistonMoving = w;
                isPlanting = true;
                break;
            }
        }
    }

    private void checkHarvest() {
        //For now, only crops!
        for (int w = 0; w < harvesterWidth; w++) {
            TileHarvesterTrolley t = getTrolleyFromList(w);
            if (t == null) {
                return;
            }
            List<ItemStack> dropped = t.checkHarvest(harvesterLength);
            if (dropped == null) continue;
            boolean placeForAll = true;
            for (ItemStack st : dropped) {
                if (!isPlaceForItems(st)) {
                    placeForAll = false;
                    break;
                }
            }
            if (placeForAll) {
                t.doHarvest();
                isHarvesting = true;
                pistonMoving = w;
                break;
            }
        }
    }

    @Override
    public String getName() {

        return Localization.getLocalizedName(Names.blockHydraulicHarvester.unlocalized);
    }

    @Override
    public boolean hasCustomName() {

        return true;
    }

    @Override
    public ITextComponent getDisplayName() {

        return new TextComponentTranslation(Names.blockHydraulicHarvester.unlocalized);
    }

    @Override
    public void extendPistonTo(int piston, float length) {

        TileHydraulicPiston p = getPistonFromList(piston);
        p.extendTo(length);
    }

    public HarvesterReasonForNotForming getError() {

        return error;
    }

    public String getExtraErrorInfo() {

        return extraErrorInfo;
    }
}
