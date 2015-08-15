package k4unl.minecraft.Hydraulicraft.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingHelmet;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingSuit;
import k4unl.minecraft.Hydraulicraft.lib.DamageSourceHydraulicraft;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class TickHandler {

    public static int tickCount = 0;

    public static void init() {
        FMLCommonHandler.instance().bus().register(new TickHandler());
    }

    @SubscribeEvent
    public void tickWorld(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            //World world = event.world;
        }
    }

    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                //Check if player was wearing our armor before.
                ItemDivingSuit.checkArmour(event.player);

                if (event.player.getCurrentArmor(3) != null) {
                    if (event.player.getCurrentArmor(3).getItem() instanceof ItemMiningHelmet) {
                        (event.player.getCurrentArmor(3).getItem()).onArmorTick(event.player.worldObj, event.player, event.player.getCurrentArmor(3));
                    }
                }

                if(event.player.isInWater() && tickCount >= 20){
                    tickCount=0;
                    //OMG THIS MAKES IT SO MUCH EASIER
                    //Check how many blocks of water are above the player
                    int x = (int)Math.round(event.player.posX);
                    int y = (int)Math.round(event.player.posY);
                    int z = (int)Math.round(event.player.posZ);

                    int pressure = 0;
                    while(event.player.worldObj.getBlock(x, y, z) == Blocks.water){
                        Block block = event.player.worldObj.getBlock(x, y, z);
                        y++;
                        pressure++;
                    }
                    if(pressure > 18) {
                        //Do damage, unless wearing a diving suit
                        if(!isWearingADivingSuit(event.player)){
                            event.player.attackEntityFrom(DamageSourceHydraulicraft.pressure, pressure / 2);
                        }
                        //Log.info(event.player.getDisplayName() + " is under " + pressure + " bar");
                    }
                }
                tickCount ++;
            }
        }
    }

    private boolean isWearingADivingSuit(EntityPlayer player){
        //Check for HC, IC2, SC and mariculture
        // mariculture:diving_helmet mariculture:diving_top mariculture:diving_pants mariculture:diving_boots
        // steamcraft:ItemDivingHelmet
        //
        boolean helmet = false;
        boolean chest  = false;
        boolean legs   = false;
        boolean boots  = false;
        boolean ic2helmet = false;
        boolean ic2chest  = false;
        boolean ic2legs   = false;
        boolean ic2boots  = false;
        for(int i = 0; i <= 3; i++){
            if(player.getCurrentArmor(i) != null) {
                if(i == 0){
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorRubBoots")){
                        boots = true;
                        ic2boots = true;
                    }
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("diving_boots")){
                        boots = true;
                    }
                }
                if(i == 1){
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorHazmatLeggings")){
                        legs = true;
                        ic2legs = true;
                    }
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("diving_pants")) {
                        legs = true;
                    }
                }
                if(i == 2){
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorHazmatChestplate")){
                        ic2chest = true;
                        chest = true;
                    }
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("diving_top")){
                        chest = true;
                    }
                }
                if(i == 3){
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorHazmatHelmet")){
                        ic2helmet = true;
                        helmet = true;
                    }
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("diving_helmet")){
                        helmet = true;
                    }
                    if(player.getCurrentArmor(i).getUnlocalizedName().equals("item.itemDivingHelmet")){
                        //Steamcraft
                        helmet = true;
                        chest = true;
                        legs = true;
                        boots = true;
                    }
                }
            }
        }
        if(ic2boots && ic2helmet && ic2chest && ic2legs){
            //Check inventory for filled air canisters.

            return true;
        }
        if(helmet && chest && legs && boots){
            return true;
        }

        if(ItemDivingSuit.isWearingFullSuit(player)){
            if(player.getCurrentArmor(3) != null) {
                if (player.getCurrentArmor(3).getItem() instanceof ItemDivingHelmet) {
                    ItemDivingHelmet dHelmet = (ItemDivingHelmet) player.getCurrentArmor(3).getItem();
                    if (dHelmet.getFluid(player.getCurrentArmor(3)).amount > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
