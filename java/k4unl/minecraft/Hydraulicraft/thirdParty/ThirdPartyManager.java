package k4unl.minecraft.Hydraulicraft.thirdParty;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.lib.config.Config;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities.ExtraUtilities;
import k4unl.minecraft.Hydraulicraft.thirdParty.fmp.FMP;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.IndustrialCraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThirdPartyManager{

    private static ThirdPartyManager INSTANCE = new ThirdPartyManager();
    private final List<IThirdParty> allThirdParty = new ArrayList<IThirdParty>();
    private final List<IThirdParty> availableThirdParty = new ArrayList<IThirdParty>();

    public static ThirdPartyManager instance(){
        return INSTANCE;
    }

    public void preInit(){
        FMLInterModComms.sendMessage("Waila", "register", "k4unl.minecraft.Hydraulicraft.thirdParty.WailaProvider.callbackRegister");
        
        allThirdParty.add(new Buildcraft());
        allThirdParty.add(new ExtraUtilities());
        allThirdParty.add(new FMP());
        allThirdParty.add(new Pneumaticraft());
        allThirdParty.add(new IndustrialCraft());
        
        IThirdParty TE = new ThermalExpansion();
        allThirdParty.add(TE);

        for(IThirdParty thirdParty : allThirdParty) {
            if(Loader.isModLoaded(thirdParty.getModId())) {
                availableThirdParty.add(thirdParty);
                Log.info(thirdParty.getModId() + " found! Initializing " + thirdParty.getModId() + " support!");
            }
        }

        if(!availableThirdParty.contains(TE)){
        	if(Config.get("enableRF")){
        		availableThirdParty.add(TE);
        	}
        }
        
        
        for(IThirdParty thirdParty : availableThirdParty) {
            try {
                thirdParty.preInit();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content for the mod " + thirdParty.getModId() + " in the PreInit phase!");
                e.printStackTrace();
            }
        }
    }

    public void init(){
        for(IThirdParty thirdParty : availableThirdParty) {
            try {
                thirdParty.init();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content for the mod " + thirdParty.getModId() + " in the Init phase!");
                e.printStackTrace();
            }
        }
    }

    public void postInit(){
        for(IThirdParty thirdParty : availableThirdParty) {
            try {
                thirdParty.postInit();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content for the mod " + thirdParty.getModId() + " in the PostInit phase!");
                e.printStackTrace();
            }
        }
    }

    public void clientSide(){
        for(IThirdParty thirdParty : availableThirdParty) {
            try {
                thirdParty.clientSide();
            } catch(Exception e) {
                Log.error("Hydraulicraft wasn't able to load third party content for the mod " + thirdParty.getModId() + " clientside!");
                e.printStackTrace();
            }
        }
    }
}
