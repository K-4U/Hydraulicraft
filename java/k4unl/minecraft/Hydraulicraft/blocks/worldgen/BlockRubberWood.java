package k4unl.minecraft.Hydraulicraft.blocks.worldgen;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.worldgen.TileRubberWood;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
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
public class BlockRubberWood extends HydraulicBlockContainerBase {

    public BlockRubberWood() {

        super(Names.blockRubberWood, Material.wood, true);

        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
        this.setDefaultState(this.blockState.getBaseState().withProperty(Properties.HAS_RUBBER_SPOT, false).withProperty(Properties.ROTATION, EnumFacing.NORTH));
        this.setTickRandomly(true);
        Vector3fMax vector = getCollisionBox();
        this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
    }

    @Override
    public boolean isFullCube() {

        return false;
    }



    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {

        return getCollisionBox().toAABB();
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
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileRubberWood();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
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
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {

        return getCollisionBox().toAABB();
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

        Vector3fMax vector = getCollisionBox();
        list.add(vector.toAABB());
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    private Vector3fMax getCollisionBox() {

        float pixel = 1F / 16F;
        return new Vector3fMax(pixel, 0.0F, pixel, 1.0F - pixel, 1.0F, 1.0F - pixel);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        Vector3fMax vector = getCollisionBox();
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

        return getDefaultState().withProperty(Properties.HAS_RUBBER_SPOT, false).withProperty(Properties.ROTATION, EnumFacing.NORTH);
    }

    public void genRubberSpot(World worldIn, BlockPos pos){

        IBlockState state = worldIn.getBlockState(pos);
        double rnd = (new Random()).nextDouble();
        int rnd2 = (new Random()).nextInt(4);
        EnumFacing rndFacing = EnumFacing.HORIZONTALS[rnd2];
        boolean rubberSpot = rnd <= HCConfig.INSTANCE.getDouble("rubberPatchChance", "worldgen");
        if(worldIn.getBlockState(pos.down()).getBlock() == Blocks.grass || worldIn.getBlockState(pos.down()).getBlock() == Blocks.dirt){
            rubberSpot = false;
        }
        Log.info("Rubber on " + pos.toString() + "=" + rubberSpot);
        state = state.withProperty(Properties.HAS_RUBBER_SPOT, rubberSpot).withProperty(Properties.ROTATION, rndFacing);
        worldIn.setBlockState(pos, state);
    }

    public IBlockState getStateFromMeta(int meta) {

        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(Properties.HAS_RUBBER_SPOT, ((meta & 1) == 1));

        //Rotational:
        int rotation = meta >> 1;
        iblockstate = iblockstate.withProperty(Properties.ROTATION, EnumFacing.HORIZONTALS[rotation]);

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {

        int i = 0;
        if (state.getValue(Properties.HAS_RUBBER_SPOT)) {
            i |= 1;
        }
        int horizontal = state.getValue(Properties.ROTATION).getHorizontalIndex();
        i |= horizontal << 1;

        return i;
    }

    @Override
    protected BlockState createBlockState() {

        return new BlockState(this, Properties.HAS_RUBBER_SPOT, Properties.ROTATION);
    }

    @Override
    public boolean isFullBlock() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {

        super.randomTick(worldIn, pos, state, random);
        if(state.getValue(Properties.HAS_RUBBER_SPOT) && worldIn.getTileEntity(pos) instanceof TileRubberWood){
            ((TileRubberWood)worldIn.getTileEntity(pos)).randomTick();
        }
    }
}
