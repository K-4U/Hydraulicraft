package k4unl.minecraft.Hydraulicraft.world;

import net.minecraft.block.Block;
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

    public boolean generate(World world, Random random, int x, int y, int z) {
        /*int treeHeight = random.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

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
                            block = world.getBlock(j1, i1, k1);

                            if (!this.isReplaceable(world, j1, i1, k1)) {
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
                Block block2 = world.getBlock(x, y - 1, z);

                boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
                if (isSoil && y < 256 - treeHeight - 1) {
                    block2.onPlantGrow(world, x, y - 1, z, x, y, z);
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
        }*/
        return false;
    }

    private void setAndCheckBlock(World world, int x, int y, int z, Block toSet) {
        Block block1 = world.getBlock(x, y, z);
        if (block1.isAir(world, x, y, z) || block1.isLeaves(world, x, y, z)) {
            this.setBlockAndNotifyAdequately(world, x, y, z, toSet, 0);
        }
    }
}
