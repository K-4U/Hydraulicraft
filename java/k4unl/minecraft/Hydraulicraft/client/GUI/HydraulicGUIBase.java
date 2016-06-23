package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.k4lib.lib.Functions;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
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

    public HydraulicGUIBase(Container mainContainer,
                            ResourceLocation _resLoc) {

        super(mainContainer);
        resLoc = _resLoc;
    }

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

        return isPointInRegion(theTip.x, theTip.y, theTip.w, theTip.h, mouseX,
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

    public void drawRightAlignedString(int xOffset, int yOffset, int w,
                                       String text, boolean useShadow) {

        int stringWidth = fontRendererObj.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = (xOffset + w) - stringWidth;
        }

        fontRendererObj.drawString(text, newX, yOffset, Constants.COLOR_TEXT, useShadow);
    }

    public void drawString(int xOffset, int yOffset, String text,
                           boolean useShadow) {

        fontRendererObj.drawString(text, xOffset, yOffset,
                Constants.COLOR_TEXT, useShadow);
    }

    public void drawSmallerString(int xOffset, int yOffset, String text,
                                  boolean useShadow) {

        GL11.glPushMatrix();
        FontHelper.drawString(text, xOffset, yOffset, Hydraulicraft.smallGuiFont, 1f, 1f);
        GL11.glPopMatrix();

    }

    public void drawMediumString(int xOffset, int yOffset, String text,
                                 boolean useShadow) {

        GL11.glPushMatrix();
        FontHelper.drawString(text, xOffset, yOffset, Hydraulicraft.mediumGuiFont, 1f, 1f);
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
                                                   int h, int w, float value, float max, TextureAtlasSprite icon,
                                                   String toolTipTitle, String toolTipUnit) {

        float perc = value / max;
        int height = (int) (h * perc);
        if (icon == null) return;
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
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        VertexBuffer worldRenderer = Tessellator.getInstance().getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        int o;
        for (o = 0; o < Math.floor(icons); o++) {
            worldRenderer.pos(xOffset + 0, yOffset + h - (iconHeight * o), this.zLevel).tex(uMin, vMin).endVertex(); // BL
            worldRenderer.pos(xOffset + w, yOffset + h - (iconHeight * o), this.zLevel).tex(uMax, vMin).endVertex(); // BR
            worldRenderer.pos(xOffset + w, yOffset + h - (iconHeight * (o + 1)), this.zLevel).tex(uMax, vMax).endVertex();
            worldRenderer.pos(xOffset + 0, yOffset + h - (iconHeight * (o + 1)), this.zLevel).tex(uMin, vMax).endVertex();
        }
        o = (int) Math.floor(icons);

        worldRenderer.pos(xOffset + 0, yOffset + h - (iconHeight * o), this.zLevel).tex(uMin, vMin).endVertex(); // BL
        worldRenderer.pos(xOffset + w, yOffset + h - (iconHeight * o), this.zLevel).tex(uMax, vMin).endVertex(); // BR
        worldRenderer.pos(xOffset + w, yOffset + h - (iconHeight * (o + (icons % 1.0F))), this.zLevel).tex(uMax, vMaxLast).endVertex(); // TR
        worldRenderer.pos(xOffset + 0, yOffset + h - (iconHeight * (o + (icons % 1.0F))), this.zLevel).tex(uMin, vMaxLast).endVertex(); // TL

        Tessellator.getInstance().draw();

        mc.renderEngine.bindTexture(resLoc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        tooltipList.add(new ToolTipTank(xOffset, yOffset, w, h, toolTipTitle,
                toolTipUnit, value, max));
    }

    protected void drawFluidAndPressure() {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int color;
        String fluidName;
        TextureAtlasSprite icon;
        if (!mEnt.getHandler().isOilStored()) {
            icon = Functions.getFluidIcon(FluidRegistry.WATER);
            fluidName = FluidRegistry.WATER.getLocalizedName(new FluidStack(FluidRegistry.WATER, 1));
        } else {
            icon = Functions.getFluidIcon(Fluids.fluidHydraulicOil);
            fluidName = Fluids.fluidHydraulicOil.getLocalizedName(new FluidStack(Fluids.fluidHydraulicOil, 1));
        }
        drawVerticalProgressBarWithTexture(8, 16, 54, 16, mEnt.getHandler()
                .getStored(), mEnt.getHandler().getMaxStorage(), icon, fluidName, "mB");

        color = Constants.COLOR_PRESSURE;
        drawVerticalProgressBar(152, 16, 54, 16,
                (mEnt.getHandler().getPressure(EnumFacing.UP) / 1000),
                (mEnt.getHandler().getMaxPressure(mEnt.getHandler().isOilStored(), null) / 1000),
                color, Localization.getString(Localization.PRESSURE_ENTRY),
                "Bar");
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        tooltipList.clear();
        fontRendererObj.drawString(
                I18n.translateToLocal("container.inventory"), 8,
                ySize - 94 + 2, Constants.COLOR_TEXT);
    }

}
