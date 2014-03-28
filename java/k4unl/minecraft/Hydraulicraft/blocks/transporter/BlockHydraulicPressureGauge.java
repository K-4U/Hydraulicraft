package k4unl.minecraft.Hydraulicraft.blocks.transporter;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicPressureGauge extends MachineBlockContainer {

	protected BlockHydraulicPressureGauge() {
		super(Names.blockHydraulicPressureGauge);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}
	
	
	
}
