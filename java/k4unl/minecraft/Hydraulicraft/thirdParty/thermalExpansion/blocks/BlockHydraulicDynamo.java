package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IRotateableBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicDynamo extends HydraulicBlockContainerBase implements ITieredBlock, IRotateableBlock {

    public BlockHydraulicDynamo() {
        super(Names.blockHydraulicDynamo, true);
        hasTextures = false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileHydraulicDynamo();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.DYNAMO;
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir) {
        return true;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileHydraulicDynamo) {
            ((TileHydraulicDynamo) tile).checkRedstonePower();
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileHydraulicDynamo) {
                TileHydraulicDynamo e = (TileHydraulicDynamo) te;
                EnumFacing facing = e.getFacing();
                e.setFacing(facing.rotateAround(axis.getAxis()));
                e.getHandler().updateBlock();
                world.notifyNeighborsOfStateChange(pos, this);
                return true;
            }
        }
        return false;
    }


    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
