package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

    public static final String assemblerRecipe      = "hydcraft.assembler";
    public static final String crusherRecipe        = "hydcraft.crusher";
    public static final String filterRecipe         = "hydcraft.filter";
    public static final String incineratorRecipe    = "hydcraft.incinerator";
    public static final String washerRecipe         = "hydcraft.washer";
    public static final String recombobulatorRecipe = "hydcraft.recombobulator";

    private IJeiHelpers helpers;

    public static List makeList(Object[] array) {

        List out = new ArrayList();
        Collections.addAll(out, array);

        return out;
    }

    @Override
    public void register(IModRegistry registry) {

        helpers = registry.getJeiHelpers();
        IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(
                new JEICategoryAssembler(helper),
                new JEICategoryFilter(helper),
                new JEICategoryCrusher(helper),
                new JEICategoryWasher(helper),
                new JEICategoryRecombobulator(helper)
        );

        JEIHandlerAbstract.JEIHandlerAssembler assembler = new JEIHandlerAbstract.JEIHandlerAssembler();
        JEIHandlerAbstract.JEIHandlerFilter filter = new JEIHandlerAbstract.JEIHandlerFilter();
        JEIHandlerAbstract.JEIHandlerCrusher crusher = new JEIHandlerAbstract.JEIHandlerCrusher();
        JEIHandlerAbstract.JEIHandlerWasher washer = new JEIHandlerAbstract.JEIHandlerWasher();
        JEIHandlerAbstract.JEIHandlerRecombobulator recombobulator = new JEIHandlerAbstract.JEIHandlerRecombobulator();

        registry.addRecipeHandlers(
                assembler,
                filter,
                crusher,
                washer,
                recombobulator
        );

        registry.addRecipes(assembler.getRecipes());
        registry.addRecipes(filter.getRecipes());
        registry.addRecipes(crusher.getRecipes());
        registry.addRecipes(washer.getRecipes());
        registry.addRecipes(recombobulator.getRecipes());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
