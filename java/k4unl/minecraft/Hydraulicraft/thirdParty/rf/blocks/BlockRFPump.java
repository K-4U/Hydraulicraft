package k4unl.minecraft.Hydraulicraft.thirdParty.rf.blocks;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.blocks.IRotateableBlock;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.rf.tileEntities.TileRFPump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRFPump extends HydraulicTieredBlockBase implements IMultiTieredBlock, IRotateableBlock, IBlockWithRotation {

    public BlockRFPump() {

        super(Names.blockRFPump);

        this.hasTopIcon = true;
        hasTextures = false;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileRFPump(getTier(metadata));
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.RFPUMP;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir) {

        return true;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {

        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileRFPump) {
                if (axis.equals(EnumFacing.UP) || axis.equals(EnumFacing.DOWN)) {
                    TileRFPump e = (TileRFPump) te;
                    EnumFacing facing = e.getFacing();
                    e.setFacing(facing.rotateAround(axis.getAxis()));
                    e.getHandler().updateBlock();
                    world.notifyNeighborsOfStateChange(pos, this);
                }
            }
        }

        return true;
    }

    @Override
    public PressureTier getTier(int damage) {

        return PressureTier.fromOrdinal(damage);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, BlockPos pos) {

        return (PressureTier) world.getBlockState(pos).getValue(Properties.TIER);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.TRANSLUCENT;
    }
}
