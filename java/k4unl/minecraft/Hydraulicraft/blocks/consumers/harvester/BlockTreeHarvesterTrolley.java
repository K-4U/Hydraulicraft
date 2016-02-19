package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileTreeharvesterTrolley;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockTreeHarvesterTrolley extends HydraulicBlockContainerBase {
    
    public BlockTreeHarvesterTrolley() {

        super(Names.blockTreeHarvesterTrolley, true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileTreeharvesterTrolley();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
}
