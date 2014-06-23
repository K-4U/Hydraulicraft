package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInterfaceValve extends HydraulicBlockContainerBase {

	public BlockInterfaceValve() {
		super(Names.blockInterfaceValve);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileInterfaceValve();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		
		TileInterfaceValve valve = (TileInterfaceValve) world.getTileEntity(x, y, z);
		valve.checkTank();
		return false;
	}

}
