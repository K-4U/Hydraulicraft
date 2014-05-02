package k4unl.minecraft.Hydraulicraft.blocks.transporter;

import java.util.List;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.TileEntities.transporter.TilePressureHose;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHose extends MachineTieredBlock {

	private static Vector3fMax blockBounds = new Vector3fMax(0.3f, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f);
	
	public BlockHose() {
		super(Names.partHose);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		TilePressureHose pHose = new TilePressureHose(metadata);
		return pHose;
	}

	@Override
	@SideOnly(Side.CLIENT)
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
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
		TileEntity t = world.getTileEntity(x, y, z);
		if (t instanceof TilePressureHose) {
			((TilePressureHose) t).refreshConnectedSides();
		}
	}

	private boolean isDir(Map<ForgeDirection, TileEntity> connectedSides, ForgeDirection dir){
		return connectedSides.containsKey(dir);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y,
			int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity instanceof TilePressureHose) {
			Map<ForgeDirection, TileEntity> connectedSides = ((TilePressureHose) tileEntity).getConnectedSides();

			if (connectedSides != null) {
				float minX = blockBounds.getXMin();
				float minY = blockBounds.getYMin();
				float minZ = blockBounds.getZMin();
				float maxX = blockBounds.getXMax();
				float maxY = blockBounds.getYMax();
				float maxZ = blockBounds.getZMax();

				if (isDir(connectedSides, ForgeDirection.UP))
					maxY = 1.0F;
				if (isDir(connectedSides, ForgeDirection.DOWN))
					minY = 0.0F;

				if (isDir(connectedSides, ForgeDirection.WEST))
					minX = 0.0F;
				if (isDir(connectedSides, ForgeDirection.EAST))
					maxX = 1.0F;

				if (isDir(connectedSides, ForgeDirection.NORTH))
					minZ = 0.0F;
				if (isDir(connectedSides, ForgeDirection.SOUTH))
					maxZ = 1.0F;

				this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
			}
		}
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity){
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if(tileEntity instanceof TilePressureHose) {
            Map<ForgeDirection, TileEntity> connectedSides = ((TilePressureHose)tileEntity).getConnectedSides();
            if(connectedSides != null) {
            	float minX = blockBounds.getXMin();
				float minY = blockBounds.getYMin();
				float minZ = blockBounds.getZMin();
				float maxX = blockBounds.getXMax();
				float maxY = blockBounds.getYMax();
				float maxZ = blockBounds.getZMax();

                setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);

                if(isDir(connectedSides, ForgeDirection.EAST)) {
                    setBlockBounds(0.0F, minY, minZ, maxX, maxY, maxZ);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
                }

                if(isDir(connectedSides, ForgeDirection.WEST)) {
                    setBlockBounds(minX, minY, minZ, 1.0F, maxY, maxZ);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
                }

                if(isDir(connectedSides, ForgeDirection.SOUTH)) {
                	setBlockBounds(minX, minY, minZ, maxX, maxY, 1.0F);
                    
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
                }
                if(isDir(connectedSides, ForgeDirection.NORTH)) {
                	setBlockBounds(minX, minY, 0.0F, maxX, maxY, maxZ);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
                }
                if(isDir(connectedSides, ForgeDirection.UP)) {
                    setBlockBounds(minX, minY, minZ, maxX, 1.0F, maxZ);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
                }
                if(isDir(connectedSides, ForgeDirection.DOWN)) {
                    setBlockBounds(minX, 0.0F, minZ, maxX, maxY, maxZ);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
                }
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
