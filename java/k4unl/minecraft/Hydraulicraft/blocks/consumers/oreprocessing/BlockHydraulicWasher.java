package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicWasher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicWasher extends HydraulicBlockContainerBase {

	public BlockHydraulicWasher() {
		super(Names.blockHydraulicWasher);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicWasher();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicWasher)){
			return false;
			
		}
		TileHydraulicWasher washer = (TileHydraulicWasher) entity;
		if(washer.getIsValidMultiblock()){
			player.openGui(Hydraulicraft.instance, GuiIDs.WASHER.ordinal(), world, x, y, z);
			return true;
		}
		
		return false;
	}

}
