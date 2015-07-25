package k4unl.minecraft.Hydraulicraft.blocks;


import buildcraft.api.tools.IToolWrench;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class HydraulicBlockContainerBase extends HydraulicBlockBase implements ITileEntityProvider {
	
	@Override
	public abstract TileEntity createNewTileEntity(World world, int var2);

	public abstract GuiIDs getGUIID();
	
	protected HydraulicBlockContainerBase(Name machineName) {
		super(machineName);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
	}
	
	@Override
	public void breakBlock(World w, int x, int y, int z, Block oldBlock, int oldMetaData){
		//Call TileEntity's onBlockBreaks function
		TileEntity tile = w.getTileEntity(x, y, z);
		if(tile instanceof TileHydraulicBase){
			((TileHydraulicBase)tile).onBlockBreaks();
		}
		
		super.breakBlock(w, x, y, z, oldBlock, oldMetaData);
		w.removeTileEntity(x, y, z);
	}

	public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_) {
		super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
		TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
		return tileentity != null && tileentity.receiveClientEvent(p_149696_5_, p_149696_6_);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
									EntityPlayer player, int par6, float par7, float par8, float par9) {

		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity == null) {
			return false;
		}

		if(this instanceof IRotateableBlock){
			//TODO: Fix me for dependency on BC
			if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IToolWrench){
				return false;
			}
		}

		if(getGUIID() != GuiIDs.INVALID) {
			if(this instanceof IGUIMultiBlock){
				if(!((IGUIMultiBlock)this).isValid(world, x, y, z)) {
					return false;
				}
			}
			player.openGui(Hydraulicraft.instance, getGUIID().ordinal(), world, x, y, z);
			return true;
		}

		return false;
	}
}
