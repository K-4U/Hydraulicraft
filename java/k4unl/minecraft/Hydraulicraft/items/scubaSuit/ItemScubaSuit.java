package k4unl.minecraft.Hydraulicraft.items.scubaSuit;

import k4unl.minecraft.Hydraulicraft.items.HCItems;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemScubaSuit extends ItemArmor {

    public ItemScubaSuit(int type) {
        super(ArmorMaterial.CLOTH, 0, type);
        setMaxStackSize(1);

        if(type == 0) {
            setUnlocalizedName(Names.itemScubaHelmet.unlocalized);
        }else if(type == 1){
            setUnlocalizedName(Names.itemScubaChest.unlocalized);
        }else if(type == 2){
            setUnlocalizedName(Names.itemScubaLegs.unlocalized);
        }else if(type == 3){
            setUnlocalizedName(Names.itemScubaBoots.unlocalized);
        }

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        //Log.info("Tick");
        NBTTagCompound entityData = player.getEntityData();
        if(entityData.getBoolean("isWearingFullScubaSuit")) {
            //Do 10 damage
            if(HCConfig.INSTANCE.getBool("doScubaDamage")) {
                if (itemStack.getItem() == HCItems.itemScubaHelmet) {
                    doDamage(player);
                }
            }
            //TODO: Check if there's still enough oxygen in the fluid.
            player.setAir(300);
            player.addPotionEffect(new PotionEffect(16,100));
        }
    }

    public static void checkArmour(EntityPlayer player){
        NBTTagCompound entityData = player.getEntityData();
        if(entityData.getBoolean("isWearingFullScubaSuit")) {
            if(!isWearingFullSuit(player)){
                entityData.setBoolean("isWearingFullScubaSuit", false);
                entityData.setInteger("damageDone", 0);
                //person WAS wearing one before.
                //Do damage!

            }
        }else{
            if(isWearingFullSuit(player)){
                entityData.setBoolean("isWearingFullScubaSuit", true);
                entityData.setInteger("damageDone", 0);
                //Person was NOT wearing one before
            }else{
                doDamage(player);
            }
        }
    }

    private static void doDamage(EntityPlayer player){
        NBTTagCompound entityData = player.getEntityData();
        if (entityData.getInteger("damageDone") < 100) {
            player.attackEntityFrom(DamageSource.drown, 1.0F);
            entityData.setInteger("damageDone", entityData.getInteger("damageDone") + 1);
        }
    }

    public static boolean isWearingFullSuit(EntityPlayer player){
        boolean helmet = false;
        boolean chest = false;
        boolean legs = false;
        boolean boots = false;
        if (player.getCurrentArmor(0) != null){
            if (player.getCurrentArmor(0).getItem() instanceof ItemScubaSuit) {
                helmet = true;
            }
        }
        if (player.getCurrentArmor(1) != null){
            if (player.getCurrentArmor(1).getItem() instanceof ItemScubaSuit) {
                chest = true;
            }
        }
        if (player.getCurrentArmor(2) != null){
            if (player.getCurrentArmor(2).getItem() instanceof ItemScubaSuit) {
                legs = true;
            }
        }
        if (player.getCurrentArmor(3) != null){
            if (player.getCurrentArmor(3).getItem() instanceof ItemScubaSuit) {
                boots = true;
            }
        }
        return helmet && chest && legs && boots;
    }
}
