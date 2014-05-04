package k4unl.minecraft.Hydraulicraft.proxy;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.client.renderers.Renderers;
import k4unl.minecraft.Hydraulicraft.events.KeyHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdPartyManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import thirdParty.truetyper.FontLoader;


public class ClientProxy extends CommonProxy {
	
	public void init(){
		super.init();
		initRenderers();
		initFonts();
		
		KeyHandler.init();
	}
	
	public void initRenderers(){
		Renderers.init();
		ThirdPartyManager.instance().clientSide();
	}
	
	public void initFonts(){
		Hydraulicraft.smallGuiFont = FontLoader.createFont(new ResourceLocation(ModInfo.LID,"fonts/Ubuntu.ttf"), 15, true);
	}
	
	public ModelBiped getArmorModel(int id){
		return null;
	}

}
