package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static net.minecraft.inventory.EntityEquipmentSlot.HEAD;

public class BlockLight extends HydraulicBlockBase {

    public static final int maxLightLevel = (int) (0.9F * 15);


    public BlockLight() {

        super(Names.blockLight, Material.air, false);
        setLightOpacity(15);
        setTickRandomly(false);
        setDefaultState(this.blockState.getBaseState().withProperty(Properties.LIGHTVALUE, 15));
        setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public boolean getTickRandomly() {

        return true;
    }

    public int getRenderType() {

        return -1;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {

        return true;
    }

    @Override
    public boolean isCollidable() {

        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState worldIn, World pos, BlockPos state) {
        return new AxisAlignedBB(0f, 0f, 0f, 0f, 0f, 0f);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return new AxisAlignedBB(0f, 0f, 0f, 0f, 0f, 0f);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {

        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {

    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        //if(world.getBlock(x, y, z) instanceof BlockLight){
        //Seach within 3 blocks for a player.
        //If no player found. Remove the block
        EntityPlayer closestPlayer = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 15);
        if (closestPlayer == null) {
            worldIn.setBlockToAir(pos);
        } else {
            if (closestPlayer.getItemStackFromSlot(HEAD) != null) {
                if (closestPlayer.getItemStackFromSlot(HEAD).getItem() == HCItems.itemMiningHelmet) {
                    if (!ItemMiningHelmet.isPoweredOn(closestPlayer.getItemStackFromSlot(HEAD))) {
                        worldIn.setBlockToAir(pos);
                    }
                } else {
                    worldIn.setBlockToAir(pos);
                }
            } else {
                worldIn.setBlockToAir(pos);
            }
        }
        //}
        //world.scheduleBlockUpdate(x, y, z, HCBlocks.blockLight, 10);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

        worldIn.scheduleBlockUpdate(pos, HCBlocks.blockLight, 10, 10);
    }


    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {

        return getMetaFromState(world.getBlockState(pos));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return getDefaultState().withProperty(Properties.LIGHTVALUE, 15);
    }

    @Override
    protected BlockState createBlockState() {

        return new BlockState(this, Properties.LIGHTVALUE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(Properties.LIGHTVALUE, meta >> 2);

    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return state.getValue(Properties.LIGHTVALUE);
    }
}
