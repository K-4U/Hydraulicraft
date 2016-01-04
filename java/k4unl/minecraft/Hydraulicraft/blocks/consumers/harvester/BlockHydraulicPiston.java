package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;


public class BlockHydraulicPiston extends HydraulicBlockContainerBase {

    public BlockHydraulicPiston() {
        super(Names.blockHydraulicPiston, true);
        hasFrontIcon = true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileHydraulicPiston();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return RendererHydraulicPiston.RENDER_ID;
    }
*/
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (hasFrontIcon) {

            TileEntity pEnt = worldIn.getTileEntity(pos);
            if (pEnt instanceof TileHydraulicPiston) {
                ((TileHydraulicPiston) pEnt).setFacing(placer.getHorizontalFacing().getOpposite());
            }
            super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        }
    }
}
