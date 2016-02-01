package k4unl.minecraft.Hydraulicraft.network;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketKeyPressed;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketPortalEnabled;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketSetPressure;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketSpawnParticle;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Koen Beckers (K-4U)
 */
public class NetworkHandler extends k4unl.minecraft.k4lib.network.NetworkHandler {
    public static NetworkHandler INSTANCE;

    public NetworkHandler() {
        INSTANCE = this;
    }

    @Override
    public String getModId() {
        return ModInfo.LID;
    }

    @Override
    public void init() {
        getChannel().registerMessage(PacketKeyPressed.class, PacketKeyPressed.class, discriminant++, Side.SERVER);
        getChannel().registerMessage(PacketPortalEnabled.class, PacketPortalEnabled.class, discriminant++, Side.CLIENT);
        getChannel().registerMessage(PacketSetPressure.class, PacketSetPressure.class, discriminant++, Side.CLIENT);
        getChannel().registerMessage(PacketSpawnParticle.class, PacketSpawnParticle.class, discriminant++, Side.CLIENT);
    }
}
