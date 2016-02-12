package k4unl.minecraft.Hydraulicraft.lib;

import net.minecraft.util.StatCollector;

public class Localization {

    public static final String CHUNK_ENTRY             = "lang.chunk";
    public static final String DUST_ENTRY              = "lang.dust";
    public static final String PRESSURE_ENTRY          = "lang.pressure";
    public static final String GUI_GENERATING_ENTRY    = "lang.gui.generating";
    public static final String GUI_MAX_ENTRY           = "lang.gui.max";
    public static final String GUI_USING_ENTRY         = "lang.gui.using";
    public static final String GUI_OUTPUT_ENTRY        = "lang.gui.output";
    public static final String GUI_BURNLEFT_ENTRY      = "lang.gui.burnLeft";
    public static final String MAXPRESSURE_HIGH        = "lang.gui.maxHighPressure";
    public static final String MAXPRESSURE_MEDIUM      = "lang.gui.maxMediumPressure";
    public static final String MAXPRESSURE_LOW         = "lang.gui.maxLowPressure";
    public static final String GUI_UPTODATE            = "lang.gui.upToDate";
    public static final String GUI_UPDATEAVAILABLE     = "lang.gui.updateAvailable";
    public static final String GUI_RELEASEDAT          = "lang.gui.releasedAt";
    public static final String DESC_HYDRAULICFLUIDPUMP = "desc.hydraulicFluidPump";
    public static final String GUI_MULTIBLOCK          = "lang.tooltip.multiblock";
    public static final String GUI_MULTIBLOCK_TANK     = "lang.gui.multiblocktank";


    public static String getLocalizedName(String unlocalizedName) {

        if (!unlocalizedName.startsWith("tile.")) {
            unlocalizedName = "tile." + unlocalizedName;
        }
        return getString(unlocalizedName + ".name");
    }


    public static String getString(String unlocalized) {

        return StatCollector.translateToLocal(unlocalized);
    }

    public static String getString(String unlocalized, Object... args) {

        return StatCollector.translateToLocalFormatted(unlocalized, args);
    }
}


