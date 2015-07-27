package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IRotateableBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHydraulicDynamo extends HydraulicBlockContainerBase implements ITieredBlock, IRotateableBlock {

	public BlockHydraulicDynamo() {
		super(Names.blockHydraulicDynamo);
		hasTextures = false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicDynamo();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.DYNAMO;
	}

	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir){
		return true;
    }
	
	@Override
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
				int z, Block blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileHydraulicDynamo){
			((TileHydraulicDynamo)tile).checkRedstonePower();			
		}
	}
	
	
	@Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side){
		if(!world.isRemote) {
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileHydraulicDynamo) {
				TileHydraulicDynamo e = (TileHydraulicDynamo) te;
				ForgeDirection facing = e.getFacing();
				e.setFacing(facing.getRotation(side));
				e.getHandler().updateBlock();
				world.notifyBlocksOfNeighborChange(x, y, z, this);
				return true;
			}
		}
		
		return false;
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
