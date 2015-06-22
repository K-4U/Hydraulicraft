package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHydraulicHarvester;
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
    public GuiIDs getGUIID() {

        return GuiIDs.HARVESTER;
    }


    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
