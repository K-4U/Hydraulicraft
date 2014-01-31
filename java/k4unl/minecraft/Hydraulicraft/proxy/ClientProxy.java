package k4unl.minecraft.Hydraulicraft.proxy;

import k4unl.minecraft.Hydraulicraft.client.renderers.Renderers;


public class ClientProxy extends CommonProxy {
	
	public void init(){
		super.init();
		initRenderers();
	}
	
	public void initRenderers(){
		Renderers.init();
	}
}
