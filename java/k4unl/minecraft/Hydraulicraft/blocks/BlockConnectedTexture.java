package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.tileEntities.interfaces.IConnectTexture;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockConnectedTexture extends HydraulicBlockBase {

    protected BlockConnectedTexture(Name machineName, Material material) {

        super(machineName, material, true);
    }


    public boolean connectTo(IBlockAccess world, BlockPos pos, EnumFacing dir) {

        pos = pos.offset(dir);
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
