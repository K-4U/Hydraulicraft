package k4unl.minecraft.Hydraulicraft.blocks.gow;


import k4unl.minecraft.Hydraulicraft.lib.Properties;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public abstract class GOWBlockRendering extends GOWBlockBase {

    protected GOWBlockRendering(String name) {

        super(name);
        setDefaultState(this.blockState.getBaseState().withProperty(Properties.ACTIVE, false));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, Properties.ACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(Properties.ACTIVE, ((meta & 1) == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(Properties.ACTIVE) ? 1 : 0;
    }
}
