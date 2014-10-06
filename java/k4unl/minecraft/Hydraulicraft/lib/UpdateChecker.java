package k4unl.minecraft.Hydraulicraft.lib;

import com.google.gson.Gson;
import cpw.mods.fml.common.event.FMLInterModComms;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class UpdateChecker {
	public static class UpdateInfo {
	    public String latestVersion;
	    public String buildNumber;
	    public String dateOfRelease;
	    public List<String> changelog;
	}
	
	public static boolean isUpdateAvailable;
	public static UpdateInfo infoAboutUpdate;
	
	
	public static boolean checkUpdateAvailable(){
		if(HCConfig.getBool("checkForUpdates")){
			String json = "";
			try {
				json = readUrl("http://hydraulicraft.eu/update_1.7.2.json");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(json != ""){
				try{
					Gson gson = new Gson();        
					UpdateInfo info = gson.fromJson(json, UpdateInfo.class);
					
					if(!info.latestVersion.equals(ModInfo.VERSION) || !info.buildNumber.equals(ModInfo.buildNumber)){
						Log.info("New version available!");
						Log.info("Latest version released at: " + info.dateOfRelease);
						isUpdateAvailable = true;
						infoAboutUpdate = info;
						sendUpdateInfo(info);
						return true;
					}else{
						return false;
					}
				}catch (Exception e){
					e.printStackTrace();
					return false;
				}
			}else{
				Log.warning("Got empty message from update. Either not connected to internet or something fishy is going on!");
				return false;
			}
		}else{
			return false;
		}
	}
	
	private static String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }

	}

	private static void sendUpdateInfo(UpdateInfo info)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("modDisplayName", ModInfo.NAME);
		compound.setString("oldVersion", ModInfo.VERSION + "-" + ModInfo.buildNumber);
		compound.setString("newVersion", info.latestVersion + "-" + info.buildNumber);
		compound.setString("updateUrl", "http://hydraulicraft.eu/downloads/");
		compound.setBoolean("isDirectLink", false);
		String changeLog = "";
		for (String string : info.changelog)
		{
			changeLog = changeLog + string + "\n";
		}
		compound.setString("changeLog", changeLog);
		FMLInterModComms.sendMessage("VersionChecker", "addUpdate", compound);
	}
}
