package k4unl.minecraft.Hydraulicraft.blocks.transporter;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


//TODO: IMPLEMENT ME
public class BlockHydraulicPressureGauge extends HydraulicBlockContainerBase {

	protected BlockHydraulicPressureGauge() {
		super(Names.blockHydraulicPressureGauge, true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return null;
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.INVALID;
	}

}
