package k4unl.minecraft.Hydraulicraft.thirdParty;

import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThirdParty{

    public static void init(){
        FMLInterModComms.sendMessage("Waila", "register", "k4unl.minecraft.Hydraulicraft.thirdParty.WailaProvider.callbackRegister");

        if(Loader.isModLoaded("PneumaticCraft")) {
            Log.info("Pneumaticraft found! Initializing Pneumaticraft support!");
            Pneumaticraft.init();
        }
    }

}
