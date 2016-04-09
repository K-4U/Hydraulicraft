package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IRotateableBlock;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileMovingPane;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockMovingPane extends HydraulicBlockContainerBase implements ITieredBlock, IRotateableBlock {

    public BlockMovingPane() {

        super(Names.blockMovingPane, false);
        hasTextures = false;
        setDefaultState(this.blockState.getBaseState().withProperty(Properties.CHILD, false));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileMovingPane();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return true;
    }

    @Override
    public int quantityDropped(Random p_149745_1_) {

        return 0;
    }

    //TODO: FIX ME?
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {

        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(HCItems.itemMovingPane, 1));

        return ret;
    }


    // This block is called when block is broken and destroys the primary block.
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        TileMovingPane tileEntity = (TileMovingPane) worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            Location l;
            if (tileEntity.getIsPane()) {
                l = tileEntity.getParentLocation();
            } else {
                l = tileEntity.getChildLocation();
            }
            if (l != null) {
                worldIn.setBlockToAir(l.toBlockPos());
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    // This method checks if primary block exists.
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        TileMovingPane tileEntity = (TileMovingPane) worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.checkRedstonePower();
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {

        if (world.isRemote) return false;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileMovingPane) {
            TileMovingPane p = (TileMovingPane) te;
            if (p.getIsPane()) return false;
            if (!axis.equals(p.getFacing())) {
                EnumFacing facing = p.getFacing();
                if (!axis.equals(facing.getOpposite())) {
                    int i = 0;
                    //Check if it is empty:
                    BlockPos offset = pos.offset(facing);
                    while (!world.getBlockState(offset).getBlock().equals(Blocks.air)) {
                        facing = facing.rotateAround(axis.getAxis());
                        i++;
                        if (i == 4) {
                            return false;
                        }
                    }
                    //Remove our former child.
                    Location child = p.getChildLocation();
                    ((TileMovingPane) world.getTileEntity(child.toBlockPos())).setParentLocation(null);
                    world.setBlockToAir(child.toBlockPos());
                    world.setBlockState(offset, HCBlocks.movingPane.getDefaultState());
                    TileMovingPane tilePane = (TileMovingPane) world.getTileEntity(offset);
                    if (tilePane != null) {
                        tilePane.setParentLocation(new Location(pos));
                        tilePane.setIsPane(true);
                        tilePane.setPaneFacing(p.getPaneFacing());
                        tilePane.setFacing(facing);
                    }

                    p.setChildLocation(new Location(offset));
                    p.setFacing(facing);
                    p.getHandler().updateBlock();
                    world.notifyNeighborsOfStateChange(pos, this);
                } else {
                    //Rotate the pane itself.
                    Location child = p.getChildLocation();
                    EnumFacing paneFacing = p.getPaneFacing();
                    //TODO: I MAY BE WRONG
                    paneFacing = paneFacing.rotateAround(EnumFacing.Axis.Y);
                    ((TileMovingPane) world.getTileEntity(child.toBlockPos())).setPaneFacing(paneFacing);
                    ((TileMovingPane) world.getTileEntity(child.toBlockPos())).getHandler().updateBlock();
                    p.setPaneFacing(paneFacing);
                    p.getHandler().updateBlock();
                    world.notifyNeighborsOfStateChange(pos, this);
                }
            }
        }

        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileMovingPane) {
            TileMovingPane pane = ((TileMovingPane) tileEntity);
            if (!pane.getIsPane()) {
                super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
                return;
            }
            float movedPercentage = pane.getMovedPercentage();
            EnumFacing facing = pane.getFacing();
            int xPlus = (facing.getFrontOffsetX() > 0 ? 1 : 0);
            int xMin = (facing.getFrontOffsetX() < 0 ? 1 : 0);
            int yPlus = (facing.getFrontOffsetY() > 0 ? 1 : 0);
            int yMin = (facing.getFrontOffsetY() < 0 ? 1 : 0);
            int zPlus = (facing.getFrontOffsetZ() > 0 ? 1 : 0);
            int zMin = (facing.getFrontOffsetZ() < 0 ? 1 : 0);

            float minX = 0.0F + (xMin * movedPercentage);
            float minY = 0.0F + (yMin * movedPercentage);
            float minZ = 0.0F + (zMin * movedPercentage);
            float maxX = 1.0F - (xPlus * movedPercentage);
            float maxY = 1.0F - (yPlus * movedPercentage);
            float maxZ = 1.0F - (zPlus * movedPercentage);

            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileMovingPane) {
            TileMovingPane pane = ((TileMovingPane) tileEntity);
            if (!pane.getIsPane()) {
                return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
            float movedPercentage = pane.getMovedPercentage();
            float angle = 90.0F;
            float eSin = 1.0F - (float) Math.cos(Math.toRadians(angle * movedPercentage));
            EnumFacing facing = pane.getFacing();
            int xPlus = (facing.getFrontOffsetX() > 0 ? 1 : 0);
            int xMin = (facing.getFrontOffsetX() < 0 ? 1 : 0);
            int yPlus = (facing.getFrontOffsetY() > 0 ? 1 : 0);
            int yMin = (facing.getFrontOffsetY() < 0 ? 1 : 0);
            int zPlus = (facing.getFrontOffsetZ() > 0 ? 1 : 0);
            int zMin = (facing.getFrontOffsetZ() < 0 ? 1 : 0);

            float minX = 0.0F + (xMin * eSin);
            float minY = 0.0F + (yMin * eSin);
            float minZ = 0.0F + (zMin * eSin);
            float maxX = 1.0F - (xPlus * eSin);
            float maxY = 1.0F - (yPlus * eSin);
            float maxZ = 1.0F - (zPlus * eSin);

            return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return getDefaultState().withProperty(Properties.CHILD, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, Properties.CHILD);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(Properties.CHILD, (meta & 1) == 1);

    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(Properties.CHILD) ? 1 : 0;
    }
}
