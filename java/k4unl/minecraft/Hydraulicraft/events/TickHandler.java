package k4unl.minecraft.Hydraulicraft.events;

import k4unl.minecraft.Hydraulicraft.api.IPressureDivingSuit;
import k4unl.minecraft.Hydraulicraft.items.ItemMiningHelmet;
import k4unl.minecraft.Hydraulicraft.items.ItemPressureGauge;
import k4unl.minecraft.Hydraulicraft.items.divingSuit.ItemDivingSuit;
import k4unl.minecraft.Hydraulicraft.lib.DamageSourceHydraulicraft;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketSetPressure;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

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

                if (event.player.isInWater() && tickCount >= 20 && HCConfig.INSTANCE.getBool("waterPressureKills")) {
                    tickCount = 0;
                    //OMG THIS MAKES IT SO MUCH EASIER
                    //Check how many blocks of water are above the player
                    int x = (int) Math.floor(event.player.posX);
                    int y = (int) Math.floor(event.player.posY);
                    int z = (int) Math.floor(event.player.posZ);

                    Block block = event.player.worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();
                    int pressure = 0;
                    while (block == Blocks.water) {
                        y++;
                        pressure++;
                    }
                    if (event.player.getEntityData().getInteger("pressure") != pressure) {
                        boolean pressureGauge = false;
                        for (ItemStack itemStack : event.player.inventory.mainInventory) {
                            if (itemStack != null && itemStack.getItem() instanceof ItemPressureGauge) {
                                pressureGauge = true;
                            }
                        }
                        NetworkHandler.sendTo(new PacketSetPressure(pressure, pressureGauge), (EntityPlayerMP) event.player);
                    }
                    event.player.getEntityData().setInteger("pressure", pressure);
                    if (pressure > HCConfig.INSTANCE.getInt("maxWaterPressureWithoutSuit")) {
                        //Do damage, unless wearing a diving suit
                        if (!isWearingADivingSuit(event.player, pressure)) {
                            event.player.attackEntityFrom(DamageSourceHydraulicraft.pressure, (float) (pressure / HCConfig.INSTANCE.getDouble("pressureDamageFactor")));
                        }
                        //Log.info(event.player.getDisplayName() + " is under " + pressure + " bar");
                    }
                }
                tickCount++;
            }
        }
    }

    private boolean isWearingADivingSuit(EntityPlayer player, int pressure) {
        List<Boolean> armour = new ArrayList<Boolean>();
        if (Loader.isModLoaded("IC2")) {
            boolean ic2helmet = false;
            boolean ic2chest = false;
            boolean ic2legs = false;
            boolean ic2boots = false;
            for (int i = 0; i <= 3; i++) {
                if (player.getCurrentArmor(i) != null) {
                    if (player.getCurrentArmor(i).getItem() instanceof IPressureDivingSuit) {
                        armour.add(((IPressureDivingSuit) player.getCurrentArmor(i).getItem()).isPressureSafe(player, player.getCurrentArmor(i), pressure));
                    }
                    if (i == 0) {
                        if (player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorRubBoots")) {
                            ic2boots = true;
                        }
                    }
                    if (i == 1) {
                        if (player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorHazmatLeggings")) {
                            ic2legs = true;
                        }

                    }
                    if (i == 2) {
                        if (player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorHazmatChestplate")) {
                            ic2chest = true;
                        }
                    }
                    if (i == 3) {
                        if (player.getCurrentArmor(i).getUnlocalizedName().equals("ic2.itemArmorHazmatHelmet")) {
                            ic2helmet = true;
                        }
                    }
                }
            }
            if (ic2boots && ic2helmet && ic2chest && ic2legs) {
                //Check inventory for filled air canisters.

                return true;
            }
        }

        for (boolean armourEntry : armour) {
            if (!armourEntry) {
                return false;
            }
        }
        return true;

        /*if(ItemDivingSuit.isWearingFullSuit(player)){
            if(player.getCurrentArmor(3) != null) {
                if (player.getCurrentArmor(3).getItem() instanceof ItemDivingHelmet) {
                    ItemDivingHelmet dHelmet = (ItemDivingHelmet) player.getCurrentArmor(3).getItem();
                    if (dHelmet.getFluid(player.getCurrentArmor(3)).amount > 0) {
                        return true;
                    }
                }
            }
        }*/
        //return false;
    }
}
