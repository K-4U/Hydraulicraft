package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCrusher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicCrusher extends HydraulicBlockContainerBase implements ITieredBlock, IBlockWithRotation{

	public BlockHydraulicCrusher() {
		super(Names.blockHydraulicCrusher, true);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileHydraulicCrusher();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.CRUSHER;
	}


	@Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
