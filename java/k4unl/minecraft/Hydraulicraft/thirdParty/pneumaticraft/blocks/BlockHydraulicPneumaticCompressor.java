package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicPneumaticCompressor extends HydraulicBlockContainerBase implements ITieredBlock{

	public BlockHydraulicPneumaticCompressor() {
		super(Names.blockHydraulicPneumaticCompressor, true);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicPneumaticCompressor();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.COMPRESSOR;
	}

	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir){
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

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
