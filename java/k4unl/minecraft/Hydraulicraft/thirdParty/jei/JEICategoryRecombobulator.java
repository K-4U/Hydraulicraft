package k4unl.minecraft.Hydraulicraft.thirdParty.jei;

import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import mezz.jei.api.IGuiHelper;

import javax.annotation.Nonnull;
import java.awt.*;

public class JEICategoryRecombobulator extends JEICategoryAbstract {

    private static final Point pointOutput = new Point(123 - 7, 36 - 15);

    private static final Rectangle fluidIn  = new Rectangle(53 - 6, 2, 16, 54);

    public JEICategoryRecombobulator(IGuiHelper helper) {

        super(helper);
    }

    @Override
    public String getBackgroundTextureName() {

        return "recombobulator";
    }

    @Override
    public Rectangle getRectangleForFluidOutput(int i) {

        return null;
    }

    @Override
    public Rectangle getRectangleForFluidInput(int i) {

        return fluidIn;
    }

    @Override
    public Point getPointForInput(int i) {

        return null;
    }

    @Override
    public Point getPointForOutput(int i) {

        return pointOutput;
    }

    @Nonnull
    @Override
    public String getUid() {

        return JEIPlugin.recombobulatorRecipe;
    }

    @Nonnull
    @Override
    public String getTitle() {

        return Localization.getLocalizedName(Names.blockFluidRecombobulator.unlocalized);
    }
}
