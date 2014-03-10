package k4unl.minecraft.Hydraulicraft.lib;

import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

public class Localization {
	public static final String CHUNK_ENTRY = "lang.chunk";
	public static final String DUST_ENTRY = "lang.dust";
	public static final String PRESSURE_ENTRY = "lang.pressure";
	
	
	public static String getLocalizedName(String unlocalizedName){
		if(!unlocalizedName.startsWith("tile.")){
			unlocalizedName = "tile." + unlocalizedName;
		}
		return getString(unlocalizedName + ".name");
	}
	
	
	public static String getString(String unlocalized){
		return StatCollector.translateToLocal(unlocalized);
	}
	
	public static String getString(String unlocalized, Object ... args){
		return StatCollector.translateToLocalFormatted(unlocalized, args);
	}
}

