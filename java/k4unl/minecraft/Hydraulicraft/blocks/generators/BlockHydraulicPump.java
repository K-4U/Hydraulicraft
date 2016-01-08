package k4unl.minecraft.Hydraulicraft.blocks.generators;

import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHydraulicPump extends HydraulicTieredBlockBase implements IMultiTieredBlock {

    public BlockHydraulicPump() {

        super(Names.blockHydraulicPump);
        this.hasTextures = false;
        //setLightOpacity(255);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileHydraulicPump(((PressureTier)getStateFromMeta(metadata).getValue(TIER)).toInt());
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.PUMP;
    }
/*
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RendererHydraulicPump.RENDER_ID;
    }*/

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity entity = worldIn.getTileEntity(pos);
        if (entity != null && entity instanceof TileHydraulicPump) {
            ((TileHydraulicPump) entity).setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }

    @Override
    public PressureTier getTier(int damage) {

        return PressureTier.fromOrdinal(damage);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, BlockPos pos) {

        return getTierFromState(world.getBlockState(pos));
    }
}
