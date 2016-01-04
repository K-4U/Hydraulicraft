package k4unl.minecraft.Hydraulicraft;

import k4unl.minecraft.Hydraulicraft.api.HCApi.IHCApi;
import k4unl.minecraft.Hydraulicraft.api.IBaseClass;
import k4unl.minecraft.Hydraulicraft.api.ITrolleyRegistrar;
import k4unl.minecraft.Hydraulicraft.api.recipes.IRecipeHandler;
import k4unl.minecraft.Hydraulicraft.lib.recipes.HydraulicRecipes;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Koen Beckers (K-4U)
 */
public class HydraulicraftAPI implements IHCApi {

    @Override
    public IRecipeHandler getRecipeHandler() {

        return HydraulicRecipes.INSTANCE;
    }

    @Override
    public ITrolleyRegistrar getTrolleyRegistrar() {

        return Hydraulicraft.trolleyRegistrar;
    }

    @Override
    public IBaseClass getBaseClass(TileEntity target, int maxStorage) {

        TileHydraulicBase newBase = new TileHydraulicBase(maxStorage);
        newBase.init(target);
        return newBase;
    }

    /*@Override
    public IBaseClass getBaseClass(TMultiPart target, int maxStorage) {

        TileHydraulicBase newBase = new TileHydraulicBase(maxStorage);
        newBase.init(target);

        return newBase;
    }*/
}
