package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.IGuiHelper;

import javax.annotation.Nonnull;
import java.awt.*;

public class JEICategoryCrusher extends JEICategoryAbstract {
   public static final Point pointInput  = new Point(46 - 6, 34 - 14);
   public static final Point pointOutput = new Point(120 - 6, 34 - 14);

   public JEICategoryCrusher(IGuiHelper helper) {
      super(helper);
   }

   @Override
   public String getBackgroundTextureName() {
      return "crusher";
   }

   @Override
   public Rectangle getRectangleForFluidOutput(int i) {
      return null;
   }

   @Override
   public Rectangle getRectangleForFluidInput(int i) {
      return null;
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
      return JEIPlugin.crusherRecipe;
   }

   @Nonnull
   @Override
   public String getTitle() {
      return "Crusher"; // TODO localize
   }
}
