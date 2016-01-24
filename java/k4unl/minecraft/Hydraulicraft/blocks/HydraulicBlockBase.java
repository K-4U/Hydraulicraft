package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HydraulicBlockBase extends Block {
    public Name mName;
    protected boolean hasTextures = true;

    protected HydraulicBlockBase(Name machineName, boolean addToTab) {
        this(machineName, Material.rock, addToTab);
    }


    public HydraulicBlockBase(Name machineName, Material material, boolean addToTab) {
        super(material);

        mName = machineName;

        setUnlocalizedName(mName.unlocalized);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5F);
        setResistance(10F);
        if(this instanceof IBlockWithRotation) {
            setDefaultState(this.blockState.getBaseState().withProperty(Properties.ROTATION, EnumFacing.NORTH));
        }

        if (addToTab) {
            setCreativeTab(CustomTabs.tabHydraulicraft);
        }
        Hydraulicraft.proxy.registerBlockRenderer(this);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(this instanceof IBlockWithRotation) {
            return getDefaultState().withProperty(Properties.ROTATION, placer.getHorizontalFacing().getOpposite());
        }else{
            return getDefaultState();
        }
    }

    @Override
    protected BlockState createBlockState() {
        if(this instanceof IBlockWithRotation) {
            return new BlockState(this, Properties.ROTATION);
        }else{
            return new BlockState(this);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if(this instanceof IBlockWithRotation){
            return getDefaultState().withProperty(Properties.ROTATION, EnumFacing.getHorizontal(meta >> 2));
        }else{
            return getDefaultState();
        }

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(this instanceof IBlockWithRotation){
            return (((EnumFacing) state.getValue(Properties.ROTATION)).getHorizontalIndex() << 2);
        }else {
            return 0;
        }
    }
}
