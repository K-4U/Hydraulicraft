package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import thirdParty.truetyper.FontHelper;

public class MachineGUI extends GuiContainer {
	private ResourceLocation resLoc;
	IHydraulicMachine mEnt;
	
	public class ToolTip{
		int x;
		int y;
		int w;
		int h;
		String title;
		String unit;
		float value;
		float max;
		
		public ToolTip(int _x, int _y, int _w, int _h, String _title, String _unit, float _value, float _max){
			x = _x;
			y = _y;
			w = _w;
			h = _h;
			
			title = _title;
			unit = _unit;
			
			value = _value;
			max = _max;
		}
		
		public List<String> getText(){
			List<String> text = new ArrayList<String>();
			text.add(title);
			text.add((int)value + "/" + (int)max + " " + unit);
			return text;
		}
	}
	protected List<ToolTip> tooltipList = new ArrayList<ToolTip>();
	
	public MachineGUI(IHydraulicMachine Entity, Container mainContainer, ResourceLocation _resLoc) {
		super(mainContainer);
		mEnt = Entity;
		resLoc = _resLoc;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resLoc);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	
	@Override
    public List<String> handleTooltip(int mouseX, int mouseY, List<String> currenttip){
		for (ToolTip tip : tooltipList) {
			if(shouldRenderToolTip(mouseX, mouseY, tip)){
				currenttip.addAll(tip.getText());
			}
		}
        return currenttip;
    }
	
	private boolean shouldRenderToolTip(int mouseX, int mouseY, ToolTip theTip){
		return isPointInRegion(theTip.x, theTip.y, theTip.w, theTip.h, mouseX, mouseY);
	}
	
	public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow){
		int stringWidth = fontRenderer.getStringWidth(text);
		int newX = xOffset;
		if(stringWidth < w){
			newX = (w / 2) - (stringWidth / 2) + xOffset;
		}
		
		fontRenderer.drawString(text, newX, yOffset, Constants.COLOR_TEXT, useShadow);
	}
	
	public void drawString(int xOffset, int yOffset, String text, boolean useShadow){
		fontRenderer.drawString(text, xOffset, yOffset, Constants.COLOR_TEXT, useShadow);
	}
	
	public void drawSmallerString(int xOffset, int yOffset, String text, boolean useShadow){
		GL11.glPushMatrix();
		FontHelper.drawString(text, (float)xOffset, (float)yOffset, Hydraulicraft.smallGuiFont, 1f, 1f);
		GL11.glPopMatrix();
		
	}
	
	public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color){
		float perc = (float)value / (float)max;
		int height = (int)(h * perc);
		drawRect(xOffset, yOffset + (h-height), xOffset + w, yOffset + h, color);
	}
	
	public void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color, String toolTipTitle, String toolTipUnit){
		float perc = (float)value / (float)max;
		int height = (int)(h * perc);
		//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
		drawRect(xOffset, yOffset + (h-height), xOffset + w, yOffset + h, color);
		
        tooltipList.add(new ToolTip(xOffset, yOffset, w, h, toolTipTitle, toolTipUnit, value, max));
	}
	
	public void drawVerticalProgressBarWithTexture(int xOffset, int yOffset, int h, int w, float value, float max, Icon icon, String toolTipTitle, String toolTipUnit){
		float perc = (float)value / (float)max;
		int height = (int)(h * perc);
		float uMin = icon.getMinU();
		float uMax = icon.getMaxU();
		float vMin = icon.getMinV();
		float vMax = icon.getMaxV();
		float iconHeight = icon.getIconHeight();
		float icons = height / iconHeight;
		float vMaxLast = ((vMax - vMin) * (icons % 1.0F)) + vMin;
		
		//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		int o = 0;
		for(o = 0; o < Math.floor(icons); o++){
	        tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * o), this.zLevel, uMin, vMin); //BL
	        tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * o), this.zLevel, uMax, vMin); //BR
	        tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * (o + 1)), this.zLevel, uMax, vMax);
	        tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * (o + 1)), this.zLevel, uMin, vMax);
		}
		o = (int) Math.floor(icons);
		
        tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * o), this.zLevel, uMin, vMin); //BL
        tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * o), this.zLevel, uMax, vMin); //BR
        tessellator.addVertexWithUV(xOffset + w, yOffset + h - (iconHeight * (o+(icons % 1.0F))), this.zLevel, uMax, vMaxLast); //TR
        tessellator.addVertexWithUV(xOffset + 0, yOffset + h - (iconHeight * (o+(icons % 1.0F))), this.zLevel, uMin, vMaxLast); //TL
        
        tessellator.draw();
	
		
		
        mc.renderEngine.bindTexture(resLoc);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        
        
		tooltipList.add(new ToolTip(xOffset, yOffset, w, h, toolTipTitle, toolTipUnit, value, max));
	}
	
	protected void drawFluidAndPressure(){
		int color = 0xFFFFFFFF;
		String fluidName = "";
		Icon icon = null;
		if(!mEnt.getHandler().isOilStored()){
			color = Constants.COLOR_WATER;
			icon = FluidRegistry.WATER.getIcon();
			fluidName = FluidRegistry.WATER.getLocalizedName();
		}else{
			color = Constants.COLOR_OIL;
			icon = Fluids.fluidOil.getIcon();
			fluidName = Fluids.fluidOil.getLocalizedName();
		}
		drawVerticalProgressBarWithTexture(8, 16, 54, 16, mEnt.getHandler().getStored(), mEnt.getMaxStorage(), icon, fluidName, "mB");
		
		color = Constants.COLOR_PRESSURE;
		drawVerticalProgressBar(152, 16, 54, 16, mEnt.getPressure(ForgeDirection.UNKNOWN), mEnt.getMaxPressure(mEnt.getHandler().isOilStored(), null), color, Localization.getString(Localization.PRESSURE_ENTRY), "mBar");
	}
	
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		tooltipList.clear();
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-94 + 2, Constants.COLOR_TEXT);
	}

}
