package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.k4lib.lib.Vector3fMax;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
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
    public boolean renderAsNormalBlock() {

        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
        TileEntity tileEntity = world.getTileEntity(x, y, z);

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
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity){
        TileEntity tileEntity = world.getTileEntity(x, y, z);

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

            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }else{
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            setBlockBounds(0,0,0,1,1,1);
        }
    }

    @SuppressWarnings("static-method")
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
        super.onBlockPlacedBy(world, x, y, z, player, iStack);
        TileEntity tile = world.getTileEntity(x, y, z);

        if(tile instanceof TileHarvesterFrame){
            checkRotation((TileHarvesterFrame)tile, player);
        }
    }

}
