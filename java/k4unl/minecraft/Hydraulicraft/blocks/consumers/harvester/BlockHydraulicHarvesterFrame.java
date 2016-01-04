package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockHydraulicHarvesterFrame extends HydraulicBlockContainerBase {
    public Vector3fMax blockBounds = new Vector3fMax(0.2f, 0.2f, 0.2f, 0.8F, 0.8F, 0.8F);

    public BlockHydraulicHarvesterFrame() {
        super(Names.blockHarvesterFrame, true);
        hasTextures = false;

    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileHarvesterFrame();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos){
        TileEntity tileEntity = world.getTileEntity(pos);

        if(tileEntity instanceof TileHarvesterFrame) {
            TileHarvesterFrame frame = (TileHarvesterFrame) tileEntity;
            float minX = blockBounds.getXMin();
            float minY = blockBounds.getYMin();
            float minZ = blockBounds.getZMin();
            float maxX = blockBounds.getXMax();
            float maxY = blockBounds.getYMax();
            float maxZ = blockBounds.getZMax();

            if(!frame.getIsRotated()){
                minX = 0.0F;
                maxX = 1.0F;
            }else{
                minZ = 0.0F;
                maxZ = 1.0F;
            }

            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }else{
            setBlockBounds(0,0,0,1,1,1);
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if(tileEntity instanceof TileHarvesterFrame) {
            TileHarvesterFrame frame = (TileHarvesterFrame) tileEntity;
            float minX = blockBounds.getXMin();
            float minY = blockBounds.getYMin();
            float minZ = blockBounds.getZMin();
            float maxX = blockBounds.getXMax();
            float maxY = blockBounds.getYMax();
            float maxZ = blockBounds.getZMax();

            if(!frame.getIsRotated()){
                minX = 0.0F;
                maxX = 1.0F;
            }else{
                minZ = 0.0F;
                maxZ = 1.0F;
            }

            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }else{
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            setBlockBounds(0,0,0,1,1,1);
        }
    }

    public void checkRotation(TileHarvesterFrame frame, EntityLivingBase player){
        int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;

        boolean isRotated = false;
        switch(sideToPlace){
            case 0:
                isRotated = true; //C
                break;
            case 1:
                isRotated = false; //C
                break;
            case 2:
                isRotated = true; // C
                break;
            case 3:
                isRotated = false; //C
                break;
        }

        frame.setRotated(isRotated);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tile = worldIn.getTileEntity(pos);

        if(tile instanceof TileHarvesterFrame){
            checkRotation((TileHarvesterFrame)tile, placer);
        }
    }

}
