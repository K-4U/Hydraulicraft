package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicFrictionIncinerator extends HydraulicBlockContainerBase implements ITieredBlock, IBlockWithRotation {

	public BlockHydraulicFrictionIncinerator() {
		super(Names.blockHydraulicFrictionIncinerator, true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileHydraulicFrictionIncinerator();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.INCINERATOR;
	}

	@Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
