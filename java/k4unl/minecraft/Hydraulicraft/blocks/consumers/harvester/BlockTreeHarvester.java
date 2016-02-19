package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.blocks.IGUIMultiBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileTreeHarvester;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockTreeHarvester extends HydraulicBlockContainerBase implements ITieredBlock, IGUIMultiBlock, IBlockWithRotation {


    public BlockTreeHarvester() {

        super(Names.blockTreeHarvester, true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileTreeHarvester();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.TREE_HARVESTER;
    }

    @Override
    public boolean isValid(IBlockAccess world, BlockPos pos) {

        return world.getTileEntity(pos) instanceof TileTreeHarvester && ((TileTreeHarvester) world.getTileEntity(pos)).isValid();
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
