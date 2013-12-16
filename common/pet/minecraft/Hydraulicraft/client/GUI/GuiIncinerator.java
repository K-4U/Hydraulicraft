package pet.minecraft.Hydraulicraft.client.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicFrictionIncinerator;
import pet.minecraft.Hydraulicraft.client.containers.ContainerIncinerator;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class GuiIncinerator extends GuiContainer {

	private ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/incinerator.png");
	TileHydraulicFrictionIncinerator incinerator;
	
	
	public GuiIncinerator(InventoryPlayer invPlayer, TileHydraulicFrictionIncinerator _incinerator) {
		super(new ContainerIncinerator(invPlayer, _incinerator));
		incinerator = _incinerator;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2){
		//TODO: Change that color
		fontRenderer.drawString(Names.blockHydraulicCrusher.localized, 8, 6, 0xFFFFFF);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-96 + 2, 0xFFFFFF);
		
		if(incinerator.getStored() > 0){
			int color = 0xFFFFFFFF;
			if(!incinerator.isOilStored()){
				color = 0xFF006DD9;
			}
			
			int max = incinerator.getStorage();
			float perc = (float)incinerator.getStored() / (float)max;
			
			int xOffset = 8;
			int yOffset = 14;
			int h = 58;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		if(incinerator.getPressure() > 0){
			int color = 0xBFFFFFFF;
			
			float max = incinerator.getMaxPressure();
			float perc = incinerator.getPressure() / max;
			
			int xOffset = 152;
			int yOffset = 14;
			int h = 58;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		
		
		if(incinerator.isSmelting()){
			Icon smeltingIcon = incinerator.getSmeltingItem().getIconIndex();
			int done = incinerator.getSmeltingTicks();
			int startX = 8;
			int maxTicks = 200;
			int targetX = 118;
			int travelPath = targetX - startX;
			float percentage = (float)done / (float)maxTicks;
			int xPos = startX + (int) (travelPath * percentage);
			drawTexturedModelRectFromIcon(xPos, 14, smeltingIcon, 0, 0);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resLoc);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
