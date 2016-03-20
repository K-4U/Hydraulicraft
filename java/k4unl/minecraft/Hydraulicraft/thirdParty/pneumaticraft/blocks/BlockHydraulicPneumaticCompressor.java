package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicPneumaticCompressor extends HydraulicBlockContainerBase implements ITieredBlock {

    public BlockHydraulicPneumaticCompressor() {

        super(Names.blockHydraulicPneumaticCompressor, true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicPneumaticCompressor();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.COMPRESSOR;
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir) {

        return true;
    }


    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileHydraulicPneumaticCompressor) {
            ((TileHydraulicPneumaticCompressor) tile).checkRedstonePower();
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {

        super.onNeighborChange(world, pos, neighbor);
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileHydraulicPneumaticCompressor) {
            //((TileHydraulicPneumaticCompressor) tile).getAirHandler();
        }
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
