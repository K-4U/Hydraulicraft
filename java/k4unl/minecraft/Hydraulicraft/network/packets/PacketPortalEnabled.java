package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalTeleporter;
import k4unl.minecraft.k4lib.network.messages.LocationIntPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketPortalEnabled extends LocationIntPacket<PacketPortalEnabled> {

    private EnumFacing baseDir;
    private EnumFacing portalDir;

    public PacketPortalEnabled() {

    }

    public PacketPortalEnabled(BlockPos pos, EnumFacing _baseDir, EnumFacing _portalDir) {

        super(pos);
        baseDir = _baseDir;
        portalDir = _portalDir;
    }

    @Override
    public void toBytes(ByteBuf buffer) {

        super.toBytes(buffer);
        ByteBufUtils.writeUTF8String(buffer, baseDir.toString());
        ByteBufUtils.writeUTF8String(buffer, portalDir.toString());
    }

    @Override
    public void fromBytes(ByteBuf buffer) {

        super.fromBytes(buffer);

        baseDir = EnumFacing.byName(ByteBufUtils.readUTF8String(buffer));
        portalDir = EnumFacing.byName(ByteBufUtils.readUTF8String(buffer));
    }

    @Override
    public void handleClientSide(PacketPortalEnabled message, EntityPlayer player) {

        if (player.worldObj.getTileEntity(message.pos) instanceof TilePortalTeleporter) {
            ((TilePortalTeleporter) player.worldObj.getTileEntity(message.pos)).setRotation(message.baseDir, message.portalDir);
        }
    }

    @Override
    public void handleServerSide(PacketPortalEnabled message, EntityPlayer player) {

    }

}
