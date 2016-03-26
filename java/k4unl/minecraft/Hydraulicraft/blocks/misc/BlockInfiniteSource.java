package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockInfiniteSource extends HydraulicBlockContainerBase {

    public BlockInfiniteSource() {

        super(Names.blockInfiniteSource, false);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {

        return new TileInfiniteSource();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INFINITESOURCE;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking())
            return false;


        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity == null || !(entity instanceof TileInfiniteSource)) {
            return false;

        }

        if (playerIn.getActiveItemStack() != null) {
            ItemStack inUse = playerIn.getActiveItemStack();
            FluidStack input = FluidContainerRegistry.getFluidForFilledItem(inUse);
            if (input != null) {

                if (!worldIn.isRemote) {
                    ((TileInfiniteSource) entity).fill(EnumFacing.UP, input, true);
                }
                return true;
            }
        }

        playerIn.openGui(Hydraulicraft.instance, getGUIID().ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
