package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHydraulicPiston extends HydraulicBlockContainerBase {

	public BlockHydraulicPiston() {
		super(Names.blockHydraulicPiston);
		hasFrontIcon = true;
		hasTextures = false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicPiston();
	}

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType(){
        return -1;
    }
	
	@Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return true;
    }
	
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if(tileEntity instanceof TileHydraulicPiston) {
            float extendedLength = ((TileHydraulicPiston)tileEntity).getExtendedLength();

            //Get rotation:
            int metadata = tileEntity.getBlockMetadata();
            float minX = 0.0F;
            float minY = 0.0F;
            float minZ = 0.0F;
            float maxX = 1.0F;
            float maxY = 1.0F;
            float maxZ = 1.0F;
            
            ForgeDirection dir = ForgeDirection.getOrientation(metadata);
            minX += extendedLength * (dir.offsetX < 0 ? dir.offsetX : 0);
            minY += extendedLength * (dir.offsetY < 0 ? dir.offsetY : 0);
            minZ += extendedLength * (dir.offsetZ < 0 ? dir.offsetZ : 0);
            
            maxX += extendedLength * (dir.offsetX > 0 ? dir.offsetX : 0);
            maxY += extendedLength * (dir.offsetY > 0 ? dir.offsetY : 0);
            maxZ += extendedLength * (dir.offsetZ > 0 ? dir.offsetZ : 0);
            
            
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }
    
    @SuppressWarnings("cast")
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		if(hasFrontIcon){
			int sideToPlace = MathHelper.floor_double((double)(player.rotationYaw / 90F) + 0.5D) & 3;
			
			
			int metaDataToSet = 0;
			switch(sideToPlace){
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
			
			TileEntity pEnt = world.getTileEntity(x, y, z);
			if(pEnt instanceof TileHydraulicPiston){
				((TileHydraulicPiston)pEnt).setFacing(ForgeDirection.getOrientation(metaDataToSet));
			}
		}
	}
    
/*
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity){
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

    	if(tileEntity instanceof TileHydraulicPiston) {
            float extendedLength = ((TileHydraulicPiston)tileEntity).getExtendedLength();

            //Get rotation:
            int metadata = tileEntity.getBlockMetadata();
            float minX = 0.0F;
            float minY = 0.0F;
            float minZ = 0.0F;
            float maxX = 1.0F;
            float maxY = 1.0F;
            float maxZ = 1.0F;
            
            ForgeDirection dir = ForgeDirection.getOrientation(metadata);
            minX -= extendedLength * (dir.offsetX < 0 ? dir.offsetX : 0);
            minY -= extendedLength * (dir.offsetY < 0 ? dir.offsetY : 0);
            minZ -= extendedLength * (dir.offsetZ < 0 ? dir.offsetZ : 0);
            
            maxX += extendedLength * (dir.offsetX > 0 ? dir.offsetX : 0);
            maxY += extendedLength * (dir.offsetY > 0 ? dir.offsetY : 0);
            maxZ += extendedLength * (dir.offsetZ > 0 ? dir.offsetZ : 0);
            
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
            //arraylist.add(arg0)
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
        }
    }*/
    
}
