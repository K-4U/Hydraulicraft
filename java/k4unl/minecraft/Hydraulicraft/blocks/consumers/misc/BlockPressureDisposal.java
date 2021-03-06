package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TilePressureDisposal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPressureDisposal extends HydraulicBlockContainerBase {

    public BlockPressureDisposal() {

        super(Names.blockPressureDisposal, false);

    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TilePressureDisposal();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {

        return true;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {

        super.onNeighborChange(world, pos, neighbor);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TilePressureDisposal) {
            ((TilePressureDisposal) tile).checkRedstonePower();
        }
    }

}
