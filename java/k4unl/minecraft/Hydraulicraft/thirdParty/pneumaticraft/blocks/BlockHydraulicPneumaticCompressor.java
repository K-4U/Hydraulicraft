package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicPneumaticCompressor extends HydraulicBlockContainerBase {

	public BlockHydraulicPneumaticCompressor() {
		super(Names.blockHydraulicPneumaticCompressor);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicPneumaticCompressor();
	}

	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir){
		return true;
    }
	
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicPneumaticCompressor)){
			return false;
			
		}
		//TileHydraulicPneumaticCompressor compressor = (TileHydraulicPneumaticCompressor) entity;
		player.openGui(Hydraulicraft.instance, GuiIDs.GUIPneumaticCompressor, world, x, y, z);
		
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, Block blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileHydraulicPneumaticCompressor){
			((TileHydraulicPneumaticCompressor)tile).checkRedstonePower();			
		}
	}
	
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ){
	    TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof TileHydraulicPneumaticCompressor){
            ((TileHydraulicPneumaticCompressor)tile).getAirHandler().onNeighborChange();          
        }
	}

}
