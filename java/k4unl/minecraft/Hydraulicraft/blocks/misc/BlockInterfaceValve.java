package k4unl.minecraft.Hydraulicraft.blocks.misc;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.BlockConnectedTextureContainer;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererInterfaceValve;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInterfaceValve;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;

public class BlockInterfaceValve extends BlockConnectedTextureContainer {

	public BlockInterfaceValve() {
		super(Names.blockInterfaceValve);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileInterfaceValve();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float par7, float par8, float par9) {

		if(FMLCommonHandler.instance().getEffectiveSide().isServer()) {
			TileInterfaceValve valve = (TileInterfaceValve) world.getTileEntity(x, y, z);
			if(!valve.isValidTank()) {
				ForgeDirection s = ForgeDirection.getOrientation(side);
				valve.checkTank(s);
			}else{
				//player.addChatComponentMessage(new ChatComponentText("In this tank: "));
				FluidTankInfo tankInfo = valve.getTankInfo(ForgeDirection.UP)[0];
				if(tankInfo != null) {
					if(tankInfo.fluid == null){
						player.addChatComponentMessage(new ChatComponentText("Capacity: " + tankInfo.capacity));
					}else {
						player.addChatComponentMessage(new ChatComponentText(tankInfo.fluid.getFluid().getName()));
						player.addChatComponentMessage(new ChatComponentText(tankInfo.fluid.amount + "/" + tankInfo.capacity));
					}
				}
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {

		return RendererInterfaceValve.RENDER_ID;
	}

}
