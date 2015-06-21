package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class HydraulicItemBase extends Item {
    private Name mName;
    private boolean hasEffect   = false;
    private String  defaultInfo = "";


    public HydraulicItemBase(Name itemName) {
        super();

        mName = itemName;

        setMaxStackSize(64);
        setUnlocalizedName(itemName.unlocalized);
        setTextureName(ModInfo.LID + ":" + itemName.unlocalized);

        setCreativeTab(CustomTabs.tabHydraulicraft);
    }

    /*!
     * @author Koen Beckers
     * @date 13-12-2013
     * Sets whether the "enchanted" effect is active on this item.
     */
    public void setEffect(boolean _hasEffect) {
        hasEffect = _hasEffect;
    }

    public void setEffect(ItemStack itemStack, boolean _hasEffect) {
        NBTTagCompound stackCompound = itemStack.getTagCompound();
        stackCompound.setBoolean("hasEffect", _hasEffect);
        itemStack.setTagCompound(stackCompound);
    }

    @Override
    public boolean hasEffect(ItemStack itemStack, int pass) {
        if (itemStack.getTagCompound() == null) {
            return hasEffect;
        }

        return hasEffect || itemStack.getTagCompound().getBoolean("hasEffect");
    }

    public void setDefaultInfo(String info) {
        defaultInfo = info;
    }

    public void setDefaultInfo(ItemStack itemStack, String info) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound stackCompound = itemStack.getTagCompound();
        stackCompound.setString("defaultInfo", info);
        itemStack.setTagCompound(stackCompound);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        if (defaultInfo != "") {
            list.add(defaultInfo);
        }
        if (itemStack.getTagCompound() != null) {
            if (itemStack.getTagCompound().getString("defaultInfo") != "") {
                list.add(itemStack.getTagCompound().getString("defaultInfo"));
            }
        }
    }
}
