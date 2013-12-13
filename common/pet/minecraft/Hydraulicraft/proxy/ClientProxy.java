package pet.minecraft.Hydraulicraft.proxy;

import pet.minecraft.Hydraulicraft.client.renderers.Renderers;


public class ClientProxy extends CommonProxy {
	
	public void initRenderers(){
		Renderers.init();
	}
}
