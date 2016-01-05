package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCharger;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicCharger extends HydraulicBlockContainerBase implements ITieredBlock, IBlockWithRotation{

	public BlockHydraulicCharger() {
		super(Names.blockHydraulicCharger, true);
		hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileHydraulicCharger();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.CHARGER;
	}

	@Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
