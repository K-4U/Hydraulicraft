package k4unl.minecraft.Hydraulicraft.thirdParty.igwmod;

import k4unl.minecraft.Hydraulicraft.thirdParty.IThirdParty;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * Created by K-4U on 16-7-2015.
 */
public class IGWMod implements IThirdParty {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public void clientSide() {

        FMLInterModComms.sendMessage("IGWMod", IGWHandler.class.getName(), "init");
    }
}
