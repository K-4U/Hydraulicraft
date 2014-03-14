package k4unl.minecraft.Hydraulicraft.blocks.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileInterfaceValve;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class BlockInterfaceValve extends MachineBlockContainer {

	public BlockInterfaceValve() {
		super(Ids.blockInterfaceValve, Names.blockInterfaceValve);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileInterfaceValve();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}

}
