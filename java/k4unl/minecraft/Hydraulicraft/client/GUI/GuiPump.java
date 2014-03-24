package k4unl.minecraft.Hydraulicraft.client.GUI;

import java.text.DecimalFormat;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPump;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiPump extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/pump.png");
	TileHydraulicPump pump;
	
	
	public GuiPump(InventoryPlayer invPlayer, TileHydraulicPump _pump) {
		super(_pump, new ContainerPump(invPlayer, _pump), resLoc);
		pump = _pump;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRenderer.drawString(pump.getInvName(), 8, 6, 0xFFFFFF);
		drawHorizontalAlignedString(7, 3, xSize-14, pump.getInventoryName(), true);
		
		if(pump.getIsBurning()){
			int color = 0xFFB25900;
			
			float perc = pump.getBurningPercentage();
			int xOffset = 34;
			int yOffset = 35;
			int h = 10;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
			xOffset = 39;
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
			xOffset = 44;
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
			xOffset = 49;
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
		}
		int startY = 17;
		int step = (int)(Hydraulicraft.smallGuiFont.getLineHeight() / 3.2F);
		//int step = 6;
		
		String generating = (new DecimalFormat("#.##")).format(pump.getGenerating(ForgeDirection.UP));
		drawSmallerString(61, startY + (step * 0), EnumChatFormatting.GREEN + "Generating:", false);
		drawSmallerString(65, startY + (step * 1), EnumChatFormatting.GREEN + "" + generating + " mBar/t", false);
		drawSmallerString(61, startY + (step * 2), EnumChatFormatting.GREEN + "Max:", false);
		drawSmallerString(65, startY + (step * 3), EnumChatFormatting.GREEN + "" + pump.getMaxGenerating(ForgeDirection.UP) + " mBar/t", false);
		drawSmallerString(61, startY + (step * 4), EnumChatFormatting.GREEN + "Burn left:", false);
		drawSmallerString(65, startY + (step * 5), EnumChatFormatting.GREEN + "" + (int)(pump.getBurningPercentage()*100) + " %", false);
		
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
		
	}
}
