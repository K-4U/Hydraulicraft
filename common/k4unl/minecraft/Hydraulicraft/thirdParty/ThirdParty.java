package k4unl.minecraft.Hydraulicraft.thirdParty;

import cpw.mods.fml.common.event.FMLInterModComms;

public class ThirdParty {

	public static void init() {
		FMLInterModComms.sendMessage("Waila", "register", "k4unl.minecraft.Hydraulicraft.thirdParty.WailaProvider.callbackRegister");
	}

}
