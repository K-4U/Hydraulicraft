package k4unl.minecraft.Hydraulicraft.blocks.worldgen;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.world.WorldGenRubberTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockRubberSapling extends BlockBush implements IGrowable {

    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

    public BlockRubberSapling() {

        super(Material.plants);
        setUnlocalizedName(Names.blockRubberSapling.unlocalized);
        this.setSoundType(SoundType.GROUND);

        setCreativeTab(CustomTabs.tabHydraulicraft);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

        return super.canPlaceBlockAt(worldIn, pos);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if (!worldIn.isRemote) {
            super.updateTick(worldIn, pos, state, rand);

            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
                this.grow(worldIn, pos, state, rand);
            }
        }
    }

    public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {

        if ((Integer) state.getValue(STAGE) == 0) {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        } else {
            this.growTree(worldIn, pos, state, rand);
        }
    }

    public void growTree(World world, BlockPos pos, IBlockState state, Random random) {

        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, random, pos)) return;
        Object object = new WorldGenRubberTree(true);
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;

        Block block = Blocks.air;
        world.setBlockState(pos, block.getDefaultState(), 4);

        if (!((WorldGenerator) object).generate(world, random, new BlockPos(pos.getX() + i1, pos.getY(), pos.getZ() + j1))) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            if (flag) {
                world.setBlockState(new BlockPos(x + i1, y, z + j1), this.getDefaultState(), 4);
                world.setBlockState(new BlockPos(x + i1 + 1, y, z + j1), this.getDefaultState(), 4);
                world.setBlockState(new BlockPos(x + i1, y, z + j1 + 1), this.getDefaultState(), 4);
                world.setBlockState(new BlockPos(x + i1 + 1, y, z + j1 + 1), this.getDefaultState(), 4);
            } else {
                world.setBlockState(new BlockPos(x, y, z), this.getDefaultState(), 4);
            }
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {

        return true;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {

        return (double) worldIn.rand.nextFloat() < 0.45D;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {

        this.grow(worldIn, pos, state, rand);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {

        return this.getDefaultState().withProperty(STAGE, (meta & 8) >> 3);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {

        int i = 0;
        i = i | ((Integer) state.getValue(STAGE)).intValue() << 3;
        return i;
    }

    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, STAGE);
    }
}
