package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.blocks;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IRotateableBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileHydraulicGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicGenerator extends HydraulicBlockContainerBase implements ITieredBlock, IRotateableBlock {

    public BlockHydraulicGenerator() {

        super(Names.blockHydraulicGenerator, true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicGenerator();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.GENERATOR;
    }

    public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir) {

        return true;
    }

    @Override
    public int getRenderType() {

        return -1;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity ent = worldIn.getTileEntity(pos);
        if (ent instanceof TileHydraulicGenerator) {
            if (stack != null) {
                ((TileHydraulicGenerator) ent).setFacing(placer.getHorizontalFacing());
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileHydraulicGenerator) {
            ((TileHydraulicGenerator) tile).checkRedstonePower();
        }
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {

        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileHydraulicGenerator) {
                if (side.equals(EnumFacing.UP) || side.equals(EnumFacing.DOWN)) {
                    TileHydraulicGenerator e = (TileHydraulicGenerator) te;
                    EnumFacing facing = e.getFacing();
                    e.setFacing(facing.rotateAround(side.getAxis()));
                    e.getHandler().updateBlock();
                    world.notifyNeighborsOfStateChange(pos, this);
                }
            }
        }

        return true;
    }

    @Override
    public PressureTier getTier() {

        return PressureTier.HIGHPRESSURE;
    }
}
