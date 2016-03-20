package k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets;

import k4unl.minecraft.Hydraulicraft.client.GUI.HydraulicGUIBase;
import k4unl.minecraft.Hydraulicraft.client.GUI.ToolTip;
import k4unl.minecraft.k4lib.lib.Functions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NEIWidgetTank extends WidgetBase {

    //TODO: MOVE ME TO THE GUI PACKAGE
    FluidTank        tank;
    HydraulicGUIBase gui;

    public NEIWidgetTank(FluidTankInfo info, int x, int y, int width, int height) {

        this(new FluidTank(info.fluid, info.capacity), x, y, width, height);
    }

    public NEIWidgetTank(FluidTankInfo info, int x, int y, int width, int height, HydraulicGUIBase gui) {

        this(info, x, y, width, height);
        this.gui = gui;
    }

    public NEIWidgetTank(FluidTank tank, int x, int y, int width, int height, HydraulicGUIBase gui) {

        this(tank, x, y, width, height);
        this.gui = gui;
    }

    public NEIWidgetTank(FluidTank tank, int x, int y, int width, int height) {

        super(x, y, width, height);
        this.tank = tank;

        setTooltip(getTooltip());
    }

    public NEIWidgetTank(FluidStack fluidStack, int x, int y, int width, int height) {

        this(new FluidTank(fluidStack, fluidStack.amount), x, y, width, height);
    }

    public void updateAmount(FluidStack newAmount) {

        tank.setFluid(newAmount);
    }

    public void render(FluidTankInfo info) {

        if (info != null && info.fluid != null) {
            updateAmount(info.fluid);
        } else if (info.fluid == null) {
            tank.setFluid(null);
        }

        render();
    }

    @Override
    public void render() {

        int scaledHeight = (int) (this.height * ((float) tank.getFluidAmount() / tank.getCapacity()));
        if (tank.getFluid() == null)
            return;

        TextureAtlasSprite icon = Functions.getFluidIcon(tank.getFluid().getFluid());
        float uMin = icon.getMinU();
        float uMax = icon.getMaxU();
        float vMin = icon.getMinV();
        float vMax = icon.getMaxV();
        float iconHeight = icon.getIconHeight();
        float icons = scaledHeight / iconHeight;
        float vMaxLast = ((vMax - vMin) * (icons % 1.0F)) + vMin;

        // drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        VertexBuffer worldRenderer = Tessellator.getInstance().getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        int o;
        float zLevel = 0f;

        // render (height / iconHeight) times the base texture
        for (o = 0; o < Math.floor(icons); o++) {
            worldRenderer.pos(x + 0, y - (iconHeight * o), zLevel).tex(uMin, vMin).endVertex(); // BL
            worldRenderer.pos(x + width, y - (iconHeight * o), zLevel).tex(uMax, vMin).endVertex(); // BR
            worldRenderer.pos(x + width, y - (iconHeight * (o + 1)), zLevel).tex(uMax, vMax).endVertex();
            worldRenderer.pos(x + 0, y - (iconHeight * (o + 1)), zLevel).tex(uMin, vMax).endVertex();
        }
        o = (int) Math.floor(icons);

        // render remaining part of the size with the remainings of the texture
        worldRenderer.pos(x + 0, y - (iconHeight * o), zLevel).tex(uMin, vMin).endVertex(); // BL
        worldRenderer.pos(x + width, y - (iconHeight * o), zLevel).tex(uMax, vMin).endVertex(); // BR
        worldRenderer.pos(x + width, y - (iconHeight * (o + (icons % 1.0F))), zLevel).tex(uMax, vMaxLast).endVertex(); // TR
        worldRenderer.pos(x + 0, y - (iconHeight * (o + (icons % 1.0F))), zLevel).tex(uMin, vMaxLast).endVertex(); // TL

        Tessellator.getInstance().draw();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        super.render();

        if (gui != null)
            gui.addTooltip(new ToolTip(x, y - height, width, height, getTooltip()));
    }

    @Override
    protected String[] getTooltip() {

        if (tank.getFluid() == null)
            return new String[]{"empty"};

        return new String[]{tank.getFluid().getFluid().getLocalizedName(tank.getFluid()), tank.getFluidAmount() + "mB" +
                (gui != null ? "/" + tank.getCapacity() + "mB" : "")};
    }

    @Override
    public Rectangle getBounds() {

        return new Rectangle(x, y - height, width, height);
    }

    /*@Override
    public boolean clicked(int button) {
        if (tank.getFluid() == null)
            return false;

        if (button == 0) { // left click
            return GuiCraftingRecipe.openRecipeGui("fluid", tank.getFluid());
        } else if (button == 1) { // right click
            return GuiUsageRecipe.openRecipeGui("fluid", tank.getFluid());
        }
        return false;
    }*/
}

