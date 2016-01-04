package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.lib.Functions;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.k4lib.network.messages.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;

public class PacketKeyPressed extends AbstractPacket<PacketKeyPressed> {
	private int keyIndex;

    public PacketKeyPressed(){}

    public PacketKeyPressed(int keyPressedIndex){
    	keyIndex = keyPressedIndex;
    }

    @Override
    public void toBytes(ByteBuf buffer){
        buffer.writeInt(keyIndex);
    }

    @Override
    public void fromBytes(ByteBuf buffer){
        keyIndex = buffer.readInt();
    }

    @Override
    public void handleClientSide(PacketKeyPressed message, EntityPlayer player){}

    @Override
    public void handleServerSide(PacketKeyPressed message, EntityPlayer player){
		switch(message.keyIndex){
		case Constants.KEYS_MINING_HELMET:
			if(player.getCurrentArmor(3) != null){
				if(player.getCurrentArmor(3).getItem() instanceof ItemMiningHelmet){
					ItemMiningHelmet.togglePower(player.getCurrentArmor(3));
					Functions.showMessageInChat(player, "Helmet is now " + (ItemMiningHelmet.isPoweredOn(player.getCurrentArmor(3)) ? "on" : "off"));
				}
			}
			break;
		}
    }
}
