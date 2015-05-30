package k4unl.minecraft.Hydraulicraft.blocks.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.BlockConnectedTexture;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;

public class BlockHydraulicPressureGlass extends BlockConnectedTexture {

	public BlockHydraulicPressureGlass() {
		super(Names.blockHydraulicPressureGlass, Material.glass);
	}
	
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass(){
        return 1;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(){
    	return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int side){
    	Block block = w.getBlock(x, y, z);

        if (this == HCBlocks.hydraulicPressureGlass)
        {
            if (w.getBlockMetadata(x, y, z) != w.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]))
            {
                return true;
            }

            if (block == this)
            {
                return false;
            }
        }

        return block == this ? false : super.shouldSideBeRendered(w, x, y, z, side);
    }

}
