package k4unl.minecraft.Hydraulicraft.thirdParty.igwmod;

import igwmod.api.WikiRegistry;

public class IGWHandler {

    public static void init(){
        WikiRegistry.registerWikiTab(new HydraulicraftWikiTab());

        WikiRegistry.registerRecipeIntegrator(new IntegratorAssembler());
    }
}
