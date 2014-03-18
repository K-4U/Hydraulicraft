package k4unl.minecraft.Hydraulicraft.lib.helperClasses;


public class Name {
	public String localized = "LOCALIZED_UNKNOWN";
	public String unlocalized = "UNLOCALIZED_UNKNOWN";
	
	
	public Name(String _localized, String _unlocalized){
		//localized = _localized;
		unlocalized = _unlocalized;
	}
	
	public String getLowerLocalized(){
		return localized.toLowerCase();
	}
	
	public String getLowerUnlocalized(){
		return unlocalized.toLowerCase();
	}
}
