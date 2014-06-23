package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import k4unl.minecraft.Hydraulicraft.network.LocationDoublePacket;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketSpawnParticle extends LocationDoublePacket {

	private double dx, dy, dz;
	private String particleName;
	
	public PacketSpawnParticle(){}
	
	public PacketSpawnParticle(String _particleName, double _x, double _y, double _z, double _dx, double _dy, double _dz){
		super(_x, _y, _z);
		particleName = _particleName;
		dx = _dx;
		dy = _dy;
		dz = _dz;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		super.encodeInto(ctx, buffer);
		ByteBufUtils.writeUTF8String(buffer, particleName);
		buffer.writeDouble(dx);
		buffer.writeDouble(dy);
		buffer.writeDouble(dz);
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
		super.decodeInto(ctx, buffer);
		particleName = ByteBufUtils.readUTF8String(buffer);
		dx = buffer.readDouble();
		dy = buffer.readDouble();
		dz = buffer.readDouble();
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		player.worldObj.spawnParticle(particleName, x, y, z, dx, dy, dz);
	}

	@Override
	public void handleServerSide(EntityPlayer player) { }

}
