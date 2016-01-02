package k4unl.minecraft.Hydraulicraft.network.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.network.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketSetPressure extends AbstractPacket {

    private int pressure;
    private boolean hasPressureGaugeInInventory;
    public PacketSetPressure() {
    }

    public PacketSetPressure(int pressure, boolean hasPressureGaugeInInventory) {
        this.pressure = pressure;
        this.hasPressureGaugeInInventory = hasPressureGaugeInInventory;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setInteger("pressure", pressure);
        toSend.setBoolean("hasPressureGaugeInInventory", hasPressureGaugeInInventory);
        ByteBufUtils.writeTag(buffer, toSend);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        NBTTagCompound received = ByteBufUtils.readTag(buffer);
        pressure = received.getInteger("pressure");
        hasPressureGaugeInInventory = received.getBoolean("hasPressureGaugeInInventory");
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        Hydraulicraft.pressure = pressure;
        Hydraulicraft.hasPressureGaugeInInventory = hasPressureGaugeInInventory;
    }

    @Override
    public void handleServerSide(EntityPlayer player) { }


}
