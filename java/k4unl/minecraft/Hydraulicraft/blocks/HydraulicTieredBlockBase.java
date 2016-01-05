package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

public abstract class HydraulicTieredBlockBase extends HydraulicBlockContainerBase {
        /*private IIcon[] tieredIcon;
		private IIcon[] tieredTopIcon;
		private IIcon[] tieredBottomIcon;*/

    private Name[] mName;

    protected boolean hasTopIcon = false;
    protected boolean hasBottomIcon = false;
    protected boolean hasTextures = true;

    public static final PropertyEnum TIER = PropertyEnum.create("tier", PressureTier.class);

    @Override
    public abstract TileEntity createNewTileEntity(World world, int var2);

    protected HydraulicTieredBlockBase(Name[] machineName) {
        super(machineName[0], true);

        mName = machineName;
/*
        tieredIcon = new IIcon[3];
        tieredTopIcon = new IIcon[3];
        tieredBottomIcon = new IIcon[3];*/
        if(this instanceof IBlockWithRotation){
            setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, EnumFacing.NORTH).withProperty(TIER, PressureTier.INVALID));
        }else{
            setDefaultState(this.blockState.getBaseState().withProperty(TIER, PressureTier.INVALID));
        }


        setUnlocalizedName(mName[0].unlocalized);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5F);

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }


    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

/*
    private String getTieredTextureName(String side, int tier) {
        if (!side.equals("")) {
            return ModInfo.LID + ":" + mName[0].unlocalized + "_" + tier + "_" + side;
        } else {
            return ModInfo.LID + ":" + mName[0].unlocalized + "_" + tier;
        }
    }
		
    /*@Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        if(hasTextures){
            if(hasTopIcon || hasBottomIcon){
                for(int i = 0; i < 3; i++){
                    tieredIcon[i] = iconRegistry.registerIcon(getTieredTextureName("sides",i));
                    if(hasTopIcon){
                        tieredTopIcon[i] = iconRegistry.registerIcon(getTieredTextureName("top",i));
                    }else{
                        tieredTopIcon[i] = tieredIcon[i];
                    }
                    if(hasBottomIcon){
                        tieredBottomIcon[i] = iconRegistry.registerIcon(getTieredTextureName("bottom",i));
                    }else{
                        tieredBottomIcon[i] = tieredIcon[i];
                    }
                }
            }else{
                for(int i = 0; i < 3; i++){
                    tieredIcon[i] = iconRegistry.registerIcon(getTieredTextureName("",i));
                    tieredBottomIcon[i] = tieredIcon[i];
                    tieredTopIcon[i] = tieredIcon[i];
                }
            }
        }else{
            for(int i = 0; i < 3; i++){
                tieredIcon[i] = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
                tieredBottomIcon[i] = tieredIcon[i];
                tieredTopIcon[i] = tieredIcon[i];
            }
        }
    }


    @Override
    public IIcon getIcon(int side, int metadata){
        EnumFacing s = EnumFacing.getOrientation(side);
        if(s.equals(EnumFacing.UP)){
            return tieredTopIcon[metadata];
        }else if(s.equals(EnumFacing.DOWN)){
            return tieredBottomIcon[metadata];
        }

        return tieredIcon[metadata];

    }
    */

    @Override
    public int damageDropped(IBlockState state) {
        return getTierFromState(state).toInt();
    }

    @Override
    protected BlockState createBlockState() {
        if(this instanceof IBlockWithRotation) {
            return new BlockState(this, TIER, ROTATION);
        }else{
            return new BlockState(this, TIER);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if(this instanceof IBlockWithRotation) {
            return super.getStateFromMeta(meta).withProperty(TIER, PressureTier.fromOrdinal(meta & 3));
        }else{
            return getDefaultState().withProperty(TIER, PressureTier.fromOrdinal(meta & 3));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(this instanceof IBlockWithRotation) {
            return super.getMetaFromState(state) + ((PressureTier) state.getValue(TIER)).toInt();
        }else{
            return ((PressureTier) state.getValue(TIER)).toInt();
        }
    }

    public PressureTier getTierFromState(IBlockState state) {
        return (PressureTier) state.getValue(TIER);
    }
}
