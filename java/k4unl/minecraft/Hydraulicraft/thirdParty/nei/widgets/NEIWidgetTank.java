package k4unl.minecraft.Hydraulicraft.thirdParty.nei.widgets;

import k4unl.minecraft.Hydraulicraft.client.GUI.HydraulicGUIBase;
import k4unl.minecraft.Hydraulicraft.client.GUI.ToolTip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NEIWidgetTank extends WidgetBase {
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

    public void updateAmount(int newAmount) {
        if (tank.getFluid() == null)
            return;

        tank.getFluid().amount = newAmount;
    }

    public void render(FluidTankInfo info) {
        if (info != null && info.fluid != null)
            updateAmount(info.fluid.amount);

        render();
    }

    @Override
    public void render() {
        int scaledHeight = (int) (this.height * ((float) tank.getFluidAmount() / tank.getCapacity()));
        if (tank.getFluid() == null)
            return;

        IIcon icon = tank.getFluid().getFluid().getIcon();
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
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        int o = 0;
        float zLevel = 0f;

        // render (height / iconHeight) times the base texture
        for (o = 0; o < Math.floor(icons); o++) {
            tessellator.addVertexWithUV(x + 0, y - (iconHeight * o), zLevel, uMin, vMin); // BL
            tessellator.addVertexWithUV(x + width, y - (iconHeight * o), zLevel, uMax, vMin); // BR
            tessellator.addVertexWithUV(x + width, y - (iconHeight * (o + 1)), zLevel, uMax, vMax);
            tessellator.addVertexWithUV(x + 0, y - (iconHeight * (o + 1)), zLevel, uMin, vMax);
        }
        o = (int) Math.floor(icons);

        // render remaining part of the size with the remainings of the texture
        tessellator.addVertexWithUV(x + 0, y - (iconHeight * o), zLevel, uMin, vMin); // BL
        tessellator.addVertexWithUV(x + width, y - (iconHeight * o), zLevel, uMax, vMin); // BR
        tessellator.addVertexWithUV(x + width, y - (iconHeight * (o + (icons % 1.0F))), zLevel, uMax, vMaxLast); // TR
        tessellator.addVertexWithUV(x + 0, y - (iconHeight * (o + (icons % 1.0F))), zLevel, uMin, vMaxLast); // TL

        tessellator.draw();

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
}
