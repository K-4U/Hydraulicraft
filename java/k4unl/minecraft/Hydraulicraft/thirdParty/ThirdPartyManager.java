package k4unl.minecraft.Hydraulicraft.thirdParty;

import cpw.mods.fml.common.Loader;
import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.thirdParty.bluepower.BluePower;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities.ExtraUtilities;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.FMP;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.IndustrialCraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdPartyManager{

    private static ThirdPartyManager INSTANCE = new ThirdPartyManager();
    private final List<IThirdParty> thirdPartyMods = new ArrayList<IThirdParty>();
    
    public static ThirdPartyManager instance(){
        return INSTANCE;
    }

    public void preInit(){
        //FMLInterModComms.sendMessage("Waila", "register", "k4unl.minecraft.Hydraulicraft.thirdParty.WailaProvider.callbackRegister");
        
        Map<String, Class<? extends IThirdParty>> thirdPartyClasses = new HashMap<String, Class<? extends IThirdParty>>();
        thirdPartyClasses.put("BuildCraft|Core",  Buildcraft.class);
        thirdPartyClasses.put("ExtraUtilities", ExtraUtilities.class);
        thirdPartyClasses.put("ForgeMicroblock", FMP.class);
        thirdPartyClasses.put("PneumaticCraft", Pneumaticraft.class);
        thirdPartyClasses.put("IC2", IndustrialCraft.class);
        thirdPartyClasses.put("ThermalExpansion", ThermalExpansion.class);
        thirdPartyClasses.put("bluepower", BluePower.class);

        for(Map.Entry<String, Class<? extends IThirdParty>> entry : thirdPartyClasses.entrySet()) {
            if(Loader.isModLoaded(entry.getKey()) || (entry.getKey().equals("ThermalExpansion") && HCConfig.getBool("enableRF"))) {
                try {
                    thirdPartyMods.add(entry.getValue().newInstance());
                } catch(Exception e) {
                    Log.error("Failed to instantiate third party handler!");
                    e.printStackTrace();
                }
            }
        }      
        
        for(IThirdParty thirdParty : thirdPartyMods) {
            try {
                thirdParty.preInit();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content from the third party class " + thirdParty.getClass()+ " in the PreInit phase!");
                e.printStackTrace();
            }
        }
    }

    public void init(){
        for(IThirdParty thirdParty : thirdPartyMods) {
            try {
                thirdParty.init();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content from the third party class " + thirdParty.getClass() + " in the Init phase!");
                e.printStackTrace();
            }
        }
    }

    public void postInit(){
        for(IThirdParty thirdParty : thirdPartyMods) {
            try {
                thirdParty.postInit();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content from the third party class " + thirdParty.getClass() + " in the PostInit phase!");
                e.printStackTrace();
            }
        }
    }

    public void clientSide(){
        for(IThirdParty thirdParty : thirdPartyMods) {
            try {
                thirdParty.clientSide();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content from the third party class " + thirdParty.getClass() + " clientside!");
                e.printStackTrace();
            }
        }
    }
}
