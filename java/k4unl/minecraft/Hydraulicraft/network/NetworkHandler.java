package k4unl.minecraft.Hydraulicraft.network;

import k4unl.minecraft.Hydraulicraft.network.packets.*;
import k4unl.minecraft.k4lib.lib.config.ModInfo;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Koen Beckers (K-4U)
 */
public class NetworkHandler extends k4unl.minecraft.k4lib.network.NetworkHandler{

    public static String getModId() {

        return ModInfo.LID;
    }

    /*
     * The integer is the ID of the message, the Side is the side this message will be handled (received) on!
     */
    public static void init() {

        INSTANCE.registerMessage(PacketKeyPressed.class, PacketKeyPressed.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(PacketPortalEnabled.class, PacketPortalEnabled.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketPortalStateChanged.class, PacketPortalStateChanged.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSetPressure.class, PacketSetPressure.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(PacketSpawnParticle.class, PacketSpawnParticle.class, discriminant++, Side.CLIENT);
    }
}
