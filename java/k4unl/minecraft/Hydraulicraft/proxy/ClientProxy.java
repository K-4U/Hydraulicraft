package k4unl.minecraft.Hydraulicraft.proxy;

import net.minecraft.util.ResourceLocation;
import thirdParty.truetyper.FontLoader;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.HydraulicBaseClassSupplier;
import k4unl.minecraft.Hydraulicraft.client.renderers.Renderers;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdParty;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdPartyClient;


public class ClientProxy extends CommonProxy {
	
	public void init(){
		super.init();
		initRenderers();
		initFonts();
	}
	
	public void initRenderers(){
		Renderers.init();
		ThirdPartyClient.initRenderers();
	}
	
	public void initFonts(){
		Hydraulicraft.smallGuiFont = FontLoader.createFont(new ResourceLocation(ModInfo.LID,"fonts/Ubuntu.ttf"), 15, true);
	}
}
