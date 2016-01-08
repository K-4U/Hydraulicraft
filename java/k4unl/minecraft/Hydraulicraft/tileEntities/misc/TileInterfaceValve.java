package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.misc.BlockHydraulicPressureWall;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IConnectTexture;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileInterfaceValve extends TileHydraulicBaseNoPower implements ISidedInventory, IFluidHandler, IConnectTexture {
    private BlockPos targetPos;
    private boolean targetHasChanged = true;

    private boolean isTank = false;
    private Location tankCorner1;
    private Location tankCorner2;
    private int tankScore = 0;
    private FluidTank tank;


    private IHydraulicConsumer target;
    private IFluidHandler      fluidTarget;
    private ISidedInventory    inventoryTarget;
    private boolean clientNeedsToResetTarget = false;
    private boolean clientNeedsToSetTarget   = false;

    //TODO: Use PlayerInteractEvent for detection of right clicking.

    public void resetTarget() {

        target = null;
        targetPos = getPos();
        targetHasChanged = true;

        if (!worldObj.isRemote) {
            clientNeedsToResetTarget = true;
        }
        worldObj.markBlockForUpdate(getPos());
    }

    public void setTarget(BlockPos pos) {

        targetPos = pos;
        targetHasChanged = true;

        if (!worldObj.isRemote) {
            clientNeedsToSetTarget = true;
        }
        worldObj.markBlockForUpdate(getPos());
    }

    public IHydraulicConsumer getTarget() {
        if(targetPos != null) {
            if (targetHasChanged && !getPos().equals(targetPos)) {
                TileEntity t = worldObj.getTileEntity(targetPos);
                if (t instanceof IHydraulicConsumer) {
                    target = (IHydraulicConsumer) t;
                    targetHasChanged = false;
                }
                if (t instanceof IFluidHandler) {
                    fluidTarget = (IFluidHandler) t;
                }
                if (t instanceof ISidedInventory) {
                    inventoryTarget = (ISidedInventory) t;
                }
            } else if (targetHasChanged && targetPos.equals(getPos())) {
                target = null;
                fluidTarget = null;
                inventoryTarget = null;
            }
        }
        return target;
    }

    public IFluidHandler getFluidTarget() {
        if(targetPos != null) {
            if (targetHasChanged && !targetPos.equals(getPos())) {
                TileEntity t = worldObj.getTileEntity(targetPos);
                if (t instanceof IHydraulicConsumer) {
                    target = (IHydraulicConsumer) t;
                }
                if (t instanceof IFluidHandler) {
                    fluidTarget = (IFluidHandler) t;
                }
                if (t instanceof ISidedInventory) {
                    inventoryTarget = (ISidedInventory) t;
                }
                targetHasChanged = false;
                //}
            } else if (targetHasChanged && getPos().equals(targetPos)) {
                target = null;
                fluidTarget = null;
                inventoryTarget = null;
            }
        }
        return fluidTarget;
    }

    public ISidedInventory getInventoryTarget() {
        if(targetPos != null) {
            if (targetHasChanged && !targetPos.equals(getPos())) {
                TileEntity t = worldObj.getTileEntity(targetPos);
                if (t instanceof IHydraulicConsumer) {
                    target = (IHydraulicConsumer) t;
                    if (t instanceof IFluidHandler) {
                        fluidTarget = (IFluidHandler) t;
                    }
                    if (t instanceof ISidedInventory) {
                        inventoryTarget = (ISidedInventory) t;
                    }
                    targetHasChanged = false;
                }
            } else if (targetHasChanged && targetPos.equals(getPos())) {
                target = null;
                fluidTarget = null;
                inventoryTarget = null;
            }
        }
        return inventoryTarget;
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        targetPos = BlockPos.fromLong(tagCompound.getLong("targetPos"));
        if (tagCompound.getBoolean("isTargetNull")) {
            target = null;
        }
        if (worldObj != null && worldObj.isRemote) {
            if (tagCompound.getBoolean("clientNeedsToResetTarget")) {
                resetTarget();
            }
            if (tagCompound.getBoolean("clientNeedsToSetTarget")) {
                targetHasChanged = true;
                getTarget();
            }
        }
        isTank = tagCompound.getBoolean("isTank");
        tankCorner1 = new Location(tagCompound.getIntArray("tankCorner1"));
        tankCorner2 = new Location(tagCompound.getIntArray("tankCorner2"));
        if (tankScore != tagCompound.getInteger("tankScore")) {
            if (tankScore == 0) {
                tankScore = tagCompound.getInteger("tankScore");
                tank = new FluidTank(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
            }
        }
        if (tank == null) {
            tank = new FluidTank(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
        }
        NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
        if (tankCompound != null) {
            if (tank != null) {
                tank.readFromNBT(tankCompound);
            }
        }
    }


    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        NBTTagCompound tagCompound = packet.getNbtCompound();
        this.readFromNBT(tagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 5, tagCompound);
    }


    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if(targetPos != null) {
            tagCompound.setLong("targetPos", targetPos.toLong());
        }
        tagCompound.setBoolean("isTargetNull", (target == null));
        if (target == null) {
            tagCompound.setBoolean("isTargetNull", (target == null));
        }
        if (worldObj != null && !worldObj.isRemote) {
            tagCompound.setBoolean("clientNeedsToResetTarget", clientNeedsToResetTarget);
            tagCompound.setBoolean("clientNeedsToSetTarget", clientNeedsToSetTarget);
            clientNeedsToResetTarget = false;
            clientNeedsToSetTarget = false;
        }
        tagCompound.setBoolean("targetHasChanged", targetHasChanged);

        tagCompound.setBoolean("isTank", isTank);
        if (isTank) {
            tagCompound.setIntArray("tankCorner1", tankCorner1.getIntArray());
            tagCompound.setIntArray("tankCorner2", tankCorner2.getIntArray());
            tagCompound.setInteger("tankScore", tankScore);
        } else {
            tagCompound.setInteger("tankScore", 0);
        }
        NBTTagCompound tankCompound = new NBTTagCompound();
        if (tank != null) {
            tank.writeToNBT(tankCompound);
            tagCompound.setTag("tank", tankCompound);
        }
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (!isTank) {
            if (getFluidTarget() != null) {
                return getFluidTarget().fill(from, resource, doFill);
            } else {
                return 0;
            }
        } else {
            getWorld().markBlockForUpdate(getPos());
            return tank.fill(resource, doFill);

        }
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (!isTank) {
            if (getFluidTarget() != null) {
                return getFluidTarget().drain(from, resource, doDrain);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        if (!isTank) {
            if (getFluidTarget() != null) {
                return getFluidTarget().drain(from, maxDrain, doDrain);
            } else {
                return null;
            }
        } else {
            getWorld().markBlockForUpdate(getPos());
            return tank.drain(maxDrain, doDrain);
        }
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return isTank || getFluidTarget() != null && getFluidTarget().canFill(from, fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return isTank || getFluidTarget() != null && getFluidTarget().canDrain(from, fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        if (!isTank) {
            if (getFluidTarget() != null) {
                return getFluidTarget().getTankInfo(from);
            } else {
                return null;
            }
        } else {
            return new FluidTankInfo[]{new FluidTankInfo(tank)};
        }
    }

    @Override
    public int getSizeInventory() {
        if (getInventoryTarget() != null) {
            return getInventoryTarget().getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if (getInventoryTarget() != null) {
            return getInventoryTarget().getStackInSlot(i);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (getInventoryTarget() != null) {
            return getInventoryTarget().decrStackSize(i, j);
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(getInventoryTarget() != null){
            return getInventoryTarget().removeStackFromSlot(index);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (getInventoryTarget() != null) {
            getInventoryTarget().setInventorySlotContents(i, itemstack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        if (getInventoryTarget() != null) {
            return getInventoryTarget().getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return getInventoryTarget() != null && getInventoryTarget().isUseableByPlayer(entityplayer);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return getInventoryTarget() != null && getInventoryTarget().isItemValidForSlot(i, itemstack);
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
        if (getInventoryTarget() != null) {
            return getInventoryTarget().getSlotsForFace(var1);
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing j) {
        return getInventoryTarget() != null && getInventoryTarget().canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing j) {
        return getInventoryTarget() != null && getInventoryTarget().canExtractItem(i, itemstack, j);
    }

    @Override
    public String getName() {
        if (getInventoryTarget() != null) {
            return getInventoryTarget().getName();
        }
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return getInventoryTarget() != null && getInventoryTarget().hasCustomName();
    }

    @Override
    public IChatComponent getDisplayName() {
        if(getInventoryTarget() != null){
            return getInventoryTarget().getDisplayName();
        }
        return null;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (getInventoryTarget() != null) {
            getInventoryTarget().openInventory(player);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (getInventoryTarget() != null) {
            getInventoryTarget().closeInventory(player);
        }
    }

    public void checkTank(EnumFacing sideClicked) {
        if (getFluidTarget() == null) {
            //Log.info("Checking tank. Clicked on side " + sideClicked);
            EnumFacing tankDir = sideClicked.getOpposite();


            int minX = 0;
            int minY = 0;
            int minZ = 0;
            int maxX = 0;
            int maxY = 0;
            int maxZ = 0;

            Location otherSide = new Location(getPos(), tankDir);
            int offset;
            int size = 0;
            for (offset = 0; offset < Constants.MAX_TANK_SIZE; offset++) {
                Location testLoc = new Location(otherSide, tankDir, offset);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    size++;
                } else {
                    break;
                }
            }

            if (size == Constants.MAX_TANK_SIZE) {
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    return;
                }
            }
            if (tankDir.getFrontOffsetX() == 1) {
                minX = otherSide.getX();
                maxX = new Location(otherSide, tankDir, offset - 1).getX();
            }
            if (tankDir.getFrontOffsetX() == -1) {
                maxX = otherSide.getX();
                minX = new Location(otherSide, tankDir, offset - 1).getX();
            }
            if (tankDir.getFrontOffsetY() == 1) {
                minY = otherSide.getY();
                maxY = new Location(otherSide, tankDir, offset - 1).getY();
            }
            if (tankDir.getFrontOffsetY() == -1) {
                maxY = otherSide.getY();
                minY = new Location(otherSide, tankDir, offset - 1).getY();
            }
            if (tankDir.getFrontOffsetZ() == 1) {
                minZ = otherSide.getZ();
                maxZ = new Location(otherSide, tankDir, offset - 1).getZ();
            }
            if (tankDir.getFrontOffsetZ() == -1) {
                maxZ = otherSide.getZ();
                minZ = new Location(otherSide, tankDir, offset - 1).getZ();
            }

            int sizeRemaining;
            EnumFacing rotated;
            if (!tankDir.equals(EnumFacing.UP) && !tankDir.equals(EnumFacing.DOWN)) {
                rotated = tankDir.rotateAround(EnumFacing.UP.getAxis());
            } else {
                rotated = tankDir.rotateAround(EnumFacing.NORTH.getAxis());
            }

            size = 0;
            for (offset = 0; offset <= Constants.MAX_TANK_SIZE; offset++) {
                Location testLoc = new Location(otherSide, rotated, offset);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    size++;
                } else {
                    break;
                }
            }
            sizeRemaining = Constants.MAX_TANK_SIZE - offset;

            if (size == Constants.MAX_TANK_SIZE) {
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    return;
                }
            }

            if (rotated.getFrontOffsetX() == 1) {
                maxX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetX() == -1) {
                minX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetY() == 1) {
                maxY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetY() == -1) {
                minY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetZ() == 1) {
                maxZ = new Location(otherSide, rotated, size - 1).getZ();
            }
            if (rotated.getFrontOffsetZ() == -1) {
                minZ = new Location(otherSide, rotated, size - 1).getZ();
            }

            rotated = rotated.getOpposite();
            size = 0;
            for (offset = 0; offset <= sizeRemaining; offset++) {
                Location testLoc = new Location(otherSide, rotated, offset);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    size++;
                } else {
                    break;
                }
            }

            if (size == Constants.MAX_TANK_SIZE) {
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    return;
                }
            }

            if (rotated.getFrontOffsetX() == 1) {
                maxX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetX() == -1) {
                minX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetY() == 1) {
                maxY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetY() == -1) {
                minY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetZ() == 1) {
                maxZ = new Location(otherSide, rotated, size - 1).getZ();
            }
            if (rotated.getFrontOffsetZ() == -1) {
                minZ = new Location(otherSide, rotated, size - 1).getZ();
            }

            //Now, rotate it the OTHER way
            if (!tankDir.equals(EnumFacing.EAST) && !tankDir.equals
                    (EnumFacing.WEST)) {
                rotated = tankDir.rotateAround(EnumFacing.EAST.getAxis());
            } else {
                rotated = tankDir.rotateAround(EnumFacing.NORTH.getAxis());
            }

            size = 0;
            for (offset = 0; offset <= Constants.MAX_TANK_SIZE; offset++) {
                Location testLoc = new Location(otherSide, rotated, offset);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    size++;
                } else {
                    break;
                }
            }
            sizeRemaining = Constants.MAX_TANK_SIZE - offset;

            if (size == Constants.MAX_TANK_SIZE) {
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    return;
                }
            }

            if (rotated.getFrontOffsetX() == 1) {
                maxX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetX() == -1) {
                minX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetY() == 1) {
                maxY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetY() == -1) {
                minY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetZ() == 1) {
                maxZ = new Location(otherSide, rotated, size - 1).getZ();
            }
            if (rotated.getFrontOffsetZ() == -1) {
                minZ = new Location(otherSide, rotated, size - 1).getZ();
            }

            rotated = rotated.getOpposite();
            size = 0;
            for (offset = 0; offset <= sizeRemaining; offset++) {
                Location testLoc = new Location(otherSide, rotated, offset);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    size++;
                } else {
                    break;
                }
            }

            if (size == Constants.MAX_TANK_SIZE) {
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if (testLoc.getBlock(getWorld()).getMaterial() == Material.air) {
                    return;
                }
            }

            if (rotated.getFrontOffsetX() == 1) {
                maxX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetX() == -1) {
                minX = new Location(otherSide, rotated, size - 1).getX();
            }
            if (rotated.getFrontOffsetY() == 1) {
                maxY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetY() == -1) {
                minY = new Location(otherSide, rotated, size - 1).getY();
            }
            if (rotated.getFrontOffsetZ() == 1) {
                maxZ = new Location(otherSide, rotated, size - 1).getZ();
            }
            if (rotated.getFrontOffsetZ() == -1) {
                minZ = new Location(otherSide, rotated, size - 1).getZ();
            }
            //Log.info("X-: " + minX + " X+:" + maxX + " Y-: " + minY + " Y+:" + maxY + " Z-: " + minZ + " Z+:" + maxZ);

            List<Location> airBlocks = new ArrayList<Location>();
            List<Location> valveBlocks = new ArrayList<Location>();
            tankScore = 0;
            HashMap<Block, Integer> blocks = new HashMap<Block, Integer>();
            //Now.. Get all the blocks that are there:
            for (int x = minX - 1; x <= maxX + 1; x++) {
                for (int y = minY - 1; y <= maxY + 1; y++) {
                    for (int z = minZ - 1; z <= maxZ + 1; z++) {
                        Block bl = getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
                        if ((x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ)) {
                            if (bl.getMaterial() == Material.air) {
                                airBlocks.add(new Location(x, y, z));
                            } else {
                                return;
                            }
                        } else {
                            if (bl.getMaterial() == Material.air) {
                                //Log.info("Block at " + x + ", " + y + ", " + z + " is air!");
                                return;
                            } else if (HCConfig.isTankBlockBlacklisted(bl)) {
                                //Log.info("Block (" + bl.getUnlocalizedName() + ") at " + x + ", " + y + ", " + z + " is blacklisted!");
                                return;
                            } else {
                                if (bl == HCBlocks.blockInterfaceValve) {
                                    Location vBlock = new Location(x, y, z);
                                    if (!vBlock.compare(getPos())) {
                                        valveBlocks.add(vBlock);
                                    }
                                } else {
                                    //Check what material this tank is made of, it adds to the tankScore.
                                    //We should make an array here
                                    int c = 0;
                                    if(blocks.containsKey(bl)){
                                        c = blocks.get(bl);
                                        blocks.remove(bl);
                                    }
                                    blocks.put(bl, c+1);
                                    //tankScore += HCConfig.getTankBlockScore(bl);
                                }
                            }
                        }
                    }
                }
            }

            for(Map.Entry<Block, Integer> block : blocks.entrySet()){
                tankScore += HCConfig.getTankBlockScore(block.getKey()) * block.getValue();
                //Log.info(block.getKey().getLocalizedName() + "=" + block.getValue() + "=" + HCConfig.getTankBlockScore(block.getKey()));
            }
            //Log.info("TankScore = " + tankScore + " AB = " + airBlocks.size());
            tankScore = airBlocks.size() * tankScore;
            //Now. modify the locations so that it actually uses the BLOCKS
            minX -= 1;
            minY -= 1;
            minZ -= 1;
            maxX += 1;
            maxY += 1;
            maxZ += 1;

            for (Location valveLoc : valveBlocks) {
                ((TileInterfaceValve) valveLoc.getTE(getWorld())).setTarget(getPos());
            }

            tankCorner1 = new Location(minX, minY, minZ);
            tankCorner2 = new Location(maxX, maxY, maxZ);
            isTank = true;
            if (tank == null) {
                tank = new FluidTank(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
            } else {
                tank.setCapacity(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
            }
            //We should save this tank to an array.
            Hydraulicraft.tankList.addNewTank(tankCorner1, tankCorner2, new Location(getPos()));
            getWorld().markBlockForUpdate(getPos());
            getWorld().markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(), getPos().getY(), getPos().getZ());
        }
    }

    public void breakTank() {
        isTank = false;
        Hydraulicraft.tankList.deleteTank(tankCorner1, tankCorner2);
        tankCorner1 = null;
        tankCorner2 = null;
        getWorld().markBlockForUpdate(getPos());
        getWorld().markBlockRangeForRenderUpdate(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX(), getPos().getY(), getPos().getZ());
    }

    public Location getTankCorner1() {
        return tankCorner1;
    }

    public Location getTankCorner2() {
        return tankCorner2;
    }

    public boolean isValidTank() {
        return isTank;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (isTank) {
            breakTank();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        /*float extendedLength = getExtendedLength();
        float sidewaysMovement = getSideLength();*/

        float minX = 0.0F + getPos().getX();
        float minY = 0.0F + getPos().getY();
        float minZ = 0.0F + getPos().getZ();
        float maxX = 1.0F + getPos().getX();
        float maxY = 1.0F + getPos().getY();
        float maxZ = 1.0F + getPos().getZ();

        if (isValidTank()) {
            int outerXDifference;
            int outerYDifference;
            int outerZDifference;

            outerXDifference = tankCorner2.getX() - tankCorner1.getX();
            outerYDifference = tankCorner2.getY() - tankCorner1.getY();
            outerZDifference = tankCorner2.getZ() - tankCorner1.getZ();

            minX = tankCorner1.getX() - getPos().getX();
            minY = tankCorner1.getY() - getPos().getY();
            minZ = tankCorner1.getZ() - getPos().getZ();

            maxX = outerXDifference + tankCorner1.getX();
            maxY = outerYDifference + tankCorner1.getY();
            maxZ = outerZDifference + tankCorner1.getZ();
            //Log.info("minX: "+ minX + " minY: " + minY + " minZ: " + minZ + " maxX: " + maxX + " maxY: " + maxY + " maxZ: " + maxZ);
        }
        return AxisAlignedBB.fromBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean connectTexture() {
        return isValidTank() || getFluidTarget() != null || getInventoryTarget() != null || getTarget() != null;
    }

    @Override
    public boolean connectTextureTo(Block type) {
        return connectTexture() && (type instanceof BlockHydraulicPressureWall);// || type instanceof BlockHydraulicPressureValve);
    }
}

