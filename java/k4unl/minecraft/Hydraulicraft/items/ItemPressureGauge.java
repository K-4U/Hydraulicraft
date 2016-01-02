package k4unl.minecraft.Hydraulicraft.items;

import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

/**
 * @author Koen Beckers (K-4U)
 */
public class ItemPressureGauge extends HydraulicItemBase {

    public ItemPressureGauge() {
        super(Names.itemPressureGauge);
        setDefaultInfo(Localization.getString("lang.tooltip.pressureGauge"));
    }
}
