package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
   public static final String assemblerRecipe   = "hydcraft.assembler";
   public static final String crusherRecipe     = "hydcraft.crusher";
   public static final String filterRecipe      = "hydcracft.filter";
   public static final String incineratorRecipe = "hydcraft.incinerator";
   public static final String washerRecipe      = "hydcraft.washer";

   private IJeiHelpers helpers;

   public static List makeList(Object[] array) {
      List out = new ArrayList();
      Collections.addAll(out, array);

      return out;
   }

   @Override
   public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
      this.helpers = jeiHelpers;
   }

   @Override
   public void onItemRegistryAvailable(IItemRegistry itemRegistry) {

   }

   @Override
   public void register(IModRegistry registry) {
      IGuiHelper helper = helpers.getGuiHelper();

      registry.addRecipeCategories(new JEICategoryAssembler(helper));
      registry.addRecipeHandlers(new JEIHandlerAssembler());

      registry.addRecipes(JEIHandlerAssembler.getRecipes());
   }

   @Override
   public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {

   }
}
