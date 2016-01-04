package k4unl.minecraft.Hydraulicraft.blocks.gow;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockPortalFrame extends GOWBlockRendering {
    private static Vector3fMax blockBounds = new Vector3fMax(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);

    public BlockPortalFrame() {

        super(Names.portalFrame.unlocalized);
        setCreativeTab(CustomTabs.tabGOW);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {

        TileEntity ent = worldIn.getTileEntity(pos);

        if (ent instanceof TilePortalFrame) {
            TilePortalFrame frame = (TilePortalFrame) ent;
            Vector3fMax vector = blockBounds.copy();
            if (frame.isConnectedTo(EnumFacing.UP))
                vector.setYMax(1.0F);
            if (frame.isConnectedTo(EnumFacing.DOWN))
                vector.setYMin(0.0F);

            if (frame.isConnectedTo(EnumFacing.EAST))
                vector.setXMax(1.0F);
            if (frame.isConnectedTo(EnumFacing.WEST))
                vector.setXMin(0.0F);

            if (frame.isConnectedTo(EnumFacing.SOUTH))
                vector.setZMax(1.0F);
            if (frame.isConnectedTo(EnumFacing.NORTH))
                vector.setZMin(0.0F);


            this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        TileEntity ent = worldIn.getTileEntity(pos);

        if (ent instanceof TilePortalFrame) {
            TilePortalFrame frame = (TilePortalFrame) ent;
            Vector3fMax vector = blockBounds.copy();
            if (frame.isConnectedTo(EnumFacing.UP))
                vector.setYMax(1.0F);
            if (frame.isConnectedTo(EnumFacing.DOWN))
                vector.setYMin(0.0F);

            if (frame.isConnectedTo(EnumFacing.WEST))
                vector.setXMin(0.0F);
            if (frame.isConnectedTo(EnumFacing.EAST))
                vector.setXMax(1.0F);

            if (frame.isConnectedTo(EnumFacing.SOUTH))
                vector.setZMax(1.0F);
            if (frame.isConnectedTo(EnumFacing.NORTH))
                vector.setZMin(0.0F);


            this.setBlockBounds(vector.getXMin(), vector.getYMin(), vector.getZMin(), vector.getXMax(), vector.getYMax(), vector.getZMax());
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

        return new TilePortalFrame();
    }
}
