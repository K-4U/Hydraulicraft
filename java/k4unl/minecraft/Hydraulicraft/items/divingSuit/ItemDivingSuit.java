package k4unl.minecraft.Hydraulicraft.items.divingSuit;

import k4unl.minecraft.Hydraulicraft.api.IPressureDivingSuit;
import k4unl.minecraft.Hydraulicraft.client.models.ModelDivingSuit;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class ItemDivingSuit extends ItemArmor implements IPressureDivingSuit {

    public ItemDivingSuit(EntityEquipmentSlot type) {

        super(ArmorMaterial.LEATHER, 0, type);
        setMaxStackSize(1);

        if (type == EntityEquipmentSlot.HEAD) {
            setUnlocalizedName(Names.itemDivingHelmet.unlocalized);
        } else if (type == EntityEquipmentSlot.CHEST) {
            setUnlocalizedName(Names.itemDivingChest.unlocalized);
        } else if (type == EntityEquipmentSlot.LEGS) {
            setUnlocalizedName(Names.itemDivingLegs.unlocalized);
        } else if (type == EntityEquipmentSlot.FEET) {
            setUnlocalizedName(Names.itemDivingBoots.unlocalized);
        }

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }


    public static void checkArmour(EntityPlayer player) {

        NBTTagCompound entityData = player.getEntityData();
        if (entityData.getBoolean("isWearingFullScubaSuit")) {
            if (!isWearingFullSuit(player)) {
                entityData.setBoolean("isWearingFullScubaSuit", false);
                entityData.setInteger("damageDone", 0);
                //person WAS wearing one before.
                //Do damage!
            }
        } else {
            if (isWearingFullSuit(player)) {
                entityData.setBoolean("isWearingFullScubaSuit", true);
                entityData.setInteger("damageDone", 0);
                //Person was NOT wearing one before
            } else {
                if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null) {
                    if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemDivingHelmet) {
                        ItemDivingHelmet helmet = (ItemDivingHelmet) player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem();
                        if (helmet.getFluid(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD)).amount > 0) {
                            doDamage(player);
                        }
                    }
                }

            }
        }
    }

    protected static void doDamage(EntityPlayer player) {

        NBTTagCompound entityData = player.getEntityData();
        if (entityData.getInteger("damageDone") < 100) {
            player.attackEntityFrom(DamageSource.drown, 1.0F);
            entityData.setInteger("damageDone", entityData.getInteger("damageDone") + 1);
        }
    }

    public static boolean isWearingFullSuit(EntityPlayer player) {

        boolean helmet = false;
        boolean chest = false;
        boolean legs = false;
        boolean boots = false;
        if (player.getItemStackFromSlot(EntityEquipmentSlot.FEET) != null) {
            if (player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemDivingSuit) {
                boots = true;
            }
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS) != null) {
            if (player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemDivingSuit) {
                legs = true;
            }
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null) {
            if (player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemDivingSuit) {
                chest = true;
            }
        }
        if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null) {
            if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemDivingSuit) {
                helmet = true;
            }
        }
        return helmet && chest && legs && boots;
    }

    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return ModelDivingSuit.getModel(entityLiving, itemStack);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return ModInfo.LID + ":textures/model/divingSuit.png";
    }

    @Override
    public boolean isPressureSafe(EntityPlayer player, ItemStack stack, int pressure) {
        return isWearingFullSuit(player);
    }
}
