package k4unl.minecraft.Hydraulicraft.tileEntities.gow;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.ItemIPCard;
import k4unl.minecraft.Hydraulicraft.lib.IPs;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class TilePortalBase extends TileHydraulicBase implements IInventory, IHydraulicConsumer {
    private boolean        portalFormed;
    private int            portalWidth;
    private int            portalHeight;
    private EnumFacing     baseDir;
    private EnumFacing     portalDir;
    private List<Location> frames;
    private long           ip;
    private boolean ipRegistered = false;
    private int     colorIndex   = 0;


    private boolean hasInterfaceValve = false;
    private Location interfaceValveLocation;

    private boolean hasHydraulicValve = false;
    private Location hydraulicValveLocation;

    private ItemStack linkingCard;

    public TilePortalBase() {
        super(1);
        super.init(this);
    }

    public TilePortalBase(PressureTier tier) {

        super(200);
        super.init(this);
        frames = new ArrayList<Location>();
    }

    private void genNewIP() {

        if (ip != 0) {
            Hydraulicraft.ipList.removeIP(ip);
        }
        if (getWorldObj() != null) {
            String IP = Hydraulicraft.ipList.generateNewRandomIP(getWorldObj().provider.getDimensionId());
            Hydraulicraft.ipList.registerIP(IPs.ipToLong(IP), new Location(getPos(), getWorldObj().provider.getDimensionId()));
            ipRegistered = false;
            ip = IPs.ipToLong(IP);
            getWorldObj().markBlockForUpdate(getPos());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);
        portalFormed = tCompound.getBoolean("portalFormed");
        portalWidth = tCompound.getInteger("portalWidth");
        portalHeight = tCompound.getInteger("portalHeight");

        baseDir = EnumFacing.byName(tCompound.getString("baseDir"));
        portalDir = EnumFacing.byName(tCompound.getString("portalDir"));

        NBTTagCompound linkCardNBT = tCompound.getCompoundTag("linkingCard");
        if (linkCardNBT != null) {
            linkingCard = ItemStack.loadItemStackFromNBT(linkCardNBT);
        }
        ip = tCompound.getLong("ip");
        if (ip == 0 && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            genNewIP();
        } else if (ip != 0) {
            ipRegistered = false;
        }

        colorIndex = tCompound.getInteger("colorIndex");
        hasInterfaceValve = tCompound.getBoolean("hasInterfaceValve");

        readFramesFromNBT(tCompound);
    }

    private void readFramesFromNBT(NBTTagCompound tCompound) {
        if (frames != null)
            frames.clear();
        if (frames == null) {
            frames = new ArrayList<Location>();
        }
        NBTTagCompound list = tCompound.getCompoundTag("portalFrames");
        int i;
        for (i = 0; i < list.getInteger("max"); i++) {
            Location frameLocation = new Location(list.getIntArray("" + i));
            frames.add(frameLocation);
        }
    }

    private void writeFramesToNBT(NBTTagCompound tCompound) {
        NBTTagCompound list = new NBTTagCompound();
        int i = 0;
        for (Location fr : frames) {
            list.setIntArray("" + i, fr.getIntArray());
            i++;
        }
        list.setInteger("max", frames.size());

        tCompound.setTag("portalFrames", list);
    }

    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
        super.writeToNBT(tCompound);

        tCompound.setBoolean("portalFormed", portalFormed);
        tCompound.setInteger("portalWidth", portalWidth);
        tCompound.setInteger("portalHeight", portalHeight);

        if (baseDir != null) {
            tCompound.setInteger("baseDir", baseDir.ordinal());
        }
        if (portalDir != null) {
            tCompound.setInteger("portalDir", portalDir.ordinal());
        }
        if (linkingCard != null) {
            NBTTagCompound linkCardNBT = linkingCard.writeToNBT(new NBTTagCompound());
            tCompound.setTag("linkingCard", linkCardNBT);
        }

        tCompound.setLong("ip", ip);
        tCompound.setInteger("colorIndex", colorIndex);

        tCompound.setBoolean("hasInterfaceValve", hasInterfaceValve);

        writeFramesToNBT(tCompound);
    }

    @Override
    public void update() {
        super.update();

        if (ip == 0) {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                genNewIP();
            }
        }
        //Every 10 ticks, check for a complete portal.
        if (getWorldObj().getTotalWorldTime() % 20 == 0 && !getWorldObj().isRemote) {
            if (checkPortalComplete()) {
                if (portalFormed) {
                    invalidatePortal();
                } else {
                    validatePortal();
                }
            }
        }
        if (!ipRegistered) {
            ipRegistered = true;
            Hydraulicraft.ipList.registerIP(ip, new Location(getPos(), worldObj.provider.getDimensionId()));
        }
    }

    private boolean checkPortalComplete() {
        int i = 0;
        baseDir = EnumFacing.NORTH;
        portalWidth = 0;
        int half = 0;
        while (i != 2) {
            for (int z = 1; z <= HCConfig.INSTANCE.getInt("maxPortalWidth"); z++) {
                Location nLocation = new Location(getPos(), baseDir, z);
                Location oLocation = new Location(getPos(), baseDir.getOpposite(), z);
                if (nLocation.getBlock(getWorldObj()) == HCBlocks.portalFrame) {
                    portalWidth++;
                    half++;
                } else {
                    break;
                }
                if (oLocation.getBlock(getWorldObj()) == HCBlocks.portalFrame) {
                    portalWidth++;
                } else {
                    break;
                }
            }
            if (portalWidth > 0 && portalWidth % 2 == 0) {
                //Valid portal found.
                //Break out of loop
                break;
            } else {
                portalWidth = 0;
            }

            baseDir = EnumFacing.EAST;
            i++;
        }
        if (portalWidth == 0 || portalWidth % 2 != 0) {
            return false;
        }


        //Now, that is the bottom taken care of. Let's see about the rest!
        i = 0;
        portalDir = baseDir.rotateAround(EnumFacing.Axis.Y);
        Location firstLocation = new Location(getPos(), baseDir, half);
        Location secondLocation = new Location(getPos(), baseDir.getOpposite(), half);
        portalHeight = 0;
        while (i != 3) {
            //Log.info("Checking for portal with basedir at " + baseDir + " and top at " + portalDir);
            for (int y = 1; y <= HCConfig.INSTANCE.getInt("maxPortalHeight"); y++) {
                Location nLocation = new Location(firstLocation, portalDir, y);
                Location oLocation = new Location(secondLocation, portalDir, y);
                if (nLocation.getBlock(getWorldObj()) == HCBlocks.portalFrame) {
                    portalHeight++;
                } else {
                    break;
                }
                if (oLocation.getBlock(getWorldObj()) != HCBlocks.portalFrame) {
                    break;
                }
            }

            if (portalHeight > 1) {
                break;
            }
            portalDir = portalDir.rotateAround(baseDir.getAxis());
            portalHeight = 0;
            i++;
        }

        if (portalHeight == 0) {
            return false;
        }

        //Check other side (aka top):
        Location topCenter = new Location(getPos(), portalDir, portalHeight);
        if (topCenter.getBlock(getWorldObj()) != HCBlocks.portalFrame) {
            return false;
        }
        for (int x = 1; x <= half; x++) {
            Location nLocation = new Location(getPos(), baseDir, x);
            Location oLocation = new Location(getPos(), baseDir.getOpposite(), x);
            if (nLocation.getBlock(getWorldObj()) != HCBlocks.portalFrame) {
                return false;
            }
            if (oLocation.getBlock(getWorldObj()) != HCBlocks.portalFrame) {
                return false;
            }
        }
        //Log.info("Found a portal. It's " + portalWidth + " wide and " + portalHeight + " high in " + baseDir + " with the portal in the " + portalDir);
        return true;
    }

    private void validatePortal() {
        frames.clear();
        Location bottomLeft = new Location(getPos(), baseDir, (portalWidth / 2));
        Location bottomRight = new Location(getPos(), baseDir.getOpposite(), (portalWidth / 2));
        if (bottomLeft.getBlock(getWorldObj()) != HCBlocks.portalFrame) {
            return;
        }

        for (int x = 0; x <= portalWidth + 1; x++) {
            Location handleLocation = new Location(bottomLeft, baseDir.getOpposite(), x);
            Location topLocation = new Location(handleLocation, portalDir, portalHeight);
            TileEntity te = handleLocation.getTE(getWorldObj());
            if (te instanceof TilePortalFrame) {
                ((TilePortalFrame) te).setPortalBase(this);
                ((TilePortalFrame) te).dye(colorIndex);
                frames.add(handleLocation);
            }
            te = topLocation.getTE(getWorldObj());
            if (te instanceof TilePortalFrame) {
                ((TilePortalFrame) te).setPortalBase(this);
                ((TilePortalFrame) te).dye(colorIndex);
                frames.add(topLocation);
            }
        }
        for (int y = 0; y <= portalHeight; y++) {
            Location leftLocation = new Location(bottomLeft, portalDir, y);
            Location rightLocation = new Location(bottomRight, portalDir, y);
            TileEntity te = leftLocation.getTE(getWorldObj());
            if (te instanceof TilePortalFrame) {
                ((TilePortalFrame) te).setPortalBase(this);
                ((TilePortalFrame) te).dye(colorIndex);
                frames.add(leftLocation);
            }
            te = rightLocation.getTE(getWorldObj());
            if (te instanceof TilePortalFrame) {
                ((TilePortalFrame) te).setPortalBase(this);
                ((TilePortalFrame) te).dye(colorIndex);
                frames.add(rightLocation);
            }
        }

        portalFormed = true;
        markDirty();
    }

    private void invalidatePortal() {

    }


    @Override
    public void validate() {
        super.validate();
    }


    @Override
    public void redstoneChanged(boolean isPowered) {
        if (getWorldObj() != null) {
            if (portalFormed && linkingCard != null) {
                if (getIsActive() && !getIsRedstonePowered()) {
                    setIsActive(false);
                    disablePortal();
                } else if (getIsRedstonePowered() && !getIsActive() && getPressure(EnumFacing.UP) >= HCConfig.INSTANCE.getInt("portalmBarUsagePerTickPerBlock")) {
                    setIsActive(true);
                    enablePortal();
                }
                markDirty();
            }
        }

    }

    private void enablePortal() {
        if (baseDir != null) {
            Location bottomLeft = new Location(getPos(), baseDir, (portalWidth / 2));
            bottomLeft.offset(baseDir.getOpposite(), 1);
            bottomLeft.offset(portalDir, 1);
            for (int x = 0; x <= portalWidth - 2; x++) {
                Location handleLocation = new Location(bottomLeft, baseDir.getOpposite(), x);
                for (int y = 0; y < portalHeight - 1; y++) {
                    Location portalLocation = new Location(handleLocation, portalDir, y);
                    getWorldObj().setBlockState(portalLocation.toBlockPos(), HCBlocks.portalTeleporter.getDefaultState());

                    TilePortalTeleporter teleporter = (TilePortalTeleporter) portalLocation.getTE(getWorldObj());
                    teleporter.setRotation(baseDir, portalDir);
                    teleporter.setBase(this);
                }
            }
            for (Location fr : frames) {
                if (fr.getTE(getWorldObj()) != null) {
                    if (fr.getTE(getWorldObj()) instanceof TilePortalFrame) {
                        ((TilePortalFrame) fr.getTE(getWorldObj())).setActive(true);
                    }
                }
            }
        }
    }

    private void disablePortal() {
        if (baseDir != null) {
            Location bottomLeft = new Location(getPos(), baseDir, (portalWidth / 2));
            bottomLeft.offset(baseDir.getOpposite(), 1);
            bottomLeft.offset(portalDir, 1);
            for (int x = 0; x <= portalWidth - 2; x++) {
                Location handleLocation = new Location(bottomLeft, baseDir.getOpposite(), x);
                for (int y = 0; y < portalHeight - 1; y++) {
                    Location portalLocation = new Location(handleLocation, portalDir, y);
                    getWorldObj().setBlockToAir(portalLocation.toBlockPos());
                }
            }
            for (Location fr : frames) {
                if (fr.getTE(getWorldObj()) != null) {
                    ((TilePortalFrame) fr.getTE(getWorldObj())).setActive(false);
                }
            }
        }
    }

    public boolean getIsActive() {
        return getWorldObj().getBlockState(getPos()).getValue(Properties.ACTIVE);
    }

    public void setIsActive(boolean active) {
        getWorldObj().setBlockState(pos, getBlockType().getDefaultState().withProperty(Properties.ACTIVE, active));
    }

    public String getIPString() {
        if (ip == 0 && !getWorldObj().isRemote) {
            genNewIP();
        }
        return IPs.longToIp(ip);
    }

    public Long getIPLong() {
        return ip;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return linkingCard;
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        ItemStack tempStack = linkingCard.copy();
        linkingCard = null;
        return tempStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index, 1);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        if (var1 == 0) {
            linkingCard = var2;
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return (var2.getItem() instanceof ItemIPCard);
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

    public boolean getIsValid() {
        return portalFormed;
    }

    public Location getTarget() {
        //First: see if there's an item in the inventory:
        if (linkingCard == null) return null;
        //Next, see if it linked:
        if (linkingCard.getTagCompound() == null) return null;
        if (linkingCard.getTagCompound().getLong("linked") == 0) return null;
        long linked = linkingCard.getTagCompound().getLong("linked");
        if (Hydraulicraft.ipList.getLocation(linked) == null) return null;
        return new Location(Hydraulicraft.ipList.getLocation(linked), portalDir, 1);
    }

    public void dye(int i) {
        colorIndex = i;
        markDirty();
        getWorldObj().markBlockForUpdate(getPos());
        //Update frames
        if (portalFormed) {
            for (Location fr : frames) {
                if (fr.getTE(getWorldObj()) != null) {
                    ((TilePortalFrame) fr.getTE(getWorldObj())).dye(i);
                }
            }
        }
    }

    public int getDye() {
        return colorIndex;
    }

    public Location getBlockLocation() {
        return new Location(getPos());
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return !side.equals(EnumFacing.UP);
    }

    @Override
    public float workFunction(boolean simulate, EnumFacing from) {
        if (from.equals(EnumFacing.UP) && getIsActive()) {
            if(getTarget() != null) {
                if (getPressure(EnumFacing.UP) >= (HCConfig.INSTANCE.getInt("portalmBarUsagePerTickPerBlock") * getBlockLocation().getDifference(getTarget()))) {
                    if (getIsActive()) {
                        return HCConfig.INSTANCE.getInt("portalmBarUsagePerTickPerBlock") * getBlockLocation().getDifference(getTarget());
                    }
                } else {
                    if (getIsActive()) {
                        disablePortal();
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public boolean canWork(EnumFacing dir) {
        return dir.equals(EnumFacing.UP) && getTarget() != null;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
        //return super.shouldRefresh(world, pos, oldState, newSate);
    }
}

