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
import net.minecraft.util.EnumFacing;

import java.util.List;

public class SubBlockBase extends HydraulicBlockBase {
    //private List<IIcon> icons;
    /*private List<Icon> topIcons;
    private List<Icon> bottomIcons;
	*/
    private Name[] mName;

    public static final PropertyEnum TIER = PropertyEnum.create("tier", PressureTier.class);

    protected SubBlockBase(Name[] machineName) {
        super(machineName[0], true);

        mName = machineName;

        //icons = new ArrayList<IIcon>();
        //topIcons = new ArrayList<Icon>();
        //bottomIcons = new ArrayList<Icon>();
        if(this instanceof IBlockWithRotation) {
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
    public void getSubBlocks(Item block, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }


    /*    private String getTextureName(String side, int subId) {
            if (!side.equals("")) {
                return ModInfo.LID + ":" + mName[subId].unlocalized + "_" + side;
            } else {
                return ModInfo.LID + ":" + mName[subId].unlocalized;
            }
        }

        @Override
        public void registerBlockIcons(IIconRegister iconRegistry) {
            for (int i = 0; i < mName.length; i++) {
                icons.add(i, iconRegistry.registerIcon(getTextureName("", i)));
            }
        }


        @Override
        public IIcon getIcon(int side, int metadata) {
            if (metadata >= icons.size()) {
                metadata = 0;
            }
            return icons.get(metadata);

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
        return getDefaultState().withProperty(TIER, PressureTier.fromOrdinal(meta & 3));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((PressureTier) state.getValue(TIER)).toInt();
    }

    public PressureTier getTierFromState(IBlockState state) {
        return (PressureTier) state.getValue(TIER);
    }

}
