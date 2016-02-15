package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.BlockConnectedTexture;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHydraulicPressureGlass extends BlockConnectedTexture {

    public BlockHydraulicPressureGlass() {

        super(Names.blockHydraulicPressureGlass, Material.glass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {

        return false;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {

        Block block = worldIn.getBlockState(pos).getBlock();

        if (this == HCBlocks.hydraulicPressureGlass) {
/*            if (worldIn.getBlockMetadata(x, y, z) != worldIn.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]))
            {
                return true;
            }
*/
            if (block == this) {
                return false;
            }
        }

        return block != this && super.shouldSideBeRendered(worldIn, pos, side);
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {

        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public boolean isFullCube() {

        return false;
    }

}
