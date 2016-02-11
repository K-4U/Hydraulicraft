package k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets;

import java.awt.*;

public abstract class WidgetBase {

    protected int x, y, width, height;
    private String[] tooltip;

    public WidgetBase(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {

    }

    // stolen from PneumaticCraft, needed and used for NEI
    /*public void handletooltip(GuiRecipe gui, List<String> currenttip, int recipe) {
        String[] tooltip = getTooltip();
        if (tooltip.length == 0)
            return;

        Point mouse = GuiDraw.getMousePosition();
        Point offset = gui.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x - (gui.width - 176) / 2 - offset.x, mouse.y - (gui.height - 166) / 2 - offset.y);

        if (getBounds().contains(relMouse)) {
            Collections.addAll(currenttip, tooltip);
        }
    }*/

    protected abstract String[] getTooltip();

    protected void setTooltip(String[] tooltip) {

        this.tooltip = tooltip;
    }

    // stolen from PneumaticCraft
    public Rectangle getBounds() {

        return new Rectangle(x, y, width, height);
    }

    public boolean clicked(int button) {

        return false;
    }
}
