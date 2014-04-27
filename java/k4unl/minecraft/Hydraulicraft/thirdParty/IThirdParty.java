package k4unl.minecraft.Hydraulicraft.thirdParty;

public interface IThirdParty{

    public String getModId();

    public void preInit();

    public void init();

    public void postInit();

    /**
     * Gets called from the ClientProxy in the preInit.
     */
    public void clientSide();
}
