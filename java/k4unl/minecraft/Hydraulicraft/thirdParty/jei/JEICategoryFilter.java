package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import mezz.jei.api.IGuiHelper;

import javax.annotation.Nonnull;
import java.awt.*;

public class JEICategoryFilter extends JEICategoryAbstract {

    private static final Point pointInput  = new Point(81 - 6, 34 - 14);
    private static final Point pointOutput = new Point(81 - 6, 53 - 14);

    private static final Rectangle fluidIn  = new Rectangle(38 - 6, 16 - 14, 55 - 39, 70 - 16);
    private static final Rectangle fluidOut = new Rectangle(126 - 6, 16 - 14, 143 - 127, 70 - 16);

    public JEICategoryFilter(IGuiHelper helper) {

        super(helper);
    }

    @Override
    public String getBackgroundTextureName() {

        return "filter";
    }

    @Override
    public Rectangle getRectangleForFluidOutput(int i) {

        return fluidOut;
    }

    @Override
    public Rectangle getRectangleForFluidInput(int i) {

        return fluidIn;
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

        return Localization.getLocalizedName(Names.blockHydraulicFilter.unlocalized);
    }
}
