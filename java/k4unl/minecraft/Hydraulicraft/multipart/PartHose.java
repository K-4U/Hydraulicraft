package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.api.*;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.tileEntities.PressureNetwork;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.ICustomNetwork;
import mcmultipart.MCMultiPartMod;
import mcmultipart.block.TileMultipart;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;


public class PartHose extends Multipart implements ISlottedPart, ITickable, IOccludingPart, IHydraulicTransporter, ICustomNetwork, ITieredBlock {
    public static  AxisAlignedBB[] boundingBoxes = new AxisAlignedBB[14];
    private static int             expandBounds  = -1;
    
    private IBaseClass baseHandler;
    private byte    connectionCache      = 0;
    private boolean neighborBlockChanged = false;

    private static float pixel = 1.0F / 16F;
    
    private PressureTier tier = PressureTier.INVALID;
    
    static {
        float center = 0.5F;
        float offset = pixel * 2;
        //float getFrontOffsetY() = 0.2F;
        //float getFrontOffsetZ() = 0.2F;
        float centerFirst = center - offset;
        float centerSecond = center + offset;
        double w = pixel * 2;
        boundingBoxes[6] = new AxisAlignedBB(centerFirst - w, centerFirst - w, centerFirst - w, centerFirst + w, centerFirst + w, centerFirst + w);
        boundingBoxes[13] = new AxisAlignedBB(centerSecond - w, centerSecond - w, centerSecond - w, centerSecond + w, centerSecond + w, centerSecond + w);
        
        int i = 0;
        for (EnumFacing dir : EnumFacing.VALUES) {
            double xMin1 = (dir.getFrontOffsetX() < 0 ? 0.0 : (dir.getFrontOffsetX() == 0 ? centerFirst - w : centerFirst + w));
            double xMax1 = (dir.getFrontOffsetX() > 0 ? 1.0 : (dir.getFrontOffsetX() == 0 ? centerFirst + w : centerFirst - w));

            double yMin1 = (dir.getFrontOffsetY() < 0 ? 0.0 : (dir.getFrontOffsetY() == 0 ? centerFirst - w : centerFirst + w));
            double yMax1 = (dir.getFrontOffsetY() > 0 ? 1.0 : (dir.getFrontOffsetY() == 0 ? centerFirst + w : centerFirst - w));

            double zMin1 = (dir.getFrontOffsetZ() < 0 ? 0.0 : (dir.getFrontOffsetZ() == 0 ? centerFirst - w : centerFirst + w));
            double zMax1 = (dir.getFrontOffsetZ() > 0 ? 1.0 : (dir.getFrontOffsetZ() == 0 ? centerFirst + w : centerFirst - w));

            double xMin2 = (dir.getFrontOffsetX() < 0 ? 0.0 : (dir.getFrontOffsetX() == 0 ? centerSecond - w : centerSecond + w));
            double xMax2 = (dir.getFrontOffsetX() > 0 ? 1.0 : (dir.getFrontOffsetX() == 0 ? centerSecond + w : centerSecond - w));

            double yMin2 = (dir.getFrontOffsetY() < 0 ? 0.0 : (dir.getFrontOffsetY() == 0 ? centerSecond - w : centerSecond + w));
            double yMax2 = (dir.getFrontOffsetY() > 0 ? 1.0 : (dir.getFrontOffsetY() == 0 ? centerSecond + w : centerSecond - w));

            double zMin2 = (dir.getFrontOffsetZ() < 0 ? 0.0 : (dir.getFrontOffsetZ() == 0 ? centerSecond - w : centerSecond + w));
            double zMax2 = (dir.getFrontOffsetZ() > 0 ? 1.0 : (dir.getFrontOffsetZ() == 0 ? centerSecond + w : centerSecond - w));

            boundingBoxes[i] = new AxisAlignedBB(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
            boundingBoxes[i + 7] = new AxisAlignedBB(xMin2, yMin2, zMin2, xMax2, yMax2, zMax2);
            i++;
        }
    }

    public void preparePlacement(int itemDamage) {
        tier = PressureTier.fromOrdinal(itemDamage);

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (getHandler() != null) {
            getHandler().validateI();
            getHandler().readFromNBTI(tagCompound);
        }
        tier = PressureTier.fromOrdinal(tagCompound.getInteger("tier"));
        connectionCache = tagCompound.getByte("connectionCache");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        getHandler().writeToNBTI(tagCompound);
        tagCompound.setInteger("tier", tier.toInt());
        tagCompound.setByte("connectionCache", connectionCache);
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {
        super.writeUpdatePacket(buf);

        NBTTagCompound mainCompound = new NBTTagCompound();
        NBTTagCompound handlerCompound = new NBTTagCompound();
        getHandler().writeToNBTI(handlerCompound);

        mainCompound.setTag("handler", handlerCompound);
        mainCompound.setInteger("tier", getTier().toInt());
        mainCompound.setByte("connectionCache", connectionCache);

        buf.writeNBTTagCompoundToBuffer(mainCompound);
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {
        super.readUpdatePacket(buf);

        NBTTagCompound mainCompound = null;
        try {
            mainCompound = buf.readNBTTagCompoundFromBuffer();
            NBTTagCompound handlerCompound = mainCompound.getCompoundTag("handler");
            tier = PressureTier.fromOrdinal(mainCompound.getInteger("tier"));
            connectionCache = mainCompound.getByte("connectionCache");

            getHandler().readFromNBTI(handlerCompound);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EnumSet<PartSlot> getSlotMask() {
        return EnumSet.of(PartSlot.CENTER);
    }

    @Override
    public boolean occlusionTest(IMultipart part) {
        return OcclusionHelper.defaultOcclusionTest(this, part);
    }

    @Override
    public void onFluidLevelChanged(int old) {
    }

    @Override
    public boolean canConnectTo(EnumFacing side) {
        return internalConnects(side);
    }

    public PressureTier getTier() {
        return tier;
    }

    @Override
    public IBaseClass getHandler() {
        if (baseHandler == null) baseHandler = HCApi.getInstance().getBaseClass(this, 2 * (getTier().toInt() + 1));
        return baseHandler;
    }

    @Override
    public void update() {
        if (getHandler() != null) {
            //This should never happen that this is null! :|
            getHandler().updateEntityI();
        } else {
            Log.error("PartHose does not have a handler!");
        }
        if (getWorld() != null) {
            if (getWorld().isRemote && getWorld().getTotalWorldTime() % 20 == 0) {
                if (!getHandler().isOilStored() && getHandler().getPressure(EnumFacing.UP) > 0) {
                    //Do the particle thingie!
                    //world.spawnParticle("cloud", x, y, z, d3, d4, d5);
                    Random random = new Random();
                    if (random.nextDouble() < 0.4) {
                        double x = getPos().getX() + (((random.nextDouble() * .2) - .1) + 0.5);
                        double z = getPos().getZ() + (((random.nextDouble() * .2) - .1) + 0.5);
                        getWorld().spawnParticle(EnumParticleTypes.DRIP_WATER, x, getPos().getZ(), z + 0.0, 0.0D, 0.0D, 0.0D);
                    }
                    //PacketPipeline.instance.sendToAllAround(new PacketSpawnParticle("dripwater", x() + .5, y() + .5, z() + .5, 0.0D, 0.0D, 0.0D),
                    //  getWorld());
                }
            }
        }
        if(getWorld() != null) {
            if(!getWorld().isRemote) {
                if (neighborBlockChanged) {
                    updateNeighborInfo(true);
                    neighborBlockChanged = false;
                }
            }
        }
    }

    @Override
    public void updateNetwork(float oldPressure) {
        PressureNetwork newNetwork = null;
        PressureNetwork foundNetwork;
        PressureNetwork endNetwork = null;
        //This block can merge networks!
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (!isConnectedTo(dir)) {
                continue;
            }
            TileEntity ent = getWorld().getTileEntity(getPos().offset(dir));
            if (ent == null) continue;
            if (!connects(dir)) continue;
            foundNetwork = PressureNetwork.getNetworkInDir(getWorld(), getPos(), dir);

            if (foundNetwork != null) {

                if (endNetwork == null) {
                    endNetwork = foundNetwork;
                } else {
                    newNetwork = foundNetwork;
                }
            }

            if (newNetwork != null) {
                //Hmm.. More networks!? What's this!?
                //Log.info("Found an existing network (" + newNetwork.getRandomNumber() + ") @ " + x() + "," + y() + "," + z());
                endNetwork.mergeNetwork(newNetwork);
                newNetwork = null;
            }

        }

        if (endNetwork != null) {
            ((TileHydraulicBase) getHandler()).setNetwork(EnumFacing.UP, endNetwork);
            endNetwork.addMachine(this, oldPressure, EnumFacing.UP);
            //Log.info("Found an existing network (" + endNetwork.getRandomNumber() + ") @ " + x() + "," + y() + "," + z());
        } else {
            endNetwork = new PressureNetwork(this, oldPressure, EnumFacing.UP);
            ((TileHydraulicBase) getHandler()).setNetwork(EnumFacing.UP, endNetwork);
            //Log.info("Created a new network (" + endNetwork.getRandomNumber() + ") @ " + x() + "," + y() + "," + z());
        }
    }

    @Override
    public void onRemoved() {
        if (!getWorld().isRemote) {
            for (EnumFacing dir : EnumFacing.VALUES) {
                if (connects(dir)) {
                    if (getNetwork(dir) != null) {
                        getNetwork(dir).removeMachine(this);
                    }
                }
            }
        }
    }
    @Override
    public float getStrength(EntityPlayer player, PartMOP hit) {
        return 8F;
    }


    @Override
    public PressureNetwork getNetwork(EnumFacing dir) {
        return ((TileHydraulicBase) getHandler()).getNetwork(dir);
    }

    @Override
    public void setNetwork(EnumFacing side, PressureNetwork toSet) {
        ((TileHydraulicBase) getHandler()).setNetwork(side, toSet);
    }


    @Override
    public IBlockState getExtendedState(IBlockState state) {
        return state
                .withProperty(Properties.DOWN, connects(EnumFacing.DOWN))
                .withProperty(Properties.UP, connects(EnumFacing.UP))
                .withProperty(Properties.NORTH, connects(EnumFacing.NORTH))
                .withProperty(Properties.SOUTH, connects(EnumFacing.SOUTH))
                .withProperty(Properties.WEST, connects(EnumFacing.WEST))
                .withProperty(Properties.EAST, connects(EnumFacing.EAST));
    }

    @Override
    public BlockState createBlockState() {
        return new BlockState(MCMultiPartMod.multipart,
                Properties.DOWN,
                Properties.UP,
                Properties.NORTH,
                Properties.SOUTH,
                Properties.WEST,
                Properties.EAST);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
        addSelectionBoxes(list);
        return list.get(0);
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> list) {
        list.add(AxisAlignedBB.fromBounds(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {
        list.add(boundingBoxes[6]);
        list.add(boundingBoxes[13]);
        for (EnumFacing f : EnumFacing.VALUES) {
            if (connects(f)) {
                list.add(boundingBoxes[f.ordinal()]);
                list.add(boundingBoxes[f.ordinal()+7]);
            }
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if (boundingBoxes[6].intersectsWith(mask)) {
            list.add(boundingBoxes[6]);
        }
        if(boundingBoxes[13].intersectsWith(mask)){
            list.add(boundingBoxes[13]);
        }
        for (EnumFacing f : EnumFacing.VALUES) {
            if (connects(f)) {
                if (boundingBoxes[f.ordinal()].intersectsWith(mask)) {
                    list.add(boundingBoxes[f.ordinal()]);
                }
                if (boundingBoxes[f.ordinal()+6].intersectsWith(mask)) {
                    list.add(boundingBoxes[f.ordinal()+7]);
                }
            }
        }
    }


    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer) {
        return layer == EnumWorldBlockLayer.CUTOUT;
    }

    // Tile logic

    public TileEntity getNeighbourTile(EnumFacing side) {
        return side != null ? getWorld().getTileEntity(getPos().offset(side)) : null;
    }

    private boolean internalConnects(EnumFacing side) {
        ISlottedPart part = getContainer().getPartInSlot(PartSlot.getFaceSlot(side));
        if (part instanceof IMicroblock.IFaceMicroblock) {
            if (!((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
                return false;
            }
        }

        //Is there a part blocking us?
        if (!OcclusionHelper.occlusionTest(getContainer().getParts(), this, boundingBoxes[side.ordinal()])) {
            return false;
        }

        //Is there a fluidpipe in the other block?
        if (MultipartHandler.getPartHose(getWorld(), getPos().offset(side), side.getOpposite()) != null) {
            return true;
        }


        TileEntity tile = getNeighbourTile(side);
        if (tile instanceof IHydraulicMachine) {
            return true;
        }

        return false;
    }

    private void updateConnections(EnumFacing side) {
        if (side != null) {
            connectionCache &= ~(1 << side.ordinal());

            if (internalConnects(side)) {
                TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
                if (tileEntity instanceof TileMultipart) {
                    PartHose pipe = MultipartHandler.getHose(((TileMultipart)tileEntity).getPartContainer());
                    if (pipe != null && !pipe.internalConnects(side.getOpposite())) {
                        return;
                    }
                }
                connectionCache |= 1 << side.ordinal();
            }
        } else {
            for (EnumFacing facing : EnumFacing.VALUES) {
                updateConnections(facing);
            }
        }
    }

    public boolean connects(EnumFacing side) {
        return (connectionCache & (1 << side.ordinal())) != 0;
    }

    private void updateNeighborInfo(boolean sendPacket) {
        if (!getWorld().isRemote) {
            byte oc = connectionCache;

            for (EnumFacing dir : EnumFacing.VALUES) {
                updateConnections(dir);
            }

            if (sendPacket && connectionCache != oc) {
                sendUpdatePacket();
            }
        }
    }


    @Override
    public void onAdded() {
        updateNeighborInfo(false);
    }

    @Override
    public void onLoaded() {
        neighborBlockChanged = true;
    }

    @Override
    public void onNeighborBlockChange(Block block) {
        neighborBlockChanged = true;
    }

    @Override
    public boolean isConnectedTo(EnumFacing dir) {
        return connects(dir);
    }
}

