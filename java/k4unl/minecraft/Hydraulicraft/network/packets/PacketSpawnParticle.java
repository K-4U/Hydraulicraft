package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.k4lib.network.messages.LocationDoublePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

public class PacketSpawnParticle extends LocationDoublePacket<PacketSpawnParticle> {

    private double dx, dy, dz;
    private EnumParticleTypes particle;

    public PacketSpawnParticle() {
    }

    public PacketSpawnParticle(EnumParticleTypes _particle, double _x, double _y, double _z, double _dx, double _dy, double _dz) {
        super(_x, _y, _z);
        particle = _particle;
        dx = _dx;
        dy = _dy;
        dz = _dz;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeInt(particle.getParticleID());
        buffer.writeDouble(dx);
        buffer.writeDouble(dy);
        buffer.writeDouble(dz);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        particle = EnumParticleTypes.getParticleFromId(buffer.readInt());
        dx = buffer.readDouble();
        dy = buffer.readDouble();
        dz = buffer.readDouble();

    }

    @Override
    public void handleClientSide(PacketSpawnParticle message, EntityPlayer player) {
        player.worldObj.spawnParticle(message.particle, message.x, message.y, message.z, message.dx, message.dy, message.dz);
    }

    @Override
    public void handleServerSide(PacketSpawnParticle message, EntityPlayer player) {
    }

}
