package k4unl.minecraft.Hydraulicraft.blocks.generators;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicPump extends HydraulicTieredBlockBase implements IMultiTieredBlock, IBlockWithRotation {

    public BlockHydraulicPump() {

        super(Names.blockHydraulicPump);
        this.hasTextures = false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicPump(((PressureTier) getStateFromMeta(metadata).getValue(Properties.TIER)).toInt());
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.PUMP;
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
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }
}
