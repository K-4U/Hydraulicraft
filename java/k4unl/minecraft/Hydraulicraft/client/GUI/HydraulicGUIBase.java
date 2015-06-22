package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import thirdParty.truetyper.FontHelper;

import java.util.ArrayList;
import java.util.List;

public class HydraulicGUIBase extends GuiContainer {
    protected List<ToolTip> tooltipList = new ArrayList<ToolTip>();
    IHydraulicMachine mEnt;
    private ResourceLocation resLoc;

    public HydraulicGUIBase(IHydraulicMachine Entity, Container mainContainer,
                            ResourceLocation _resLoc) {
        super(mainContainer);
        mEnt = Entity;
        resLoc = _resLoc;
    }

    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h,
                                               int w, float value, float max, int color) {
        float perc = value / max;
        int height = (int) (h * perc);
        drawRect(xOffset, yOffset + (h - height), xOffset + w, yOffset + h,
                color);
    }

    public void addTooltip(ToolTip toolTip) {
        this.tooltipList.add(toolTip);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resLoc);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    public void checkTooltips(int mouseX, int mouseY) {
        for (ToolTip tip : tooltipList) {
            if (shouldRenderToolTip(mouseX, mouseY, tip)) {
                drawTooltip(mouseX, mouseY, tip);
            }
        }
    }


	/* NEI
    @Override
	public List<String> handleTooltip(int mouseX, int mouseY, List<String> currenttip){ 
		for (ToolTip tip : tooltipList) {
			if(shouldRenderToolTip(mouseX, mouseY, tip)){
				currenttip.addAll(tip.getText()); 
			} 
		} 
		return currenttip; 
	}
	*/

    private void drawTooltip(int mouseX, int mouseY, ToolTip toDraw) {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawHoveringText(toDraw.getText(), mouseX - x, mouseY - y, fontRendererObj);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    private boolean shouldRenderToolTip(int mouseX, int mouseY, ToolTip theTip) {
        return func_146978_c(theTip.x, theTip.y, theTip.w, theTip.h, mouseX,
                mouseY);
    }

    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w,
                                            String text, boolean useShadow) {
        int stringWidth = fontRendererObj.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = (w / 2) - (stringWidth / 2) + xOffset;
        }

        fontRendererObj.drawString(text, newX, yOffset, Constants.COLOR_TEXT,
                useShadow);
    }

    public void drawString(int xOffset, int yOffset, String text,
                           boolean useShadow) {
        fontRendererObj.drawString(text, xOffset, yOffset,
                Constants.COLOR_TEXT, useShadow);
    }

    public void drawSmallerString(int xOffset, int yOffset, String text,
                                  boolean useShadow) {
        GL11.glPushMatrix();
        FontHelper.drawString(text, xOffset, yOffset,
                Hydraulicraft.smallGuiFont, 1f, 1f);
        GL11.glPopMatrix();

    }

    public void drawMediumString(int xOffset, int yOffset, String text,
                                 boolean useShadow) {
        GL11.glPushMatrix();
        FontHelper.drawString(text, xOffset, yOffset,
                Hydraulicraft.mediumGuiFont, 1f, 1f);
        GL11.glPopMatrix();

    }

    public void drawHorizontalProgressBar(int xOffset, int yOffset, int h, int w,
                                          float value, float max, int color, String toolTipTitle,
                                          String toolTipUnit) {
        float perc = value / max;
        int width = (int) (w * perc);
        // drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
        drawRect(xOffset, yOffset, xOffset + width, yOffset + h,
                color);

        tooltipList.add(new ToolTipTank(xOffset, yOffset, w, h, toolTipTitle,
                toolTipUnit, value, max));
    }

    public void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w,
                                        float value, float max, int color, String toolTipTitle,
                                        String toolTipUnit) {
        float perc = value / max;
        int height = (int) (h * perc);
        // drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
        drawRect(xOffset, yOffset + (h - height), xOffset + w, yOffset + h,
                color);

        tooltipList.add(new ToolTipTank(xOffset, yOffset, w, h, toolTipTitle,
                toolTipUnit, value, max));
    }

    public void drawVerticalProgressBarWithTexture(int xOffset, int yOffset,
                                                   int h, int w, float value, float max, IIcon icon,
                                                   String toolTipTitle, String toolTipUnit) {
        float perc = value / max;
        int height = (int) (h * perc);
        float uMin = icon.getMinU();
        float uMax = icon.getMaxU();
        float vMin = icon.getMinV();
        float vMax = icon.getMaxV();
        float iconHeight = icon.getIconHeight();
        float icons = height / iconHeight;
        float vMaxLast = ((vMax - vMin) * (icons % 1.0F)) + vMin;

        // drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        int o = 0;
        for (o = 0; o < Math.floor(icons); o++) {
            tessellator.addVertexWithUV(xOffset + 0, yOffset + h
                    - (iconHeight * o), this.zLevel, uMin, vMin); // BL
            tessellator.addVertexWithUV(xOffset + w, yOffset + h
                    - (iconHeight * o), this.zLevel, uMax, vMin); // BR
            tessellator.addVertexWithUV(xOffset + w, yOffset + h
                    - (iconHeight * (o + 1)), this.zLevel, uMax, vMax);
            tessellator.addVertexWithUV(xOffset + 0, yOffset + h
                    - (iconHeight * (o + 1)), this.zLevel, uMin, vMax);
        }
        o = (int) Math.floor(icons);

        tessellator.addVertexWithUV(xOffset + 0,
                yOffset + h - (iconHeight * o), this.zLevel, uMin, vMin); // BL
        tessellator.addVertexWithUV(xOffset + w,
                yOffset + h - (iconHeight * o), this.zLevel, uMax, vMin); // BR
        tessellator.addVertexWithUV(xOffset + w, yOffset + h
                        - (iconHeight * (o + (icons % 1.0F))), this.zLevel, uMax,
                vMaxLast); // TR
        tessellator.addVertexWithUV(xOffset + 0, yOffset + h
                        - (iconHeight * (o + (icons % 1.0F))), this.zLevel, uMin,
                vMaxLast); // TL

        tessellator.draw();

        mc.renderEngine.bindTexture(resLoc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        tooltipList.add(new ToolTipTank(xOffset, yOffset, w, h, toolTipTitle,
                toolTipUnit, value, max));
    }

    protected void drawFluidAndPressure() {
        int color = 0xFFFFFFFF;
        String fluidName = "";
        IIcon icon = null;
        if (!mEnt.getHandler().isOilStored()) {
            color = Constants.COLOR_WATER;
            icon = FluidRegistry.WATER.getIcon();
            fluidName = FluidRegistry.WATER.getLocalizedName(new FluidStack(FluidRegistry.WATER, 1));
        } else {
            color = Constants.COLOR_OIL;
            icon = Fluids.fluidHydraulicOil.getIcon();
            fluidName = Fluids.fluidHydraulicOil.getLocalizedName(new FluidStack(Fluids.fluidHydraulicOil, 1));
        }
        drawVerticalProgressBarWithTexture(8, 16, 54, 16, mEnt.getHandler()
                .getStored(), mEnt.getHandler().getMaxStorage(), icon, fluidName, "mB");

        color = Constants.COLOR_PRESSURE;
        drawVerticalProgressBar(152, 16, 54, 16,
                (mEnt.getHandler().getPressure(ForgeDirection.UNKNOWN) / 1000),
                (mEnt.getHandler().getMaxPressure(mEnt.getHandler().isOilStored(), null) / 1000),
                color, Localization.getString(Localization.PRESSURE_ENTRY),
                "Bar");
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        tooltipList.clear();
        fontRendererObj.drawString(
                StatCollector.translateToLocal("container.inventory"), 8,
                ySize - 94 + 2, Constants.COLOR_TEXT);
    }

}
