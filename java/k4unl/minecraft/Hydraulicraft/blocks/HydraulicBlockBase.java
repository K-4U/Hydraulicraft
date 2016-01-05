package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HydraulicBlockBase extends Block {
    /*private IIcon blockIcon;
    private IIcon topIcon;
    private IIcon bottomIcon;
    private IIcon frontIcon;
    private IIcon leftIcon;
    private IIcon rightIcon;
    private IIcon backIcon;*/

    public Name mName;

    protected boolean hasBottomIcon = false;
    protected boolean hasTopIcon = false;
    protected boolean hasFrontIcon = false;
    protected boolean hasLeftIcon = false;
    protected boolean hasRightIcon = false;
    protected boolean hasBackIcon = false;

    protected boolean hasTextures = true;

    public static final PropertyEnum ROTATION = PropertyDirection.create("rotation", EnumFacing.Plane.HORIZONTAL);

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
            setDefaultState(this.blockState.getBaseState().withProperty(ROTATION, EnumFacing.NORTH));
        }

        if (addToTab) {
            setCreativeTab(CustomTabs.tabHydraulicraft);
        }
    }


    protected String getTextureName(String side) {
        if (side != null) {
            return ModInfo.LID + ":" + mName.unlocalized + "_" + side;
        } else {
            return ModInfo.LID + ":" + mName.unlocalized;
        }
    }


    /*@Override
    public void registerBlockIcons(IIconRegister iconRegistry) {
        if (hasTextures) {
            if (hasTopIcon || hasBottomIcon || hasFrontIcon || hasLeftIcon || hasRightIcon) {
                if (!(hasTopIcon && hasBottomIcon && hasFrontIcon && hasLeftIcon && hasRightIcon)) {
                    blockIcon = iconRegistry.registerIcon(getTextureName("sides"));
                }

                if (hasTopIcon) {
                    topIcon = iconRegistry.registerIcon(getTextureName("top"));
                } else {
                    topIcon = blockIcon;
                }
                if (hasBottomIcon) {
                    bottomIcon = iconRegistry.registerIcon(getTextureName("bottom"));
                } else {
                    bottomIcon = blockIcon;
                }
                if (hasFrontIcon) {
                    frontIcon = iconRegistry.registerIcon(getTextureName("front"));
                } else {
                    frontIcon = blockIcon;
                }
                if (hasLeftIcon) {
                    leftIcon = iconRegistry.registerIcon(getTextureName("left"));
                } else {
                    leftIcon = blockIcon;
                }
                if (hasRightIcon) {
                    rightIcon = iconRegistry.registerIcon(getTextureName("right"));
                } else {
                    rightIcon = blockIcon;
                }
                if (hasBackIcon) {
                    backIcon = iconRegistry.registerIcon(getTextureName("back"));
                } else {
                    backIcon = blockIcon;
                }
            } else {
                blockIcon = iconRegistry.registerIcon(getTextureName(null));
                bottomIcon = blockIcon;
                topIcon = blockIcon;
                frontIcon = blockIcon;
                leftIcon = blockIcon;
                rightIcon = blockIcon;
                backIcon = blockIcon;
            }
        } else {
            blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
        }
    }*/


    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (hasFrontIcon || hasBackIcon || hasLeftIcon || hasRightIcon) {
            //setDefaultDirection(worldIn, pos);
        }
    }

    /*
    private void setDefaultDirection(World world, int x, int y, int z) {
        if (!world.isRemote) { //Client, not server
            Block zm1 = world.getBlock(x, y, z - 1);
            Block zp1 = world.getBlock(x, y, z + 1);
            Block xm1 = world.getBlock(x - 1, y, z);
            Block xp1 = world.getBlock(x + 1, y, z);

            int metaDataToSet = 3;

            if (zm1.func_149730_j() && !zp1.func_149730_j()) {
                metaDataToSet = 3;
            }

            if (zp1.func_149730_j() && !zm1.func_149730_j()) {
                metaDataToSet = 2;
            }

            if (xm1.func_149730_j() && !xp1.func_149730_j()) {
                metaDataToSet = 5;
            }

            if (xp1.func_149730_j() && !xm1.func_149730_j()) {
                metaDataToSet = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
        }
    }*/


    /*@Override
    public IIcon getIcon(int side, int metadata) {
        Orientation or = Orientation.calculateOrientation(side, metadata);
        switch (or) {
            case FRONT:
                return frontIcon;
            case LEFT:
                return leftIcon;
            case RIGHT:
                return rightIcon;
            case BACK:
                return backIcon;
            case TOP:
                return topIcon;
            case BOTTOM:
                return bottomIcon;
        }

        return blockIcon;
    }*/

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(ROTATION, placer.getHorizontalFacing().getOpposite());
    }
/*
    @SuppressWarnings("cast")
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack) {
        if (hasFrontIcon || hasBackIcon || hasLeftIcon || hasRightIcon) {
            int sideToPlace = MathHelper.floor_double((double) (player.rotationYaw / 90F) + 0.5D) & 3;

            int metaDataToSet = 0;
            switch (sideToPlace) {
                case 0:
                    metaDataToSet = 2;
                    break;
                case 1:
                    metaDataToSet = 5;
                    break;
                case 2:
                    metaDataToSet = 3;
                    break;
                case 3:
                    metaDataToSet = 4;
                    break;
            }

            world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
        }
    }*/

    @Override
    protected BlockState createBlockState() {
        if(this instanceof IBlockWithRotation) {
            return new BlockState(this, ROTATION);
        }else{
            return new BlockState(this);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if(this instanceof IBlockWithRotation){
            return getDefaultState().withProperty(ROTATION, EnumFacing.getHorizontal(meta >> 2));
        }else{
            return getDefaultState();
        }

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(this instanceof IBlockWithRotation){
            return (((EnumFacing) state.getValue(ROTATION)).getHorizontalIndex() << 2);
        }else {
            return 0;
        }
    }
}
