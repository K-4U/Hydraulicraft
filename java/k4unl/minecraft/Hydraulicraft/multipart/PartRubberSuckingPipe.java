package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.LocalOcclusionHelper;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.tileEntities.worldgen.TileRubberWood;
import mcmultipart.MCMultiPartMod;
import mcmultipart.block.TileMultipartContainer;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class PartRubberSuckingPipe extends Multipart implements ISlottedPart, ITickable, INormallyOccludingPart {

    public static  AxisAlignedBB[] boundingBoxes = new AxisAlignedBB[7];
    private static float           pixel         = 1.0F / 16F;
    private static int             expandBounds  = -1;

    static {
        float center = 0.5F;
        double w = pixel * 2;
        boundingBoxes[6] = new AxisAlignedBB(center - w, center - w, center - w, center + w, center + w, center + w);

        int i = 0;
        for (EnumFacing dir : EnumFacing.VALUES) {
            double xMin1 = (dir.getFrontOffsetX() < 0 ? 0.0 : (dir.getFrontOffsetX() == 0 ? center - w : center + w));
            double xMax1 = (dir.getFrontOffsetX() > 0 ? 1.0 : (dir.getFrontOffsetX() == 0 ? center + w : center - w));

            double yMin1 = (dir.getFrontOffsetY() < 0 ? 0.0 : (dir.getFrontOffsetY() == 0 ? center - w : center + w));
            double yMax1 = (dir.getFrontOffsetY() > 0 ? 1.0 : (dir.getFrontOffsetY() == 0 ? center + w : center - w));

            double zMin1 = (dir.getFrontOffsetZ() < 0 ? 0.0 : (dir.getFrontOffsetZ() == 0 ? center - w : center + w));
            double zMax1 = (dir.getFrontOffsetZ() > 0 ? 1.0 : (dir.getFrontOffsetZ() == 0 ? center + w : center - w));

            boundingBoxes[i] = new AxisAlignedBB(xMin1, yMin1, zMin1, xMax1, yMax1, zMax1);
            i++;
        }
    }

    private byte connectionCache = 0;
    private boolean neighborBlockChanged;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        connectionCache = tagCompound.getByte("connectionCache");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        tagCompound.setByte("connectionCache", connectionCache);
    }

    @Override
    public void writeUpdatePacket(PacketBuffer buf) {

        super.writeUpdatePacket(buf);

        NBTTagCompound mainCompound = new NBTTagCompound();
        if (getWorld() != null && !getWorld().isRemote) {
            mainCompound.setByte("connectionCache", connectionCache);
        }

        buf.writeNBTTagCompoundToBuffer(mainCompound);
    }

    @Override
    public void readUpdatePacket(PacketBuffer buf) {

        super.readUpdatePacket(buf);
        NBTTagCompound mainCompound = null;
        try {
            mainCompound = buf.readNBTTagCompoundFromBuffer();
            connectionCache = mainCompound.getByte("connectionCache");
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
    public void update() {

        if (getWorld() != null) {
            if (!getWorld().isRemote) {

                if (neighborBlockChanged) {
                    updateNeighborInfo(true);
                    neighborBlockChanged = false;
                }
            }
        }
    }

    @Override
    public void onRemoved() {

        super.onRemoved();
    }

    @Override
    public float getStrength(EntityPlayer player, PartMOP hit) {

        return 8F;
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
    public BlockStateContainer createBlockState() {

        return new BlockStateContainer(MCMultiPartMod.multipart,
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

        //TODO: CHANGE ME
        list.add(new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> list) {

        list.add(boundingBoxes[6]);
        for (EnumFacing f : EnumFacing.VALUES) {
            if (connects(f)) {
                list.add(boundingBoxes[f.ordinal()]);
            }
        }
    }

    @Override
    public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        if (boundingBoxes[6].intersectsWith(mask)) {
            list.add(boundingBoxes[6]);
        }
        for (EnumFacing f : EnumFacing.VALUES) {
            if (connects(f)) {
                if (boundingBoxes[f.ordinal()].intersectsWith(mask)) {
                    list.add(boundingBoxes[f.ordinal()]);
                }
            }
        }
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
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
        if (!LocalOcclusionHelper.occlusionTest(getContainer().getParts(), this, boundingBoxes[side.ordinal()])) {
            return false;
        }

        //Is there a rubber pipe in the other block?
        if (MultipartHandler.getRubberSuckingPipe(getWorld(), getPos().offset(side), side.getOpposite()) != null) {
            return true;
        }

        //TODO: More detection magic for rubber
        TileEntity tile = getNeighbourTile(side);
        if(tile instanceof TileRubberWood){
            TileRubberWood rubberWood = ((TileRubberWood)tile);
            return (rubberWood.getFacing().getOpposite().equals(side)) && rubberWood.hasSpot();
        }
        return false;

    }

    private void updateConnections(EnumFacing side) {

        if (side != null) {
            connectionCache &= ~(1 << side.ordinal());

            if (internalConnects(side)) {
                TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
                if (tileEntity instanceof TileMultipartContainer) {
                    PartRubberSuckingPipe pipe = MultipartHandler.getRubberSuckingPipe(((TileMultipartContainer) tileEntity).getPartContainer());
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
    public ResourceLocation getModelPath() {
        return new ResourceLocation("hydcraft", "RubberSuckingPipe");
    }

    @Override
    public ItemStack getPickBlock(EntityPlayer player, PartMOP hit) {

        return new ItemStack(MultipartHandler.itemPartRubberSuckingPipe, 1);
    }
}
