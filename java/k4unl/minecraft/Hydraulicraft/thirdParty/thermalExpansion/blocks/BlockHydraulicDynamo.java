package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.tools.IToolWrench;

public class BlockHydraulicDynamo extends HydraulicBlockContainerBase {

	public BlockHydraulicDynamo() {
		super(Names.blockHydraulicDynamo);
		hasTextures = false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicDynamo();
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
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		
		 
		if(player.isSneaking()){
			return false;
		}
		
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IToolWrench){
			return false;
		}
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicDynamo)){
			return false;
			
		}
		//TileHydraulicDynamo dyn = (TileHydraulicDynamo) entity;
		player.openGui(Hydraulicraft.instance, GuiIDs.DYNAMO.ordinal(), world, x, y, z);
		
		return true;
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
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileHydraulicDynamo){
			TileHydraulicDynamo e = (TileHydraulicDynamo) te;
			ForgeDirection facing = e.getFacing();
			e.setFacing(facing.getRotation(side));
			e.getHandler().updateBlock();
			world.notifyBlocksOfNeighborChange(x, y, z, this);
			return true;
		}
		
		return false;
    }
	
}
