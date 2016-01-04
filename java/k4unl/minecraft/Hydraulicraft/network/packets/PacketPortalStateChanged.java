package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.Hydraulicraft.tileEntities.gow.TilePortalFrame;
import k4unl.minecraft.k4lib.network.messages.LocationIntPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class PacketPortalStateChanged extends LocationIntPacket<PacketPortalStateChanged> {
    private boolean isActive;

    public PacketPortalStateChanged() {
    }

    public PacketPortalStateChanged(BlockPos pos, boolean isActive) {
        super(pos);
        this.isActive = isActive;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeBoolean(isActive);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        isActive = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(PacketPortalStateChanged message, EntityPlayer player) {
        if (player.worldObj.getTileEntity(message.pos) instanceof TilePortalFrame) {
            ((TilePortalFrame) player.worldObj.getTileEntity(message.pos)).setActive(message.isActive);
        }
    }

    @Override
    public void handleServerSide(PacketPortalStateChanged message, EntityPlayer player) {

    }

}
