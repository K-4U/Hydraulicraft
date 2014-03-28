package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockInterfaceValve extends MachineBlockContainer {

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
		return false;
	}

}
