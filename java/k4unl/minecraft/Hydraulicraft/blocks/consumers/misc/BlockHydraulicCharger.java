package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicCharger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicCharger extends HydraulicBlockContainerBase implements ITieredBlock{

	public BlockHydraulicCharger() {
		super(Names.blockHydraulicCharger);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileHydraulicCharger(getTier());
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
