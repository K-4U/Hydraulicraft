package k4unl.minecraft.Hydraulicraft.tileEntities;

import k4unl.minecraft.Hydraulicraft.api.*;
import k4unl.minecraft.Hydraulicraft.blocks.IHydraulicMultiBlock;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.multipart.MultipartHandler;
import k4unl.minecraft.Hydraulicraft.multipart.PartHose;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork.networkEntry;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHydraulicStorage;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IHydraulicStorageWithTank;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.tileEntities.storage.TileHydraulicPressureVat;
import k4unl.minecraft.k4lib.lib.Location;
import mcmultipart.block.TileMultipart;
import mcmultipart.multipart.Multipart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileHydraulicBase extends TileEntity implements IBaseClass, ITickable {
    protected List<EnumFacing> connectedSides;
    protected PressureNetwork      pNetwork;
    private boolean _isOilStored      = false;
    private int     fluidLevelStored  = 0;
    private boolean isRedstonePowered = false;
    private float   pressure          = 0F;
    private boolean           isMultipart;
    private World             tWorld;
    private Location          blockLocation;
    private Multipart         tMp;
    private TileEntity        tTarget;
    private IHydraulicMachine target;
    private boolean           hasOwnFluidTank;
    private boolean firstUpdate         = true;
    private float   oldPressure         = 0f;
    private boolean shouldUpdateNetwork = true;
    private boolean shouldUpdateFluid   = false;
    //private PressureTier pressureTier;
    private int maxStorage = 0;
    private int fluidInNetwork;
    private int networkCapacity;
    private boolean shouldUpdateWorld = false;

    /**
     * @param _maxStorage The max ammount of Fluid/10 this machine can store.
     */
    public TileHydraulicBase(int _maxStorage) {

        maxStorage = _maxStorage;
        connectedSides = new ArrayList<EnumFacing>();
    }

    public void init(TileEntity _target) {

        tTarget = _target;
        target = (IHydraulicMachine) _target;
        if (target instanceof TileHydraulicPressureVat) {
            hasOwnFluidTank = true;
        }
        tWorld = _target.getWorld();
    }

    public void init(Multipart _target) {

        tMp = _target;
        tTarget = null;
        target = (IHydraulicMachine) _target;
        isMultipart = true;
        tWorld = _target.getWorld();
        tWorld = _target.getWorld();
    }

    public IBaseClass getHandler() {

        return this;
    }

    public Location getBlockLocation() {

        if (blockLocation == null) {
            if (isMultipart) {
                blockLocation = new Location(tMp.getPos());
            } else {
                blockLocation = new Location(tTarget.getPos());
            }
        }
        return blockLocation;
    }

    public World getWorldObj() {

        if (tWorld == null) {
            if (isMultipart) {
                tWorld = tMp.getWorld();
            } else {
                if (tTarget != null) {
                    tWorld = tTarget.getWorld();
                }
            }
        }
        return tWorld;
    }

    public void redstoneChanged(boolean rsPowered) {

    }

    public void dropItemStackInWorld(ItemStack itemStack) {

        if (itemStack != null) {
            EntityItem ei = new EntityItem(getWorldObj());
            ei.setEntityItemStack(itemStack);
            ei.setPosition(getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ());
            getWorldObj().spawnEntityInWorld(ei);
        }
    }

    public boolean getRedstonePowered() {

        return getIsRedstonePowered();
    }

    public void setRedstonePowered(boolean isRedstonePowered) {
        this.isRedstonePowered = isRedstonePowered;
    }

    public void updateBlock() {

        if (getWorldObj() != null && !getWorldObj().isRemote) {
            if (isMultipart && tMp.getContainer() != null) {
                getWorldObj().markBlockForUpdate(getBlockLocation().toBlockPos());
            } else {
                getWorldObj().markBlockForUpdate(getBlockLocation().toBlockPos());
            }
        }
    }

    public void checkPressure(EnumFacing dir) {

        if (getWorldObj() == null) return;
        if (getWorldObj().isRemote) return;

        float newPressure = getPressure(dir);
        int compare = Float.compare(getMaxPressure(isOilStored(), dir), newPressure);
        if (compare < 0 && getStored() > 0) {
            getWorldObj().createExplosion(null, getBlockLocation().getX(), getBlockLocation().getY(), getBlockLocation().getZ(),
                    0.6F + ((getMaxPressure(isOilStored(), null) / newPressure)), true);
            if (isOilStored()) {
                getWorldObj().setBlockState(getBlockLocation().toBlockPos(), Fluids.fluidHydraulicOilBlock.getDefaultState());
            } else {
                getWorldObj().setBlockState(getBlockLocation().toBlockPos(), FluidRegistry.WATER.getBlock().getDefaultState());
            }
        }
    }

    public float getMaxPressure(boolean isOil, EnumFacing from) {
        if (isOil) {
            switch (getPressureTier()) {
                case LOWPRESSURE:
                    return Constants.MAX_MBAR_OIL_TIER_1;
                case MEDIUMPRESSURE:
                    return Constants.MAX_MBAR_OIL_TIER_2;
                case HIGHPRESSURE:
                case INVALID:
                    return Constants.MAX_MBAR_OIL_TIER_3;
            }
        } else {
            switch (getPressureTier()) {
                case LOWPRESSURE:
                    return Constants.MAX_MBAR_WATER_TIER_1;
                case MEDIUMPRESSURE:
                    return Constants.MAX_MBAR_WATER_TIER_2;
                case HIGHPRESSURE:
                case INVALID:
                    return Constants.MAX_MBAR_WATER_TIER_3;
            }
        }
        return 0;
    }

    /*!
     * @author Koen Beckers
     * @date 15-12-2013
     * Will return how much liquid this block stores
     * Will be used to calculate the pressure all over the network.
     */
    public int getStored() {
        if (hasOwnFluidTank) {
            if (isMultipart) {
                return ((IHydraulicStorageWithTank) tMp).getStored();
            } else {
                return ((IHydraulicStorageWithTank) target).getStored();
            }
        } else {
            return fluidLevelStored;
        }
    }

    public void setStored(int i, boolean isOil, boolean doNotify) {
        if (getWorldObj() == null) return;
        if (getWorldObj().isRemote) return;
        if (i < 0)
            i = 0;
        _isOilStored = isOil;
        if (hasOwnFluidTank) {
            if (isMultipart) {
                ((IHydraulicStorageWithTank) tMp).setStored(i, isOil);
            } else {
                ((IHydraulicStorageWithTank) target).setStored(i, isOil);
            }
        } else {
            target.onFluidLevelChanged(fluidLevelStored);

            if (fluidLevelStored != i && doNotify) {
                //if new = 0 that probably means me setting it like that..
                //shouldUpdateFluid = true;
            }
            fluidLevelStored = i;
        }
        markDirty();
        updateBlock();
    }

    public boolean isOilStored() {
        return _isOilStored;
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
        return new S35PacketUpdateTileEntity(getBlockLocation().toBlockPos(), 4, tagCompound);
    }
/*

COULD BE REMOVED?
    private IHydraulicMachine getMachine(EnumFacing dir) {
        if (getWorldObj() == null) return null;

        /*int x = getBlockLocation().getX() + dir.offsetX;
        int y = getBlockLocation().getY() + dir.offsetY;
        int z = getBlockLocation().getZ() + dir.offsetZ;*//*
        Block block = getWorldObj().getBlockState(getBlockLocation().toBlockPos().offset(dir)).getBlock();
        if (block instanceof BlockAir) {
            return null;
        }

        TileEntity t = getWorldObj().getTileEntity(getBlockLocation().toBlockPos().offset(dir));
        if (t instanceof IHydraulicMachine) {
            if (((IHydraulicMachine) t).canConnectTo(dir.getOpposite()))
                return (IHydraulicMachine) t;
        } else if (t instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) t).getPartContainer())) {
            if (MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer()).isConnectedTo(dir.getOpposite())) {
                return MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer());
            }
        }
        return null;
    }
*/
    private List<IHydraulicMachine> getMachineList(List<IHydraulicMachine> list, EnumFacing dir) {
        if (getWorldObj() == null) return list;

        Block block = getWorldObj().getBlockState(getBlockLocation().toBlockPos().offset(dir)).getBlock();
        if (block instanceof BlockAir) {
            return null;
        }

        TileEntity t = getWorldObj().getTileEntity(getBlockLocation().toBlockPos().offset(dir));
        if (t instanceof IHydraulicMachine) {
            if (((IHydraulicMachine) t).canConnectTo(dir.getOpposite()) && !list.contains(t))
                list.add((IHydraulicMachine) t);
        }else if (t instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) t).getPartContainer())) {
            if (MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer()).isConnectedTo(dir.getOpposite()) && !list.contains(t)) {
                list.add(MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer()));
            }
        }
        return list;
    }

    public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList) {
        if (getNetwork(EnumFacing.UP) == null) {
            return mainList;
        }

        List<networkEntry> entryList;
        entryList = getNetwork(EnumFacing.UP).getMachines();

        for (networkEntry entry : entryList) {
            Location loc = entry.getLocation();
            TileEntity ent = getWorldObj().getTileEntity(loc.toBlockPos());
            if (ent instanceof IHydraulicMachine) {
                IHydraulicMachine machine = (IHydraulicMachine) ent;
                mainList.add(machine);
            }
        }

        return mainList;
    }

    public List<IHydraulicMachine> getConnectedBlocks(List<IHydraulicMachine> mainList, boolean chain) {
        List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (isMultipart) {
                if (((PartHose) tMp).isConnectedTo(dir)) {
                    machines = getMachineList(machines, dir);
                }
            } else {
                machines = getMachineList(machines, dir);
            }
        }

        List<IHydraulicMachine> callList = new ArrayList<IHydraulicMachine>();

        for (IHydraulicMachine machineEntity : machines) {
            if (!mainList.contains(machineEntity)) {
                if (isMultipart) {
                    mainList.add(machineEntity);
                    //callList.add(machineEntity);
                    //chain = true;
                }
                if (machineEntity instanceof IHydraulicTransporter) {
                    mainList.add(machineEntity);
                    callList.add(machineEntity);
                    chain = true;
                }
                if (machineEntity instanceof TileHydraulicValve) {
                    IHydraulicMachine target = ((TileHydraulicValve) machineEntity).getTarget();
                    if (target != null) {
                        mainList.add(target);
                        if (target instanceof IHydraulicMultiBlock) {
                            List<TileHydraulicValve> valves = ((IHydraulicMultiBlock) target).getValves();
                            for (TileHydraulicValve valve : valves) {
                                if (!valve.equals(machineEntity)) {
                                    callList.add(valve);
                                }
                            }
                            chain = true;
                        }
                    }
                }
            }
        }
        //Only go trough transporter items.
        if (chain) {
            for (IHydraulicMachine machineEntity : callList) {
                //if(machineEntity instanceof TileTransporter){
                List<IHydraulicMachine> tempList;
                tempList = ((TileHydraulicBase) machineEntity.getHandler()).getConnectedBlocks(mainList);
                mainList = Functions.mergeList(tempList, mainList);
                //}
            }
        }

        return mainList;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        fluidLevelStored = tagCompound.getInteger("fluidLevelStored");
        _isOilStored = tagCompound.getBoolean("isOilStored");
        oldPressure = tagCompound.getFloat("oldPressure");
        maxStorage = tagCompound.getInteger("maxStorage");
        networkCapacity = tagCompound.getInteger("networkCapacity");
        fluidInNetwork = tagCompound.getInteger("fluidInNetwork");

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            shouldUpdateNetwork = tagCompound.getBoolean("shouldUpdateNetwork");
            if (shouldUpdateNetwork) {
                updateNetworkOnNextTick(oldPressure);
            }
            for (EnumFacing dir : EnumFacing.VALUES) {
                if (getNetwork(dir) != null) {
                    getNetwork(dir).readFromNBT(tagCompound.getCompoundTag("network" + dir.toString()));
                } else {
                    //get pressure from the network in this dir:
                    float pressureT = tagCompound.getCompoundTag("network" + dir.toString()).getFloat("pressure");
                    setNetwork(dir, new PressureNetwork(tagCompound.getCompoundTag("network" + dir.ordinal())));
                    setWorldOnNextTick();
                }
            }
        } else if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            pressure = tagCompound.getFloat("pressure");
        }

        setRedstonePowered(tagCompound.getBoolean("isRedstonePowered"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        if (!isMultipart && getClass() != TileHydraulicBase.class) {
            super.writeToNBT(tagCompound);
        }
        tagCompound.setInteger("fluidLevelStored", fluidLevelStored);
        tagCompound.setBoolean("isOilStored", _isOilStored);
        tagCompound.setBoolean("isRedstonePowered", getIsRedstonePowered());
        tagCompound.setInteger("maxStorage", maxStorage);


        if (getWorldObj() != null && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            tagCompound.setBoolean("shouldUpdateNetwork", shouldUpdateNetwork);

            if (getNetwork(EnumFacing.UP) != null) {
                tagCompound.setFloat("oldPressure", getNetwork(EnumFacing.UP).getPressure());
                tagCompound.setFloat("pressure", getNetwork(EnumFacing.UP).getPressure());
            }
            for (EnumFacing dir : EnumFacing.VALUES) {
                if (getNetwork(dir) != null) {
                    NBTTagCompound pNetworkCompound = new NBTTagCompound();
                    getNetwork(dir).writeToNBT(pNetworkCompound);
                    tagCompound.setTag("network" + dir.toString(), pNetworkCompound);
                }
            }
            if (pNetwork != null) {
                tagCompound.setInteger("networkCapacity", getNetwork(EnumFacing.UP).getFluidCapacity());
                tagCompound.setInteger("fluidInNetwork", getNetwork(EnumFacing.UP).getFluidInNetwork());
            }
        }
    }

    protected TileEntity getTileEntity(int x, int y, int z) {
        return getWorldObj().getTileEntity(new BlockPos(x, y, z));
    }

    public void checkRedstonePower() {
        boolean isIndirectlyPowered = (getWorldObj().isBlockIndirectlyGettingPowered(getBlockLocation().toBlockPos()) > 0);
        if (isIndirectlyPowered && !getIsRedstonePowered()) {
            setRedstonePowered(true);
            this.redstoneChanged(getIsRedstonePowered());
        } else if (getIsRedstonePowered() && !isIndirectlyPowered) {
            setRedstonePowered(false);
            this.redstoneChanged(getIsRedstonePowered());
        }
    }

    @Override
    public void update() {
        if (firstUpdate && tWorld != null && !tWorld.isRemote) {
            firstUpdate = false;
            shouldUpdateNetwork = true;
            firstTick();
            checkRedstonePower();
        }
        if (getWorldObj() != null) {
            if (!getWorldObj().isRemote) {

                if (shouldUpdateNetwork) {
                    shouldUpdateNetwork = false;
                    if (tMp instanceof ICustomNetwork || tTarget instanceof ICustomNetwork) {
                        if (isMultipart) {
                            ((ICustomNetwork) tMp).updateNetwork(oldPressure);
                        } else {
                            ((ICustomNetwork) tTarget).updateNetwork(oldPressure);
                        }
                    } else {
                        updateNetwork(oldPressure);
                    }
                }
                if (shouldUpdateFluid && getWorldObj().getTotalWorldTime() % 5 == 0 && getNetwork(EnumFacing.UP) != null) {
                    shouldUpdateFluid = false;
                    getNetwork(EnumFacing.UP).updateFluid(target);
                }

                if (getWorldObj().getTotalWorldTime() % 10 == 0) {
                    updateBlock();
                }

                for (EnumFacing dir : EnumFacing.VALUES) {
                    if (getNetwork(dir) != null) {
                        if (shouldUpdateWorld) {
                            getNetwork(dir).setWorld(getWorldObj());
                        }
                        if (getWorldObj().getTotalWorldTime() % 2 == 0 && HCConfig.INSTANCE.getBool("explosions")) {
                            checkPressure(dir);
                        }

                        if (tTarget instanceof IHydraulicConsumer) {
                            IHydraulicConsumer consumer = (IHydraulicConsumer) tTarget;
                            if (consumer.canWork(dir)) {
                                float less = consumer.workFunction(true, dir);
                                if (getPressure(dir) >= less && less > 0) {
                                    less = consumer.workFunction(false, dir);
                                    float newPressure = getPressure(dir) - less;
                                    updateBlock();
                                    setPressure(newPressure, dir);

                                    //So.. the water in this block should be going down a bit.
                                    if (!isOilStored()) {
                                        setStored((int) (getStored() - (less * Constants.USING_WATER_PENALTY)), false, true);
                                        shouldUpdateFluid = true;
                                    }
                                }
                            }
                        } else if (tTarget instanceof IHydraulicGenerator) {
                            IHydraulicGenerator gen = (IHydraulicGenerator) tTarget;
                            if (gen.canWork(dir)) {
                                gen.workFunction(dir);
                            }
                        } else if (tTarget instanceof IHydraulicStorage) {
                            if (getWorldObj().getTotalWorldTime() % 40 == 0 && dir.equals(EnumFacing.UP)) {
                                //Functions.checkAndFillSideBlocks(tTarget.worldObj, tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setPressure(float newPressure, EnumFacing dir) {
        getNetwork(dir).setPressure(newPressure);
    }

    @Override
    public void setIsOilStored(boolean b) {
        _isOilStored = b;
        markDirty();
    }

    private IHydraulicMachine isValidMachine(EnumFacing dir) {

        TileEntity t = getWorldObj().getTileEntity(getBlockLocation().toBlockPos().offset(dir));
        if (t instanceof IHydraulicMachine) {
            if (((IHydraulicMachine) t).canConnectTo(dir.getOpposite()))
                return (IHydraulicMachine) t;
        }else if (t instanceof TileMultipart && MultipartHandler.hasTransporter(((TileMultipart) t).getPartContainer())) {
            if (MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer()).isConnectedTo(dir.getOpposite())) {
                return MultipartHandler.getTransporter(((TileMultipart) t).getPartContainer());
            }
        }
        return null;
    }

    private List<IHydraulicMachine> getConnectedBlocks() {
        /*int x = getBlockLocation().getX();
        int y = getBlockLocation().getY();
		int z = getBlockLocation().getZ();*/
        List<IHydraulicMachine> machines = new ArrayList<IHydraulicMachine>();
        for (EnumFacing dir : EnumFacing.VALUES) {
            IHydraulicMachine isValid = null;
            if (isMultipart) {
                if (((PartHose) tMp).isConnectedTo(dir)) {
                    isValid = isValidMachine(dir);
                }
            } else {
                isValid = isValidMachine(dir);
            }
            if (isValid != null) {
                if (isValid instanceof TileHydraulicValve) {
                    machines.add(((TileHydraulicValve) isValid).getTarget());
                }
                machines.add(isValid);
            }
        }

        return machines;
    }

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void validateI() {
        validate();
    }

    @Override
    public void updateNetworkOnNextTick(float oldPressure) {
        if (getWorldObj() != null && !getWorldObj().isRemote) {
            shouldUpdateNetwork = true;
            this.pNetwork = null;
            this.oldPressure = oldPressure;
            //markDirty();
            updateBlock();
        }
    }

    private void setWorldOnNextTick() {
        shouldUpdateWorld = true;
    }

    @Override
    public void updateFluidOnNextTick() {
        if (!getWorldObj().isRemote) {
            shouldUpdateFluid = true;
        }
    }

    @Override
    public float getPressure(EnumFacing dir) {
        if (getWorldObj().isRemote) {
            return pressure;
        }
        if (getNetwork(dir) == null) {
            Log.error("Machine at " + getBlockLocation().printCoords() + " has no pressure network!");
            return 0;
        }
        return getNetwork(dir).getPressure();
    }

    @Override
    public int getMaxStorage() {
        return HCConfig.INSTANCE.getInt("maxFluidMultiplier") * maxStorage;
    }

    @Override
    public void setMaxStorage(int maxFluid) {
        maxStorage = maxFluid;
        markDirty();
    }

    public void firstTick() {

    }

    public int getFluidInNetwork(EnumFacing from) {
        if (getWorldObj().isRemote) {
            return fluidInNetwork;
        } else {
            return getNetwork(from).getFluidInNetwork();
        }
    }

    public int getFluidCapacity(EnumFacing from) {
        if (getWorldObj().isRemote) {
            if (networkCapacity > 0) {
                return networkCapacity;
            } else {
                return getMaxStorage();
            }
        } else {
            return getNetwork(from).getFluidCapacity();
        }
    }

    public void updateNetwork(float oldPressure) {
        PressureNetwork newNetwork = null;
        PressureNetwork foundNetwork;
        PressureNetwork endNetwork = null;
        //This block can merge networks!
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (target.canConnectTo(dir)) {
                foundNetwork = PressureNetwork.getNetworkInDir(getWorldObj(), getBlockLocation().toBlockPos(), dir);
                if (foundNetwork != null) {
                    if (endNetwork == null) {
                        endNetwork = foundNetwork;
                    } else {
                        newNetwork = foundNetwork;
                    }
                    connectedSides.add(dir);
                }

                if (newNetwork != null) {
                    //Hmm.. More networks!? What's this!?
                    endNetwork.mergeNetwork(newNetwork);
                    newNetwork = null;
                }
            }
        }

        if (endNetwork != null) {
            pNetwork = endNetwork;
            pNetwork.addMachine(target, oldPressure, EnumFacing.UP);
            //Log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        } else {
            pNetwork = new PressureNetwork(target, oldPressure, EnumFacing.UP);
            //Log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + xCoord + "," + yCoord + "," + zCoord);
        }
    }

    public PressureNetwork getNetwork(EnumFacing side) {
        return pNetwork;
    }

    public void setNetwork(EnumFacing side, PressureNetwork toSet) {
        pNetwork = toSet;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            for (EnumFacing dir : connectedSides) {
                if (getNetwork(dir) != null) {
                    getNetwork(dir).removeMachine(target);
                }
            }
        }
    }

    public void onBlockBreaks() {

    }

    @Override
    public PressureTier getPressureTier() {
        if (isMultipart) {
            if (tMp instanceof ITieredBlock) {
                return ((ITieredBlock) tMp).getTier();
            }
        } else {
            if (tTarget.getBlockType() instanceof ITieredBlock) {
                return ((ITieredBlock) tTarget.getBlockType()).getTier();
            } else if (tTarget.getBlockType() instanceof IMultiTieredBlock) {
                return ((IMultiTieredBlock) tTarget.getBlockType()).getTier(getWorldObj(), getBlockLocation().toBlockPos());
            }
        }
        return PressureTier.INVALID;
    }

    @Override
    public void readFromNBTI(NBTTagCompound tagCompound) {
        readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBTI(NBTTagCompound tagCompound) {
        writeToNBT(tagCompound);
    }

    @Override
    public void onDataPacketI(NetworkManager net,
                              S35PacketUpdateTileEntity packet) {
        onDataPacket(net, packet);
    }

    @Override
    public Packet getDescriptionPacketI() {
        return getDescriptionPacket();
    }

    @Override
    public void updateEntityI() {
        update();
    }

    @Override
    public void invalidateI() {
        invalidate();
    }

    @Override
    public void onChunkUnload() {
        markDirty();
    }

    @Override
    public void addPressureWithRatio(float pressureToAdd, EnumFacing from) {
        float gen = pressureToAdd * (getHandler().isOilStored() ? 1.0F : Constants.WATER_CONVERSION_RATIO);
        gen = gen * ((float) getFluidInNetwork(from) / (float) getFluidCapacity(from));

        if (Float.compare(gen + getPressure(from), getMaxPressure(getHandler().isOilStored(), from)) > 0) {
            //This means the pressure we are generating is too much!
            gen = getMaxPressure(getHandler().isOilStored(), from) - getPressure(from);
        }

        setPressure(getPressure(from) + gen, from);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        double xCoord = getBlockLocation().getX();
        double yCoord = getBlockLocation().getY();
        double zCoord = getBlockLocation().getZ();
        return AxisAlignedBB.fromBounds(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }

    public boolean getIsRedstonePowered() {
        return isRedstonePowered;
    }


}
