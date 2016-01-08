package k4unl.minecraft.Hydraulicraft.proxy;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.client.ClientEventHandler;
import k4unl.minecraft.Hydraulicraft.client.renderers.IconSupplier;
import k4unl.minecraft.Hydraulicraft.client.renderers.Renderers;
import k4unl.minecraft.Hydraulicraft.events.KeyHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.ThirdPartyManager;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import thirdParty.truetyper.FontLoader;


public class ClientProxy extends CommonProxy {
	
	public void init(){
		super.init();
		initRenderers();
		initFonts();
		
		KeyHandler.init();
	}
	
	public void initRenderers(){
		MinecraftForge.EVENT_BUS.register(new IconSupplier());
		ClientEventHandler.init();
		Renderers.init();
		ThirdPartyManager.instance().clientSide();
	}
	
	public void initFonts(){
		Hydraulicraft.smallGuiFont = FontLoader.createFont(new ResourceLocation(ModInfo.LID,"fonts/Ubuntu.ttf"), 15, true);
		Hydraulicraft.mediumGuiFont = FontLoader.createFont(new ResourceLocation(ModInfo.LID,"fonts/Ubuntu.ttf"), 20, true);
	}

	public void registerBlockRenderer(Block toRegister){
		Renderers.registerBlockRenderer(toRegister);
	}
}
