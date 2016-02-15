package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.lib.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Koen Beckers (K-4U)
 */
public class HCGuiFactory implements IModGuiFactory {
    
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {

        return HCConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {

        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {

        return null;
    }

    public static class HCConfigGui extends GuiConfig {

        public HCConfigGui(GuiScreen parentScreen) {

            super(parentScreen, getConfigElements(), ModInfo.ID,
                    false, false, Localization.getString("config.main"));
        }

        private static List<IConfigElement> getConfigElements() {

            //List<IConfigElement> list = new ArrayList<IConfigElement>();
            //Add the two buttons that will go to each config category edit screen
            Configuration configuration = HCConfig.INSTANCE.getConfiguration();
            List<IConfigElement> list = new ArrayList<>();
            for (String category : configuration.getCategoryNames()) {
                if(category.equalsIgnoreCase("")){
                    continue;
                }
                list.add(new DummyConfigElement.DummyCategoryElement(category, "config." + category, CategoryGeneral.class));
            }
            return list;
        }

        public static class CategoryGeneral extends GuiConfigEntries.CategoryEntry {

            public CategoryGeneral(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {

                super(owningScreen, owningEntryList, prop);
            }

            @Override
            protected GuiScreen buildChildScreen() {
                //The following GuiConfig object specifies the configID of the object and thus will force-save
                // when closed.
                //Parent GuiConfig object's entryList will also be refreshed to reflect the changes.
                // --see GuiFactory of Forge for more info
                //Additionally, Forge best practices say to put the path to the config file for the category as
                // the title for the category config screen

                Configuration configuration = HCConfig.INSTANCE.getConfiguration();
                ConfigElement cat_general = new ConfigElement(configuration.getCategory(configElement.getName()));
                List<IConfigElement> propertiesOnThisScreen = cat_general.getChildElements();
                String windowTitle = configuration.toString();

                return new GuiConfig(this.owningScreen, propertiesOnThisScreen,
                        this.owningScreen.modID,
                        this.configElement.getName(),
                        this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                        this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                        windowTitle);
                //this is a complicated object that specifies the category's gui screen, to better understand
                // how it works, look into the definitions of the called functions and objects
            }
        }
    }


}
