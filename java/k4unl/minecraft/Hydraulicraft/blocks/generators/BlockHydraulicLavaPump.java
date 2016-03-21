package k4unl.minecraft.Hydraulicraft.blocks.generators;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicLavaPump extends HydraulicTieredBlockBase implements IMultiTieredBlock, IBlockWithRotation {

    public BlockHydraulicLavaPump() {

        super(Names.blockHydraulicLavaPump);
        this.hasTextures = false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicLavaPump(metadata);
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.LAVAPUMP;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public PressureTier getTier(int damage) {

        return PressureTier.fromOrdinal(damage);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, BlockPos pos) {

        return getTierFromState(world.getBlockState(pos));
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }
}
