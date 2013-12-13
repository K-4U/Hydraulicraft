package pet.minecraft.Hydraulicraft.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	
	public static int hydraulicPumpRenderType;
    public static int renderPass;
    
    
	public void initRenderers(){
		hydraulicPumpRenderType = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new FrozenDiamondRenderer());
	}
}
