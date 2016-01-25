package k4unl.minecraft.Hydraulicraft.api;

import k4unl.minecraft.Hydraulicraft.api.recipes.IRecipeHandler;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import mcmultipart.multipart.Multipart;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Loader;

/**
 * @author Koen Beckers (K-4U) & MineMaarten
 */
public class HCApi {
    private static IHCApi instance;

    public static IHCApi getInstance() {

        return instance;
    }

    public interface IHCApi {
        IRecipeHandler getRecipeHandler();

        ITrolleyRegistrar getTrolleyRegistrar();

        IBaseClass getBaseClass(TileEntity target, int maxStorage);

        IBaseClass getBaseClass(Multipart target, int maxStorage);
    }

    /**
     * For internal use only, don't call it.
     *
     * @param inst
     */
    public static void init(IHCApi inst) {

        if (instance == null && Loader.instance().activeModContainer().getModId().equals(ModInfo.ID)) {
            instance = inst;
        } else {
            throw new IllegalStateException("This method should be called from Hydraulicraft only!");
        }
    }
}
