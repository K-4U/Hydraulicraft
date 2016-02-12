package k4unl.minecraft.Hydraulicraft.blocks.worldgen;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockRubberWood extends HydraulicBlockBase {

    //private IIcon rubberPatch;

    public BlockRubberWood() {
        //TODO: Fix me, make me a proper wooden log, with rotation and all

        super(Names.blockRubberWood, Material.wood, true);

        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(Properties
                .HAS_RUBBER_SPOT, false));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_) {

        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {

        return Item.getItemFromBlock(this);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        int i = 4;
        int j = i + 1;

        if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j))) {
            for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i))) {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (iblockstate.getBlock().isLeaves(worldIn, blockpos)) {
                    iblockstate.getBlock().beginLeavesDecay(worldIn, blockpos);
                }
            }
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    private Vector3fMax getCollisionBox(IBlockState state) {

        float pixel = 1F / 16F;
        Vector3fMax vector = new Vector3fMax(pixel, pixel, pixel, 1.0F - pixel, 1.0F - pixel, 1.0F - pixel);
        if (state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Y) {
            vector.setYMin(0.0F);
            vector.setYMax(1.0F);
        }
        if (state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.X) {
            vector.setXMin(0.0F);
            vector.setXMax(1.0F);
        }
        if (state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Z) {
            vector.setZMin(0.0F);
            vector.setZMax(1.0F);
        }
        return vector;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        Vector3fMax vector = getCollisionBox(worldIn.getBlockState(pos));

        this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
    }


    @Override
    public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {

        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {

        return true;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis
                .fromFacingAxis(facing.getAxis())).withProperty(Properties.HAS_RUBBER_SPOT, false); //No rubber spot when the block is already placed
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

        super.onBlockAdded(worldIn, pos, state);
        double rnd = (new Random()).nextDouble();

        state = state.withProperty(Properties.HAS_RUBBER_SPOT, rnd <= HCConfig.INSTANCE.getDouble("rubberPatchChance", "worldgen"));
        worldIn.setBlockState(pos, state);
    }

    public IBlockState getStateFromMeta(int meta) {

        IBlockState iblockstate = this.getDefaultState();

        switch (meta & 12) {
            case 0:
                iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
        }
        iblockstate = iblockstate.withProperty(Properties.HAS_RUBBER_SPOT, ((meta & 1) == 1));

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {

        int i = 0;

        switch (state.getValue(BlockLog.LOG_AXIS)) {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }
        if (state.getValue(Properties.HAS_RUBBER_SPOT)) {
            i |= 1;
        }

        return i;
    }

    @Override
    protected BlockState createBlockState() {

        return new BlockState(this, BlockLog.LOG_AXIS, Properties.HAS_RUBBER_SPOT);
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
