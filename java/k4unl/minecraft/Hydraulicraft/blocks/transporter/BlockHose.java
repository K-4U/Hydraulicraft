package k4unl.minecraft.Hydraulicraft.blocks.transporter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.TileEntities.transporter.TilePressureHose;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHose extends MachineTieredBlock {

	public BlockHose() {
		super(Names.partHose);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		TilePressureHose pHose = new TilePressureHose();
		pHose.setTier(metadata);
		return pHose;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType(){
        return -1;
    }
	
	@Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity t = world.getTileEntity(x, y, z);
		if(t instanceof TilePressureHose){
			((TilePressureHose)t).refreshConnectedSides();
		}
	}
    
}
