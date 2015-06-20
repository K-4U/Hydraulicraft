package k4unl.minecraft.Hydraulicraft.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.items.scubaSuit.ItemScubaSuit;

public class TickHandler {
	
	public static void init(){
		FMLCommonHandler.instance().bus().register(new TickHandler());
	}
	
    @SubscribeEvent
    public void tickWorld(TickEvent.WorldTickEvent event){
        if(event.phase == TickEvent.Phase.END) {
            //World world = event.world;
        }
    }
    
    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.END) {
        	if(event.side.isServer()){
                //Check if player was wearing our armor before.
                ItemScubaSuit.checkArmour(event.player);

        		if(event.player.getCurrentArmor(3) == null) return;
        		if(event.player.getCurrentArmor(3).getItem() instanceof ItemMiningHelmet){
        			((ItemMiningHelmet)event.player.getCurrentArmor(3).getItem()).onArmorTick(event.player.worldObj, event.player, event.player.getCurrentArmor(3));
                }
        	}
        }
    }
}
