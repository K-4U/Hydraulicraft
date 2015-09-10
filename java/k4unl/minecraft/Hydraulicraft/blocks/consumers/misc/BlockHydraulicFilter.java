package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicFilter extends HydraulicBlockContainerBase implements ITieredBlock {

	public BlockHydraulicFilter() {
		super(Names.blockHydraulicFilter, true);
		this.hasFrontIcon = true;
		this.hasLeftIcon = true;
		this.hasRightIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicFilter();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.FILTER;
	}

	@Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
