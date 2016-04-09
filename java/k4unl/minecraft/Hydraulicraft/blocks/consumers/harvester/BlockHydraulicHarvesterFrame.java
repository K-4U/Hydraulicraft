package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicHarvesterFrame extends HydraulicBlockBase {

    public Vector3fMax blockBounds = new Vector3fMax(0.2f, 0.2f, 0.2f, 0.8F, 0.8F, 0.8F);

    public BlockHydraulicHarvesterFrame() {

        super(Names.blockHarvesterFrame, true);
        hasTextures = false;

        setDefaultState(this.blockState.getBaseState().withProperty(Properties.HARVESTER_FRAME_ROTATED, false));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return getDefaultState().withProperty(Properties.HARVESTER_FRAME_ROTATED, checkRotation(placer));
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, Properties.HARVESTER_FRAME_ROTATED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(Properties.HARVESTER_FRAME_ROTATED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(Properties.HARVESTER_FRAME_ROTATED) ? 1 : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        float minX = blockBounds.getXMin();
        float minY = blockBounds.getYMin();
        float minZ = blockBounds.getZMin();
        float maxX = blockBounds.getXMax();
        float maxY = blockBounds.getYMax();
        float maxZ = blockBounds.getZMax();

        if (state.getValue(Properties.HARVESTER_FRAME_ROTATED)) {
            minX = 0.0F;
            maxX = 1.0F;
        } else {
            minZ = 0.0F;
            maxZ = 1.0F;
        }

        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean checkRotation(EntityLivingBase player) {

        int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;
        return sideToPlace == 1 || sideToPlace == 3;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }
}
