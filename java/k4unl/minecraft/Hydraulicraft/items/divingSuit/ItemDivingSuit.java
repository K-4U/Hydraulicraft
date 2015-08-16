package k4unl.minecraft.Hydraulicraft.items.divingSuit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.client.models.ModelDivingSuit;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class ItemDivingSuit extends ItemArmor {

    public ItemDivingSuit(int type) {
        super(ArmorMaterial.CLOTH, 0, type);
        setMaxStackSize(1);

        if(type == 0) {
            setUnlocalizedName(Names.itemDivingHelmet.unlocalized);
        }else if(type == 1){
            setUnlocalizedName(Names.itemDivingChest.unlocalized);
        }else if(type == 2){
            setUnlocalizedName(Names.itemDivingLegs.unlocalized);
        }else if(type == 3){
            setUnlocalizedName(Names.itemDivingBoots.unlocalized);
        }

        setCreativeTab(CustomTabs.tabHydraulicraft);
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
                if(player.getCurrentArmor(3) != null){
                    if(player.getCurrentArmor(3).getItem() instanceof ItemDivingHelmet){
                        ItemDivingHelmet helmet = (ItemDivingHelmet) player.getCurrentArmor(3).getItem();
                        if(helmet.getFluid(player.getCurrentArmor(3)).amount > 0) {
                            doDamage(player);
                        }
                    }
                }

            }
        }
    }

    protected static void doDamage(EntityPlayer player){
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
            if (player.getCurrentArmor(0).getItem() instanceof ItemDivingSuit) {
                boots = true;
            }
        }
        if (player.getCurrentArmor(1) != null){
            if (player.getCurrentArmor(1).getItem() instanceof ItemDivingSuit) {
                legs = true;
            }
        }
        if (player.getCurrentArmor(2) != null){
            if (player.getCurrentArmor(2).getItem() instanceof ItemDivingSuit) {
                chest = true;
            }
        }
        if (player.getCurrentArmor(3) != null){
            if (player.getCurrentArmor(3).getItem() instanceof ItemDivingSuit) {
                helmet = true;
            }
        }
        return helmet && chest && legs && boots;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return ModelDivingSuit.getModel(entityLiving, itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return ModInfo.LID + ":textures/model/divingSuit.png";
    }
}
