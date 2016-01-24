package k4unl.minecraft.Hydraulicraft.blocks.worldgen;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
    }

    /*
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {

        super.registerBlockIcons(iconRegistry);
        rubberPatch = iconRegistry.registerIcon(getTextureName("patch"));
    }

    public IIcon getRubberPatch() {

        return rubberPatch;
    }*/

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int i = 1;
        int j = i + 1;
        int k = pos.getX();
        int l = pos.getY();
        int i1 = pos.getZ();

        if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j))) {
            for (int j1 = -i; j1 <= i; ++j1) {
                for (int k1 = -i; k1 <= i; ++k1) {
                    for (int l1 = -i; l1 <= i; ++l1) {
                        BlockPos blockpos = pos.add(j1, k1, l1);
                        IBlockState iblockstate = worldIn.getBlockState(blockpos);

                        if (iblockstate.getBlock().isLeaves(worldIn, blockpos)) {
                            iblockstate.getBlock().beginLeavesDecay(worldIn, blockpos);
                        }
                    }
                }
            }
        }
    }


    @Override
    public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {
        return true;
    }
}
