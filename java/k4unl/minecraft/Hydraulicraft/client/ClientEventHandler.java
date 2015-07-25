package k4unl.minecraft.Hydraulicraft.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingSuit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientEventHandler {

    public static void init(){
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }


    @SubscribeEvent
    public void fogDensityEvent(EntityViewRenderEvent.FogDensity event){
        if(ItemDivingSuit.isWearingFullSuit(Minecraft.getMinecraft().thePlayer)){
            event.density=0.1F;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void fogColors(EntityViewRenderEvent.FogColors event){
        if(ItemDivingSuit.isWearingFullSuit(Minecraft.getMinecraft().thePlayer)){
            //223 31 53
            event.red = 223F / 255F;
            event.green = 31F / 255F;
            event.blue = 53F / 255F;
        }
    }
}
