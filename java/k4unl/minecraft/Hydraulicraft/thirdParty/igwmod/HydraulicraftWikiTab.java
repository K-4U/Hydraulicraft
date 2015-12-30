package k4unl.minecraft.Hydraulicraft.thirdParty.igwmod;

import cpw.mods.fml.common.Loader;
import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class HydraulicraftWikiTab extends BaseWikiTab {

    public HydraulicraftWikiTab(){

        pageEntries.add("baseConcepts");
        pageEntries.add("pressureNetwork");
        pageEntries.add("crafting");
        pageEntries.add("diving");

        addSectionHeader("worldgen");
        pageEntries.add("oil");
        pageEntries.add("minerals");
        pageEntries.add("ores");
        skipLine();

        addSectionHeader("multiblocks");
        pageEntries.add("farms");
        pageEntries.add("washer");
        pageEntries.add("tanks");
        pageEntries.add("portal");

        skipLine();

        addSectionHeader("thirdparty");
        if(Loader.isModLoaded("IC2"))
            pageEntries.add("thirdparty:ic2");
        if(Loader.isModLoaded("ExtraUtilities"))
            pageEntries.add("thirdparty:extrautilities");
        if(Loader.isModLoaded("bluepower"))
            pageEntries.add("thirdparty:bluepower");
        if(Loader.isModLoaded("PneumaticCraft"))
            pageEntries.add("thirdparty:pneumaticcraft");
        pageEntries.add("thirdparty:rf");

        Log.info("IGW support for Hydraulicraft Loaded");
    }

    @Override
    protected String getPageName(String pageEntry){
        if(pageEntry.startsWith("item") || pageEntry.startsWith("block")) return I18n.format(pageEntry.replace("/", ".").replace("block", "tile") + ".name");
        if(pageEntry.startsWith("#")) return I18n.format(ModInfo.LID + ".wiki.section." + pageEntry.replace("#",""));
        return I18n.format(ModInfo.LID + ".wiki.entry." + pageEntry);
    }

    @Override
    protected String getPageLocation(String pageEntry){
        if(pageEntry.startsWith("item") || pageEntry.startsWith("block")) return ModInfo.LID + ":" + pageEntry;
        return ModInfo.LID + ":menu/" + pageEntry;
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
