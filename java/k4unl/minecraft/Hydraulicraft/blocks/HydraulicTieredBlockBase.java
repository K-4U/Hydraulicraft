package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public abstract class HydraulicTieredBlockBase extends HydraulicBlockContainerBase {

    private Name[] mName;

    protected boolean hasTopIcon    = false;
    protected boolean hasBottomIcon = false;
    protected boolean hasTextures   = true;

    @Override
    public abstract TileEntity createNewTileEntity(World world, int var2);

    protected HydraulicTieredBlockBase(Name[] machineName) {

        super(machineName[0], true);

        mName = machineName;

        if (this instanceof IBlockWithRotation) {
            setDefaultState(this.blockState.getBaseState().withProperty(Properties.ROTATION, EnumFacing.NORTH).withProperty(Properties.TIER, PressureTier.INVALID));
        } else {
            setDefaultState(this.blockState.getBaseState().withProperty(Properties.TIER, PressureTier.INVALID));
        }


        setUnlocalizedName(mName[0].unlocalized);
        setSoundType(SoundType.STONE);
        setHardness(3.5F);

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }

    public String getUnlocalizedName(int meta) {

        return "tile." + mName[meta].unlocalized;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {

        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        if (this instanceof IBlockWithRotation) {
            return getDefaultState().withProperty(Properties.ROTATION, placer.getHorizontalFacing().getOpposite()).withProperty(Properties.TIER, PressureTier.fromOrdinal(meta & 3));
        } else {
            return getDefaultState().withProperty(Properties.TIER, PressureTier.fromOrdinal(meta & 3));
        }

    }

    @Override
    public int damageDropped(IBlockState state) {

        return getTierFromState(state).toInt();
    }

    @Override
    protected BlockStateContainer createBlockState() {

        if (this instanceof IBlockWithRotation) {
            return new BlockStateContainer(this, Properties.TIER, Properties.ROTATION);
        } else {
            return new BlockStateContainer(this, Properties.TIER);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        if (this instanceof IBlockWithRotation) {
            return super.getStateFromMeta(meta).withProperty(Properties.TIER, PressureTier.fromOrdinal(meta & 3));
        } else {
            return getDefaultState().withProperty(Properties.TIER, PressureTier.fromOrdinal(meta & 3));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        if (this instanceof IBlockWithRotation) {
            return super.getMetaFromState(state) + ((PressureTier) state.getValue(Properties.TIER)).toInt();
        } else {
            return ((PressureTier) state.getValue(Properties.TIER)).toInt();
        }
    }

    public PressureTier getTierFromState(IBlockState state) {

        return (PressureTier) state.getValue(Properties.TIER);
    }


}
