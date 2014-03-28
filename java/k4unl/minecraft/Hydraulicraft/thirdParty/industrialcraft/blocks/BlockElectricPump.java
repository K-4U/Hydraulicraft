package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.tools.IToolWrench;

public class BlockElectricPump extends MachineTieredBlock {

	public BlockElectricPump() {
		super(Names.blockElectricPump);
		
		this.hasTopIcon = true;
		hasTextures = false;
	}


	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileElectricPump();
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
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileElectricPump)){
			return false;
		}
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IToolWrench){
			return false;
		}

		player.openGui(Hydraulicraft.instance, GuiIDs.GUIElecticPump, world, x, y, z);
		
		return true;
	}
	
	@Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection side){
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileElectricPump){
			if(side.equals(ForgeDirection.UP) || side.equals(ForgeDirection.DOWN)){
				TileElectricPump e = (TileElectricPump) te;
				ForgeDirection facing = e.getFacing();
				e.setFacing(facing.getRotation(side));
				e.getHandler().updateBlock();
				world.notifyBlocksOfNeighborChange(x, y, z, this);
			}
		}
		
		return true;
    }

}
