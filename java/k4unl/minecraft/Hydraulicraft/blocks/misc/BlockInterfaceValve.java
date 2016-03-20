package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.BlockConnectedTextureContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockInterfaceValve extends BlockConnectedTextureContainer {

    public BlockInterfaceValve() {

        super(Names.blockInterfaceValve);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {

        return new TileInterfaceValve();
    }

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.TANK;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            TileInterfaceValve valve = (TileInterfaceValve) worldIn.getTileEntity(pos);
            if (!valve.isValidTank()) {
                valve.checkTank(side);
            } else {
                playerIn.openGui(Hydraulicraft.instance, getGUIID().ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return false;
    }

}
