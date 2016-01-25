package k4unl.minecraft.Hydraulicraft.thirdParty;

public interface IThirdParty{

    void preInit();

    void init();

    void postInit();

    /**
     * Gets called from the ClientProxy in the preInit.
     */
    void clientSide();
}
