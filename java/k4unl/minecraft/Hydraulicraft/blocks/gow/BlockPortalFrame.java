package k4unl.minecraft.Hydraulicraft.blocks.gow;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import k4unl.minecraft.k4lib.lib.Location;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockPortalFrame extends GOWBlockRendering {
    private static Vector3fMax blockBounds = new Vector3fMax(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);

    public BlockPortalFrame() {

        super(Names.portalFrame.unlocalized);
        setCreativeTab(CustomTabs.tabGOW);
        setDefaultState(this.blockState.getBaseState().withProperty(Properties.ACTIVE, false));

    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        Vector3fMax vector = blockBounds.copy();
        if (isStateConnectedTo(worldIn, pos, EnumFacing.UP))
            vector.setYMax(1.0F);
        if (isStateConnectedTo(worldIn, pos, EnumFacing.DOWN))
            vector.setYMin(0.0F);

        if (isStateConnectedTo(worldIn, pos, EnumFacing.EAST))
            vector.setXMax(1.0F);
        if (isStateConnectedTo(worldIn, pos, EnumFacing.WEST))
            vector.setXMin(0.0F);

        if (isStateConnectedTo(worldIn, pos, EnumFacing.SOUTH))
            vector.setZMax(1.0F);
        if (isStateConnectedTo(worldIn, pos, EnumFacing.NORTH))
            vector.setZMin(0.0F);


        this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
    }

    private boolean isStateConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing dir){
        switch(dir){

            case DOWN:
                return world.getBlockState(pos).getValue(Properties.DOWN);
            case UP:
                return world.getBlockState(pos).getValue(Properties.UP);
            case NORTH:
                return world.getBlockState(pos).getValue(Properties.NORTH);
            case SOUTH:
                return world.getBlockState(pos).getValue(Properties.SOUTH);
            case WEST:
                return world.getBlockState(pos).getValue(Properties.WEST);
            case EAST:
                return world.getBlockState(pos).getValue(Properties.EAST);
        }
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        Vector3fMax vector = blockBounds.copy();
        if (isStateConnectedTo(worldIn, pos, EnumFacing.UP))
            vector.setYMax(1.0F);
        if (isStateConnectedTo(worldIn, pos, EnumFacing.DOWN))
            vector.setYMin(0.0F);

        if (isStateConnectedTo(worldIn, pos, EnumFacing.EAST))
            vector.setXMax(1.0F);
        if (isStateConnectedTo(worldIn, pos, EnumFacing.WEST))
            vector.setXMin(0.0F);

        if (isStateConnectedTo(worldIn, pos, EnumFacing.SOUTH))
            vector.setZMax(1.0F);
        if (isStateConnectedTo(worldIn, pos, EnumFacing.NORTH))
            vector.setZMin(0.0F);


        this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TilePortalFrame();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, Properties.ACTIVE, Properties.DOWN, Properties.UP, Properties.NORTH, Properties.SOUTH, Properties.WEST, Properties.EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        return super.getActualState(state, worldIn, pos)
                .withProperty(Properties.DOWN, isConnectedTo(worldIn, pos, EnumFacing.DOWN))
                .withProperty(Properties.UP, isConnectedTo(worldIn, pos, EnumFacing.UP))
                .withProperty(Properties.NORTH, isConnectedTo(worldIn, pos, EnumFacing.NORTH))
                .withProperty(Properties.SOUTH, isConnectedTo(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(Properties.EAST, isConnectedTo(worldIn, pos, EnumFacing.EAST))
                .withProperty(Properties.WEST, isConnectedTo(worldIn, pos, EnumFacing.WEST));
    }

    public boolean isConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing dir) {

        Location thatLocation = new Location(pos, dir);
        return !(world == null) && (thatLocation.getBlock(world) == this || thatLocation.getBlock(world) instanceof BlockPortalBase);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(Properties.ACTIVE, ((meta & 1) == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(Properties.ACTIVE) ? 1 : 0;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
