package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicHarvester extends HydraulicBlockContainerBase implements ITieredBlock {

    public BlockHydraulicHarvester() {

        super(Names.blockHydraulicHarvester);

        hasFrontIcon = true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicHarvester();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
      EntityPlayer player, int par6, float par7, float par8, float par9) {

        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity == null || !(entity instanceof TileHydraulicHarvester)) {
            return false;

        }

        TileHydraulicHarvester harvester = (TileHydraulicHarvester) entity;
        if (harvester.getIsMultiblock()) {
            player.openGui(Hydraulicraft.instance, GuiIDs.HARVESTER.ordinal(), world, x, y, z);
            return true;
		}
		
		return false;
	}


    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
