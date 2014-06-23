package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import k4unl.minecraft.Hydraulicraft.network.LocationIntPacket;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPortalStateChanged extends LocationIntPacket {
	private boolean isActive;
	
	public PacketPortalStateChanged(){}
	public PacketPortalStateChanged(int x, int y, int z, boolean _isActive){
		super(x, y, z);
		isActive = _isActive;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		super.encodeInto(ctx, buffer);
		buffer.writeBoolean(isActive);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		super.decodeInto(ctx, buffer);
		isActive = buffer.readBoolean();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		if(player.worldObj.getTileEntity(x, y, z) instanceof TilePortalFrame){
			((TilePortalFrame)player.worldObj.getTileEntity(x, y, z)).setActive(isActive);
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player) {

	}

}
