package k4unl.minecraft.Hydraulicraft.blocks.rubberHarvesting;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.Properties;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.rubberHarvesting.TileRubberTap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockRubberTap extends HydraulicBlockContainerBase implements IBlockWithRotation {

    //TODO: Collision boxes
    public BlockRubberTap() {

        super(Names.blockRubberTap, Material.iron, true);
        setSoundType(SoundType.ANVIL);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileRubberTap();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return getDefaultState().withProperty(Properties.ROTATION, placer.getHorizontalFacing());
    }
}
