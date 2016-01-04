package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.BlockConnectedTextureContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTankInfo;
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

        return GuiIDs.INVALID;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            TileInterfaceValve valve = (TileInterfaceValve) worldIn.getTileEntity(pos);
            if (!valve.isValidTank()) {
                valve.checkTank(side);
            } else {
                //player.addChatComponentMessage(new ChatComponentText("In this tank: "));
                FluidTankInfo tankInfo = valve.getTankInfo(EnumFacing.UP)[0];
                if (tankInfo != null) {
                    if (tankInfo.fluid == null) {
                        playerIn.addChatComponentMessage(new ChatComponentText("Capacity: " + tankInfo.capacity));
                    } else {
                        playerIn.addChatComponentMessage(new ChatComponentText(tankInfo.fluid.getFluid().getName()));
                        playerIn.addChatComponentMessage(new ChatComponentText(tankInfo.fluid.amount + "/" + tankInfo.capacity));
                    }
                }
            }
        }
        return false;
    }
/*
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RendererInterfaceValve.RENDER_ID;
    }*/

}
