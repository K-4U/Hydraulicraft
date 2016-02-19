package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IConnectTexture;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockConnectedTexture extends HydraulicBlockBase {

    public static int[] iconRefByID = { 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2,
            27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5,
            4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 16, 16, 20, 20, 16, 16, 28, 28, 21, 21, 46, 42, 21, 21, 43, 38, 4, 4, 5, 5, 4, 4,
            5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 16, 16, 20, 20, 16, 16, 28, 28, 25, 25, 45, 37, 25, 25, 40, 32, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3,
            19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19,
            15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5, 4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 7, 7, 24,
            24, 7, 7, 10, 10, 29, 29, 44, 41, 29, 29, 39, 33, 4, 4, 5, 5, 4, 4, 5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 7, 7, 24, 24, 7, 7, 10,
            10, 8, 8, 36, 35, 8, 8, 34, 11 };

    protected BlockConnectedTexture(Name machineName, Material material) {

        super(machineName, material, true);
    }

    public int getIconForFace(IBlockAccess world, int x, int y, int z, EnumFacing side) {

        boolean[] bitMatrix = new boolean[8];

        if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
            bitMatrix[0] = connectTo(world, x - 1, y, z - 1);
            bitMatrix[1] = connectTo(world, x, y, z - 1);
            bitMatrix[2] = connectTo(world, x + 1, y, z - 1);
            bitMatrix[3] = connectTo(world, x - 1, y, z);
            bitMatrix[4] = connectTo(world, x + 1, y, z);
            bitMatrix[5] = connectTo(world, x - 1, y, z + 1);
            bitMatrix[6] = connectTo(world, x, y, z + 1);
            bitMatrix[7] = connectTo(world, x + 1, y, z + 1);
        }
        if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
            bitMatrix[0] = connectTo(world, x + (side == EnumFacing.NORTH ? 1 : -1), y + 1, z);
            bitMatrix[1] = connectTo(world, x, y + 1, z);
            bitMatrix[2] = connectTo(world, x + (side == EnumFacing.SOUTH ? 1 : -1), y + 1, z);
            bitMatrix[3] = connectTo(world, x + (side == EnumFacing.NORTH ? 1 : -1), y, z);
            bitMatrix[4] = connectTo(world, x + (side == EnumFacing.SOUTH ? 1 : -1), y, z);
            bitMatrix[5] = connectTo(world, x + (side == EnumFacing.NORTH ? 1 : -1), y - 1, z);
            bitMatrix[6] = connectTo(world, x, y - 1, z);
            bitMatrix[7] = connectTo(world, x + (side == EnumFacing.SOUTH ? 1 : -1), y - 1, z);
        }
        if (side == EnumFacing.WEST || side == EnumFacing.EAST) {
            bitMatrix[0] = connectTo(world, x, y + 1, z + (side == EnumFacing.EAST ? 1 : -1));
            bitMatrix[1] = connectTo(world, x, y + 1, z);
            bitMatrix[2] = connectTo(world, x, y + 1, z + (side == EnumFacing.WEST ? 1 : -1));
            bitMatrix[3] = connectTo(world, x, y, z + (side == EnumFacing.EAST ? 1 : -1));
            bitMatrix[4] = connectTo(world, x, y, z + (side == EnumFacing.WEST ? 1 : -1));
            bitMatrix[5] = connectTo(world, x, y - 1, z + (side == EnumFacing.EAST ? 1 : -1));
            bitMatrix[6] = connectTo(world, x, y - 1, z);
            bitMatrix[7] = connectTo(world, x, y - 1, z + (side == EnumFacing.WEST ? 1 : -1));
        }

        int idBuilder = 0;

        for (int i = 0; i <= 7; i++)
            idBuilder = idBuilder + (bitMatrix[i] ? (i << 1) : 0);

        return idBuilder > 255 || idBuilder < 0 ? 0 : iconRefByID[idBuilder];
    }


    public boolean connectTo(IBlockAccess world, int x, int y, int z) {

        BlockPos pos = new BlockPos(x, y, z);
        if (world.getBlockState(pos).getBlock() == this) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            if (te instanceof IConnectTexture) {
                return ((IConnectTexture) te).connectTexture();
            }
        }
        return false;
    }
}
