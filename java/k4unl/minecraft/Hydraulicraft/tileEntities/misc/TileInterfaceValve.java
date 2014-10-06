package k4unl.minecraft.Hydraulicraft.tileEntities.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileInterfaceValve extends TileHydraulicBaseNoPower implements ISidedInventory, IFluidHandler {
    private int targetX;
    private int targetY;
    private int targetZ;
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
        targetX = xCoord;
        targetY = yCoord;
        targetZ = zCoord;
        targetHasChanged = true;

        if (!worldObj.isRemote) {
            clientNeedsToResetTarget = true;
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void setTarget(int x, int y, int z) {

        targetX = x;
        targetY = y;
        targetZ = z;
        targetHasChanged = true;

        if (!worldObj.isRemote) {
            clientNeedsToSetTarget = true;
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public IHydraulicConsumer getTarget() {

        if (targetHasChanged == true && (targetX != xCoord || targetY != yCoord || targetZ != zCoord)) {
            TileEntity t = worldObj.getTileEntity(targetX, targetY, targetZ);
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
        } else if (targetHasChanged == true && targetX == xCoord && targetY == yCoord && targetZ == zCoord) {
            target = null;
            fluidTarget = null;
			inventoryTarget = null;
		}
		return target;
	}
	
	public IFluidHandler getFluidTarget(){
		if(targetHasChanged == true && (targetX != xCoord || targetY != yCoord || targetZ != zCoord)){
			TileEntity t = worldObj.getTileEntity(targetX, targetY, targetZ);
			if(t instanceof IHydraulicConsumer){
				target = (IHydraulicConsumer) t;
            }
            if(t instanceof IFluidHandler){
                fluidTarget = (IFluidHandler) t;
            }
            if(t instanceof ISidedInventory){
                inventoryTarget = (ISidedInventory) t;
            }
            targetHasChanged = false;
			//}
		}else if(targetHasChanged == true && targetX == xCoord && targetY == yCoord && targetZ == zCoord){
			target = null;
			fluidTarget = null;
			inventoryTarget = null;
		}
		return fluidTarget;
	}
	
	public ISidedInventory getInventoryTarget(){
		if(targetHasChanged == true && (targetX != xCoord || targetY != yCoord || targetZ != zCoord)){
			TileEntity t = worldObj.getTileEntity(targetX, targetY, targetZ);
			if(t instanceof IHydraulicConsumer){
				target = (IHydraulicConsumer) t;
				if(t instanceof IFluidHandler){
					fluidTarget = (IFluidHandler) t;
				}
				if(t instanceof ISidedInventory){
					inventoryTarget = (ISidedInventory) t;
				}
				targetHasChanged = false;
			}
		}else if(targetHasChanged == true && targetX == xCoord && targetY == yCoord && targetZ == zCoord){
			target = null;
			fluidTarget = null;
			inventoryTarget = null;
		}
		return inventoryTarget;
	}
	

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		targetX = tagCompound.getInteger("targetX");
		targetY = tagCompound.getInteger("targetY");
		targetZ = tagCompound.getInteger("targetZ");
		if(tagCompound.getBoolean("isTargetNull")){
			target = null;
		}
		if(worldObj != null && worldObj.isRemote){
			if(tagCompound.getBoolean("clientNeedsToResetTarget")){
				resetTarget();
			}
			if(tagCompound.getBoolean("clientNeedsToSetTarget")){
				targetHasChanged = true;
				getTarget();
			}
		}
		isTank = tagCompound.getBoolean("isTank");
		tankCorner1 = new Location(tagCompound.getIntArray("tankCorner1"));
		tankCorner2 = new Location(tagCompound.getIntArray("tankCorner2"));
		if(tankScore != tagCompound.getInteger("tankScore")) {
            if(tankScore == 0){
                tankScore = tagCompound.getInteger("tankScore");
                tank = new FluidTank(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
            }
		}
		if(tank == null){
			tank = new FluidTank(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
		}
		NBTTagCompound tankCompound = tagCompound.getCompoundTag("tank");
		if(tankCompound != null){
			if(tank != null){
				tank.readFromNBT(tankCompound);
			}
		}
	}
	
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		NBTTagCompound tagCompound = packet.func_148857_g();
		this.readFromNBT(tagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 5, tagCompound);
	}
	

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("targetX", targetX);
		tagCompound.setInteger("targetY", targetY);
		tagCompound.setInteger("targetZ", targetZ);
		tagCompound.setBoolean("isTargetNull", (target == null));
		if(target == null){
			tagCompound.setBoolean("isTargetNull", (target == null));			
		}
		if(worldObj != null && !worldObj.isRemote){
			tagCompound.setBoolean("clientNeedsToResetTarget", clientNeedsToResetTarget);
			tagCompound.setBoolean("clientNeedsToSetTarget", clientNeedsToSetTarget);
			clientNeedsToResetTarget = false;
			clientNeedsToSetTarget = false;
		}
		tagCompound.setBoolean("targetHasChanged", targetHasChanged);

		tagCompound.setBoolean("isTank", isTank);
		if(isTank == true) {
			tagCompound.setIntArray("tankCorner1", tankCorner1.getIntArray());
			tagCompound.setIntArray("tankCorner2", tankCorner2.getIntArray());
			tagCompound.setInteger("tankScore", tankScore);
		}else{
            tagCompound.setInteger("tankScore", 0);
        }
		NBTTagCompound tankCompound = new NBTTagCompound();
		if(tank != null){
			tank.writeToNBT(tankCompound);
			tagCompound.setTag("tank", tankCompound);
		}
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(!isTank) {
			if (getFluidTarget() != null) {
				return getFluidTarget().fill(from, resource, doFill);
			} else {
				return 0;
			}
		}else{
			getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
			return tank.fill(resource, doFill);

		}
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if(!isTank) {
			if (getFluidTarget() != null) {
				return getFluidTarget().drain(from, resource, doDrain);
			} else {
				return null;
			}
		}else{
			return null;
		}
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(!isTank) {
			if (getFluidTarget() != null) {
				return getFluidTarget().drain(from, maxDrain, doDrain);
			} else {
				return null;
			}
		}else{
			getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
			return tank.drain(maxDrain, doDrain);
		}
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(!isTank) {
			if (getFluidTarget() != null) {
				return getFluidTarget().canFill(from, fluid);
			} else {
				return false;
			}
		}else{
			return true;
		}
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(!isTank) {
			if (getFluidTarget() != null) {
				return getFluidTarget().canDrain(from, fluid);
			} else {
				return false;
			}
		}else{
			return true;
		}
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(!isTank) {
			if (getFluidTarget() != null) {
				return getFluidTarget().getTankInfo(from);
			} else {
				return null;
			}
		}else{
			FluidTankInfo[] tankInfo = {new FluidTankInfo(tank)};
			return tankInfo;
		}
	}

	@Override
	public int getSizeInventory() {
		if(getInventoryTarget() != null){
			return getInventoryTarget().getSizeInventory();
		}
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().getStackInSlot(i);
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().decrStackSize(i, j);
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().getStackInSlotOnClosing(i);
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(getInventoryTarget() != null){
			getInventoryTarget().setInventorySlotContents(i, itemstack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		if(getInventoryTarget() != null){
			return getInventoryTarget().getInventoryStackLimit();
		}
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().isUseableByPlayer(entityplayer);
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().isItemValidForSlot(i, itemstack);
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().getAccessibleSlotsFromSide(var1);
		}
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().canInsertItem(i, itemstack, j);
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(getInventoryTarget() != null){
			return getInventoryTarget().canExtractItem(i, itemstack, j);
		}
		return false;
	}

	@Override
	public String getInventoryName() {
		if(getInventoryTarget() != null){
			return getInventoryTarget().getInventoryName();
		}
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		if(getInventoryTarget() != null){
			return getInventoryTarget().hasCustomInventoryName();
		}
		return false;
	}

	@Override
	public void openInventory() {
		if(getInventoryTarget() != null){
			getInventoryTarget().openInventory();
		}		
	}

	@Override
	public void closeInventory() {
		if(getInventoryTarget() != null){
			getInventoryTarget().closeInventory();
		}	
	}

	public void checkTank(ForgeDirection sideClicked) {
        if(getFluidTarget() == null){
            Log.info("Checking tank. Clicked on side " + sideClicked);
            ForgeDirection tankDir = sideClicked.getOpposite();


            int minX = 0;
            int minY = 0;
            int minZ = 0;
            int maxX = 0;
            int maxY = 0;
            int maxZ = 0;

            Location otherSide = new Location(xCoord, yCoord, zCoord, tankDir);
            int offset = 0;
            int size = 0;
            for(offset = 0; offset < Constants.MAX_TANK_SIZE; offset++){
                Location testLoc = new Location(otherSide, tankDir, offset);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    size++;
                }else{
                    break;
                }
            }

            if(size == Constants.MAX_TANK_SIZE){
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    return;
                }
            }
            if(tankDir.offsetX == 1){
                minX = otherSide.getX();
                maxX = new Location(otherSide, tankDir, offset-1).getX();
            }
            if(tankDir.offsetX == -1){
                maxX = otherSide.getX();
                minX = new Location(otherSide, tankDir, offset-1).getX();
            }
            if(tankDir.offsetY == 1){
                minY = otherSide.getY();
                maxY = new Location(otherSide, tankDir, offset-1).getY();
            }
            if(tankDir.offsetY == -1){
                maxY = otherSide.getY();
                minY = new Location(otherSide, tankDir, offset-1).getY();
            }
            if(tankDir.offsetZ == 1){
                minZ = otherSide.getZ();
                maxZ = new Location(otherSide, tankDir, offset-1).getZ();
            }
            if(tankDir.offsetZ == -1){
                maxZ = otherSide.getZ();
                minZ = new Location(otherSide, tankDir, offset-1).getZ();
            }

            int sizeRemaining = Constants.MAX_TANK_SIZE;
            ForgeDirection rotated;
            if(!tankDir.equals(ForgeDirection.UP) && !tankDir.equals(ForgeDirection.DOWN)){
                rotated = tankDir.getRotation(ForgeDirection.UP);
            }else{
                rotated = tankDir.getRotation(ForgeDirection.NORTH);
            }

            size = 0;
            for(offset = 0; offset <= Constants.MAX_TANK_SIZE; offset++){
                Location testLoc = new Location(otherSide, rotated, offset);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    size++;
                }else{
                    break;
                }
            }
            sizeRemaining = Constants.MAX_TANK_SIZE - offset;

            if(size == Constants.MAX_TANK_SIZE){
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    return;
                }
            }

            if(rotated.offsetX == 1){
                maxX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetX == -1){
                minX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetY == 1){
                maxY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetY == -1){
                minY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetZ == 1){
                maxZ = new Location(otherSide, rotated, size-1).getZ();
            }
            if(rotated.offsetZ == -1){
                minZ = new Location(otherSide, rotated, size-1).getZ();
            }

            rotated = rotated.getOpposite();
            size = 0;
            for(offset = 0; offset <= sizeRemaining; offset++){
                Location testLoc = new Location(otherSide, rotated, offset);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    size++;
                }else{
                    break;
                }
            }
            sizeRemaining = Constants.MAX_TANK_SIZE - offset;

            if(size == Constants.MAX_TANK_SIZE){
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    return;
                }
            }

            if(rotated.offsetX == 1){
                maxX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetX == -1){
                minX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetY == 1){
                maxY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetY == -1){
                minY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetZ == 1){
                maxZ = new Location(otherSide, rotated, size-1).getZ();
            }
            if(rotated.offsetZ == -1){
                minZ = new Location(otherSide, rotated, size-1).getZ();
            }

            //Now, rotate it the OTHER way
            if(!tankDir.equals(ForgeDirection.EAST) && !tankDir.equals
                    (ForgeDirection.WEST)){
                rotated = tankDir.getRotation(ForgeDirection.EAST);
            }else{
                rotated = tankDir.getRotation(ForgeDirection.NORTH);
            }

            size = 0;
            for(offset = 0; offset <= Constants.MAX_TANK_SIZE; offset++){
                Location testLoc = new Location(otherSide, rotated, offset);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    size++;
                }else{
                    break;
                }
            }
            sizeRemaining = Constants.MAX_TANK_SIZE - offset;

            if(size == Constants.MAX_TANK_SIZE){
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    return;
                }
            }

            if(rotated.offsetX == 1){
                maxX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetX == -1){
                minX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetY == 1){
                maxY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetY == -1){
                minY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetZ == 1){
                maxZ = new Location(otherSide, rotated, size-1).getZ();
            }
            if(rotated.offsetZ == -1){
                minZ = new Location(otherSide, rotated, size-1).getZ();
            }

            rotated = rotated.getOpposite();
            size = 0;
            for(offset = 0; offset <= sizeRemaining; offset++){
                Location testLoc = new Location(otherSide, rotated, offset);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    size++;
                }else{
                    break;
                }
            }
            sizeRemaining = Constants.MAX_TANK_SIZE - offset;

            if(size == Constants.MAX_TANK_SIZE){
                //Check if there's a block at the end.
                Location testLoc = new Location(otherSide, tankDir, size);
                if(testLoc.getBlock(getWorldObj()) == Blocks.air){
                    return;
                }
            }

            if(rotated.offsetX == 1){
                maxX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetX == -1){
                minX = new Location(otherSide, rotated, size-1).getX();
            }
            if(rotated.offsetY == 1){
                maxY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetY == -1){
                minY = new Location(otherSide, rotated, size-1).getY();
            }
            if(rotated.offsetZ == 1){
                maxZ = new Location(otherSide, rotated, size-1).getZ();
            }
            if(rotated.offsetZ == -1){
                minZ = new Location(otherSide, rotated, size-1).getZ();
            }
            Log.info("X-: " + minX + " X+:" + maxX + " Y-: " + minY + " Y+:" + maxY + " Z-: " + minZ + " Z+:" + maxZ);

            List<Location> airBlocks = new ArrayList<Location>();
            List<Location> valveBlocks = new ArrayList<Location>();
            tankScore = 0;
            //Now.. Get all the blocks that are there:
            for(int x = minX-1; x <= maxX+1; x++){
                for(int y = minY-1; y <= maxY+1; y++){
                    for(int z = minZ-1; z <= maxZ+1; z++){
                        Block bl = getWorldObj().getBlock(x,y,z);
                        if((x >= minX && x <= maxX) && (y >= minY && y <= maxY) && (z >= minZ && z <= maxZ)){
                            if(bl == Blocks.air){
                                airBlocks.add(new Location(x,y,z));
                            }else{
                                return;
                            }
                        }else{
                            if(bl == Blocks.air){
                                Log.info("Block at " + x + ", " + y + ", " + z + " is air!");
                                return;
                            }else if(HCConfig.isTankBlockBlacklisted(bl)){
                                Log.info("Block (" + bl.getUnlocalizedName() + ") at " + x + ", " + y + ", " + z + " is blacklisted!");
                                return;
                            }else{
                                if(bl == HCBlocks.blockInterfaceValve){
                                    Location vBlock =new Location(x, y, z);
                                    if(!vBlock.compare(xCoord, yCoord, zCoord)){
                                        valveBlocks.add(vBlock);
                                    }
                                }else{
                                    //Check what material this tank is made of, it adds to the tankScore.
                                    //We should make an array here
                                    tankScore += HCConfig.getTankBlockScore(bl);
                                }
                            }

                        }
                    }
                }
            }
            tankScore = airBlocks.size() + tankScore;
            //Now. modify the locations so that it actually uses the BLOCKS
            minX -= 1;
            minY -= 1;
            minZ -= 1;
            maxX += 1;
            maxY += 1;
            maxZ += 1;

            for(Location valveLoc : valveBlocks){
                ((TileInterfaceValve)valveLoc.getTE(getWorldObj())).setTarget(xCoord, yCoord, zCoord);
            }

            tankCorner1 = new Location(minX, minY, minZ);
            tankCorner2 = new Location(maxX, maxY, maxZ);
            isTank = true;
            if(tank == null){
                tank = new FluidTank(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
            }else{
                tank.setCapacity(tankScore * FluidContainerRegistry.BUCKET_VOLUME);
            }
            //We should save this tank to an array.
            Hydraulicraft.tankList.addNewTank(tankCorner1, tankCorner2, new Location(xCoord, yCoord, zCoord));
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
            getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        }
	}

    public void breakTank(){
        isTank = false;
        Hydraulicraft.tankList.deleteTank(tankCorner1, tankCorner2);
        tankCorner1 = null;
        tankCorner2 = null;
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
        getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
    }

	public Location getTankCorner1(){
		return tankCorner1;
	}

	public Location getTankCorner2(){
		return tankCorner2;
	}

	public boolean isValidTank() {
		return isTank;
	}

    @Override
    public void invalidate(){
        super.invalidate();
        if(isTank){
            breakTank();
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(){
        /*float extendedLength = getExtendedLength();
        float sidewaysMovement = getSideLength();*/

        float minX = 0.0F + xCoord;
        float minY = 0.0F + yCoord;
        float minZ = 0.0F + zCoord;
        float maxX = 1.0F + xCoord;
        float maxY = 1.0F + yCoord;
        float maxZ = 1.0F + zCoord;

        if(isValidTank()){
            int outerXDifference = 0;
            int outerYDifference = 0;
            int outerZDifference = 0;

            outerXDifference = tankCorner2.getX() - tankCorner1.getX();
            outerYDifference = tankCorner2.getY() - tankCorner1.getY();
            outerZDifference = tankCorner2.getZ() - tankCorner1.getZ();

            minX = tankCorner1.getX() - xCoord;
            minY = tankCorner1.getY() - yCoord;
            minZ = tankCorner1.getZ() - zCoord;

            maxX = outerXDifference;
            maxY = outerYDifference;
            maxZ = outerZDifference;
            //Log.info("minX: "+ minX + " minY: " + minY + " minZ: " + minZ + " maxX: " + maxX + " maxY: " + maxY + " maxZ: " + maxZ);
        }
        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}

