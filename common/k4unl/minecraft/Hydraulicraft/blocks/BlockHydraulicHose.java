package k4unl.minecraft.Hydraulicraft.blocks;

import java.util.Map;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import codechicken.lib.vec.Vector3;

public class BlockHydraulicHose extends MachineTieredBlock {
	public Vector3 minVector = new Vector3(0.35, 0.35, 0.35);
    public Vector3 maxVector = new Vector3(0.65, 0.65, 0.65);
	
    protected BlockHydraulicHose() {
        super(Ids.blockHydraulicHose, Names.blockHydraulicHose);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileHydraulicHose();
    }

    @Override
    public int getRenderType(){
        return -1;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    private boolean isDir(Map<ForgeDirection, TileEntity> connectedSides, ForgeDirection dir){
    	return connectedSides.containsKey(dir);
    }

	@Override
	
	public void onNeighborBlockChange(World world, int x, int y,
				int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof TileHydraulicHose){
        	((TileHydraulicHose) tileEntity).checkConnectedSides();
        }
	}
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
    	TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof TileHydraulicHose){
        	Map<ForgeDirection, TileEntity> connectedSides = ((TileHydraulicHose) tileEntity).getConnectedSides();

            if (connectedSides != null){
                float minX = (float) this.minVector.x;
                float minY = (float) this.minVector.y;
                float minZ = (float) this.minVector.z;
                float maxX = (float) this.maxVector.x;
                float maxY = (float) this.maxVector.y;
                float maxZ = (float) this.maxVector.z;

                if(isDir(connectedSides, ForgeDirection.UP)) maxY = 1.0F;
                if(isDir(connectedSides, ForgeDirection.DOWN)) minY = 0.0F;
                
                if(isDir(connectedSides, ForgeDirection.WEST)) maxX = 1.0F;
                if(isDir(connectedSides, ForgeDirection.EAST)) minX = 0.0F;

                if(isDir(connectedSides, ForgeDirection.NORTH)) maxZ = 1.0F;
                if(isDir(connectedSides, ForgeDirection.SOUTH)) minZ = 0.0F;
                
                this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
            }
        }
    }
    
    @Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		super.onBlockPlacedBy(world, x, y, z, player, iStack);
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof TileHydraulicHose){
        	((TileHydraulicHose) tileEntity).checkConnectedSides();
        }
	}

}
