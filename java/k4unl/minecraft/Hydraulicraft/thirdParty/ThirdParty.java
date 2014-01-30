package k4unl.minecraft.Hydraulicraft.thirdParty;

import k4unl.minecraft.Hydraulicraft.lib.Log;
import k4unl.minecraft.Hydraulicraft.thirdParty.buildcraft.Buildcraft;
import k4unl.minecraft.Hydraulicraft.thirdParty.extraUtilities.ExtraUtilities;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.Pneumaticraft;
import net.minecraft.block.Block;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ThirdParty{

    public static void init(){
        FMLInterModComms.sendMessage("Waila", "register", "k4unl.minecraft.Hydraulicraft.thirdParty.WailaProvider.callbackRegister");

        if(Loader.isModLoaded("PneumaticCraft")) {
            Log.info("Pneumaticraft found! Initializing Pneumaticraft support!");
            Pneumaticraft.init();
        }
        if(Loader.isModLoaded("ExtraUtilities")){
    		Log.info("ExtraUtilities found! Initializing ExtraUtilities support!");
    		
        }
        
        if(Loader.isModLoaded("BuildCraft|Core")){
        	Log.info("Buildcraft found! Initializing Buildcraft support!");
        	Buildcraft.init();
        }
    }
    
    public static void postInit(){
    	if(Loader.isModLoaded("ExtraUtilities")){
    		Log.info("ExtraUtilities found! Initializing ExtraUtilities support!");
    		ExtraUtilities.init();
        }
    }

}
