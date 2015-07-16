package k4unl.minecraft.Hydraulicraft.thirdParty.igwmod;

import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class HydraulicraftWikiTab extends BaseWikiTab {

    static {
        Log.info("IF YOU SEE ME, SHIT GONE AWRY");
    }

    public HydraulicraftWikiTab(){

        pageEntries.add(ModInfo.ID + ":baseConcepts");

        Log.info("IGW support for Hydraulicraft Loaded");
    }

    @Override
    protected String getPageName(String pageEntry){
        if(pageEntry.startsWith("item") || pageEntry.startsWith("block")) {
            return I18n.format(pageEntry.replace("/", ".").replace("block", "tile") + ".name");
        } else {
            return I18n.format(ModInfo.ID + ".wiki.entry." + pageEntry);
        }
    }

    @Override
    protected String getPageLocation(String pageEntry){
        if(pageEntry.startsWith("item") || pageEntry.startsWith("block")) return ModInfo.ID + ":" + pageEntry;
        return ModInfo.ID + ":" + "menu/" + pageEntry;
    }
    @Override
    public String getName() {
        return ModInfo.NAME;
    }

    @Override
    public ItemStack renderTabIcon(GuiWiki guiWiki) {
        return CustomTabs.tabHydraulicraft.getIconItemStack();
    }
}
