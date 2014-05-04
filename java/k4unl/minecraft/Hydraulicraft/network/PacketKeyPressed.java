package k4unl.minecraft.Hydraulicraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class PacketKeyPressed extends AbstractPacket{
	private int keyIndex;

    public PacketKeyPressed(){}

    public PacketKeyPressed(int keyPressedIndex){
    	keyIndex = keyPressedIndex;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        buffer.writeInt(keyIndex);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer){
        keyIndex = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player){}

    @Override
    public void handleServerSide(EntityPlayer player){
		switch(keyIndex){
		case Constants.KEYS_MINING_HELMET:
			Log.info("Key pressed!");
			if(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3) != null){
				if(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3).getItem() instanceof ItemMiningHelmet){
					ItemMiningHelmet.togglePower(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3));
					Functions.showMessageInChat(Minecraft.getMinecraft().thePlayer, "Helmet is now " + (ItemMiningHelmet.isPoweredOn(Minecraft.getMinecraft().thePlayer.getCurrentArmor(3)) ? "on" : "off"));
				}
			}
			break;
		}
    }

}
