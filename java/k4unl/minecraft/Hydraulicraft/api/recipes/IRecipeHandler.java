package k4unl.minecraft.Hydraulicraft.api.recipes;

/**
 * @author Koen Beckers (K-4U)
 */
public interface IRecipeHandler {
   void addCrushingRecipe(IFluidRecipe toAdd);

   void addAssemblerRecipe(IFluidRecipe toAdd);

   void addWasherRecipe(IFluidRecipe toAdd);

   void addFilterRecipe(IFluidRecipe toAdd);
}
