package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketSetPressure extends AbstractPacket<PacketSetPressure> {

    private int     pressure;
    private boolean hasPressureGaugeInInventory;

    public PacketSetPressure() {

    }

    public PacketSetPressure(int pressure, boolean hasPressureGaugeInInventory) {

        this.pressure = pressure;
        this.hasPressureGaugeInInventory = hasPressureGaugeInInventory;
    }

    @Override
    public void toBytes(ByteBuf buffer) {

        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setInteger("pressure", pressure);
        toSend.setBoolean("hasPressureGaugeInInventory", hasPressureGaugeInInventory);
        ByteBufUtils.writeTag(buffer, toSend);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {

        NBTTagCompound received = ByteBufUtils.readTag(buffer);
        pressure = received.getInteger("pressure");
        hasPressureGaugeInInventory = received.getBoolean("hasPressureGaugeInInventory");
    }

    @Override
    public void handleClientSide(PacketSetPressure message, EntityPlayer player) {

        Hydraulicraft.pressure = message.pressure;
        Hydraulicraft.hasPressureGaugeInInventory = message.hasPressureGaugeInInventory;
    }

    @Override
    public void handleServerSide(PacketSetPressure message, EntityPlayer player) {

    }


}
