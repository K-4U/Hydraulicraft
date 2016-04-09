package k4unl.minecraft.Hydraulicraft;

import com.google.common.base.Predicate;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.OcclusionHelper;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class LocalOcclusionHelper {
   private static Predicate<IMultipart> getPredicate(final IMultipart ignored) {
      return new Predicate<IMultipart>() {
         @Override
         public boolean apply(@Nullable IMultipart input) {
            return input == ignored;
         }
      };
   }

   public static boolean occlusionTest(Iterable<? extends IMultipart> parts, IMultipart ignored, AxisAlignedBB... boxes) {
      return OcclusionHelper.occlusionTest(parts,
              getPredicate(ignored),
              boxes);
   }
}
