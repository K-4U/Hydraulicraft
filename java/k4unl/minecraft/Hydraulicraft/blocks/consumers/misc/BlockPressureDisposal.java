package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TilePressureDisposal;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPressureDisposal extends HydraulicBlockContainerBase {

	public BlockPressureDisposal() {
		super(Names.blockPressureDisposal, false);
		
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TilePressureDisposal();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.INVALID;
	}
	
	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir){
		return true;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TilePressureDisposal){
			((TilePressureDisposal)tile).checkRedstonePower();			
		}
	}

}
