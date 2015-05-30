package k4unl.minecraft.Hydraulicraft.thirdParty.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.k4lib.lib.Functions;
import net.minecraft.item.ItemStack;


@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEIHydraulicraftConfig implements IConfigureNEI{


    @Optional.Method(modid="NotEnoughItems")
    @Override
    public void loadConfig() {
        API.registerRecipeHandler(new NEICrusherRecipeManager());
        API.registerUsageHandler(new NEICrusherRecipeManager());
        
        API.registerRecipeHandler(new NEIWasherRecipeManager());
        API.registerUsageHandler(new NEIWasherRecipeManager());

        API.registerRecipeHandler(new NEIFrictionIncineratorRecipeManager());
        API.registerUsageHandler(new NEIFrictionIncineratorRecipeManager());

        //Do a check here to see if we're in dev
        if(!Functions.isInDev()) {
            API.hideItem(new ItemStack(HCBlocks.blockChunkLoader));
            API.hideItem(new ItemStack(HCBlocks.movingPane));
            API.hideItem(new ItemStack(HCBlocks.blockLight));
            API.hideItem(new ItemStack(HCBlocks.portalTeleporter));
            API.hideItem(new ItemStack(HCBlocks.blockCharger));
        }
    }

    @Optional.Method(modid="NotEnoughItems")
    @Override
    public String getName() {
        return ModInfo.NAME;
    }

    @Optional.Method(modid="NotEnoughItems")
    @Override
    public String getVersion() {
        return ModInfo.VERSION;
    }
}
