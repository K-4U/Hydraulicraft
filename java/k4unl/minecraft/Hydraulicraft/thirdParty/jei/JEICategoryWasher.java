package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import mezz.jei.api.IGuiHelper;

import javax.annotation.Nonnull;
import java.awt.*;

public class JEICategoryWasher extends JEICategoryAbstract {
   public static final Point pointInput  = new Point(55 - 6, 15 - 14);
   public static final Point pointOutput = new Point(105 - 6, 55 - 14);

   public JEICategoryWasher(IGuiHelper helper) {
      super(helper);
   }

   @Override
   public int getBackgroundHeight() {
      return 60;
   }

   @Override
   public String getBackgroundTextureName() {
      return "washer";
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
      return JEIPlugin.washerRecipe;
   }

   @Nonnull
   @Override
   public String getTitle() {
      return "Washer"; // TODO localize
   }
}
