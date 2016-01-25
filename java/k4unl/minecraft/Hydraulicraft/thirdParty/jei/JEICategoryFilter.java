package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.IGuiHelper;

import javax.annotation.Nonnull;
import java.awt.*;

public class JEICategoryFilter extends JEICategoryAbstract {
   private static final Point pointInput  = new Point(81 - 6, 34 - 14);
   private static final Point pointOutput = new Point(81 - 6, 53 - 14);

   private static final Rectangle fluidOut  = new Rectangle(38 - 6, 15 - 14, 55 - 38, 70 - 15);
   private static final Rectangle fluidIn = new Rectangle(126 - 6, 15 - 14, 143 - 126, 70 - 15);

   public JEICategoryFilter(IGuiHelper helper) {
      super(helper);
   }

   @Override
   public String getBackgroundTextureName() {
      return "filter";
   }

   @Override
   public Rectangle getRectangleForFluidOutput(int i) {
      return fluidIn;
   }

   @Override
   public Rectangle getRectangleForFluidInput(int i) {
      return fluidOut;
   }

   @Override
   public Point getPointForInput(int i) {
      return pointInput;
   }

   @Override
   public Point getPointForOutput(int i) {
      return pointOutput;
   }

   @Nonnull
   @Override
   public String getUid() {
      return JEIPlugin.filterRecipe;
   }

   @Nonnull
   @Override
   public String getTitle() {
      return "Filter"; // TODO localize
   }
}
