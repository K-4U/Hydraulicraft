package k4unl.minecraft.Hydraulicraft.world;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.worldgen.BlockRubberWood;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

/**
 * @author Koen Beckers (K-4U)
 */
public class WorldGenRubberTree extends WorldGenAbstractTree {

    /**
     * The minimum height of a generated tree.
     */
    private final int minTreeHeight;

    public WorldGenRubberTree(boolean doBlockNotify) {

        this(doBlockNotify, 5);
    }

    public WorldGenRubberTree(boolean doBlockNotify, int minTreeHeight) {

        super(doBlockNotify);
        this.minTreeHeight = minTreeHeight;
    }

    public boolean generate(World world, Random random, BlockPos pos) {

        int treeHeight = random.nextInt(5) + this.minTreeHeight;
        boolean flag = true;
        //CBA to rewrite this, so just do it like this :D
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (y >= 1 && y + treeHeight + 1 <= 256) {
            byte b0;
            int k1;
            Block block;

            for (int i1 = y; i1 <= y + 1 + treeHeight; ++i1) {
                b0 = 1;

                if (i1 == y) {
                    b0 = 0;
                }

                if (i1 >= y + 1 + treeHeight - 2) {
                    b0 = 2;
                }

                for (int j1 = x - b0; j1 <= x + b0 && flag; ++j1) {
                    for (k1 = z - b0; k1 <= z + b0 && flag; ++k1) {
                        if (i1 >= 0 && i1 < 256) {
                            block = world.getBlockState(new BlockPos(j1, i1, k1)).getBlock();

                            if (!this.isReplaceable(world, new BlockPos(j1, i1, k1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }


            if (!flag) {
                return false;
            } else {
                Block block2 = world.getBlockState(pos.down()).getBlock();

                boolean isSoil = block2.canSustainPlant(world.getBlockState(pos), world, pos.down(), EnumFacing.UP, HCBlocks.blockRubberSapling);
                if (isSoil && y < 256 - treeHeight - 1) {
                    block2.onPlantGrow(world.getBlockState(pos), world, pos.down(), pos);
                    b0 = 3;

                    //First on top.
                    setAndCheckBlock(world, x, treeHeight + y, z, HCBlocks.blockRubberLeaves);
                    for (int i = 0; i <= 3; i++) {
                        setAndCheckBlock(world, x + i, treeHeight + y - 1, z, HCBlocks.blockRubberLeaves);
                        setAndCheckBlock(world, x - i, treeHeight + y - 1, z, HCBlocks.blockRubberLeaves);
                        setAndCheckBlock(world, x, treeHeight + y - 1, z + i, HCBlocks.blockRubberLeaves);
                        setAndCheckBlock(world, x, treeHeight + y - 1, z - i, HCBlocks.blockRubberLeaves);
                    }
                    setAndCheckBlock(world, x + 3, treeHeight + y, z, HCBlocks.blockRubberLeaves);
                    setAndCheckBlock(world, x - 3, treeHeight + y, z, HCBlocks.blockRubberLeaves);
                    setAndCheckBlock(world, x, treeHeight + y, z + 3, HCBlocks.blockRubberLeaves);
                    setAndCheckBlock(world, x, treeHeight + y, z - 3, HCBlocks.blockRubberLeaves);

                    for (k1 = 0; k1 < treeHeight; ++k1) {
                        setAndCheckBlock(world, x, y + k1, z, HCBlocks.blockRubberWood);
                    }
                    //4 blocks to the side of the top of the tree.
                    int i1 = y + treeHeight - 2;
                    setAndCheckBlock(world, x + 1, i1, z, HCBlocks.blockRubberWood);
                    setAndCheckBlock(world, x - 1, i1, z, HCBlocks.blockRubberWood);
                    setAndCheckBlock(world, x, i1, z + 1, HCBlocks.blockRubberWood);
                    setAndCheckBlock(world, x, i1, z - 1, HCBlocks.blockRubberWood);

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

    }

    private void setAndCheckBlock(World world, int x, int y, int z, Block toSet) {

        BlockPos pos = new BlockPos(x, y, z);
        Block block1 = world.getBlockState(pos).getBlock();
        if (block1.isAir(world.getBlockState(pos), world, pos) || block1.isLeaves(world.getBlockState(pos), world, pos)) {
            this.setBlockAndNotifyAdequately(world, pos, toSet.getDefaultState());
            if(toSet == HCBlocks.blockRubberWood){
                ((BlockRubberWood)HCBlocks.blockRubberWood).genRubberSpot(world, pos);
            }
        }
    }
}
