package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockHydraulicGenerator extends MachineBlockContainer {

	public BlockHydraulicGenerator() {
		super(Ids.blockHydraulicGenerator, Names.blockHydraulicGenerator);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicGenerator();
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
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicGenerator)){
			return false;
			
		}
		//TileHydraulicPneumaticCompressor compressor = (TileHydraulicPneumaticCompressor) entity;
		//player.openGui(Hydraulicraft.instance, Ids.GUIPneumaticCompressor.act, world, x, y, z);
		
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile instanceof TileHydraulicGenerator){
			((TileHydraulicGenerator)tile).checkRedstonePower();			
		}
	}
	@Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side){
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(te instanceof TileHydraulicGenerator){
			TileHydraulicGenerator e = (TileHydraulicGenerator) te;
			ForgeDirection facing = e.getFacing();
			e.setFacing(facing.getRotation(side));
			e.getHandler().updateBlock();
			world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		}
		
		return true;
    }
	
}
