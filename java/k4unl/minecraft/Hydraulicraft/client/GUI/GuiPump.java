package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.generator.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerPump;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

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
		fontRenderer.drawString(pump.getInvName(), 8, 6, 0xFFFFFF);
		
		if(pump.getIsBurning()){
			int color = 0xFFB25900;
			
			float perc = pump.getBurningPercentage();
			int xOffset = 66;
			int yOffset = 35;
			int h = 10;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(66, yOffset + (h-height), xOffset + 3, yOffset + h, color);
			xOffset = 71;
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
			xOffset = 76;
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
			xOffset = 81;
			drawRect(xOffset, yOffset + (h-height), xOffset + 3, yOffset + h, color);
		}
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
		
	}
}
