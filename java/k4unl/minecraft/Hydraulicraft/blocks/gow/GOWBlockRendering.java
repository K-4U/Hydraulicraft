package k4unl.minecraft.Hydraulicraft.blocks.gow;


import k4unl.minecraft.Hydraulicraft.lib.Properties;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public abstract class GOWBlockRendering extends GOWBlockBase {

    protected GOWBlockRendering(String name) {
        super(name);
        setDefaultState(this.blockState.getBaseState().withProperty(Properties.ACTIVE, false));
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, Properties.ACTIVE);
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
