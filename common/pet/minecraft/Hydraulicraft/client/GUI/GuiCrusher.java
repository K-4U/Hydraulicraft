package pet.minecraft.Hydraulicraft.client.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicCrusher;
import pet.minecraft.Hydraulicraft.client.containers.ContainerCrusher;
import pet.minecraft.Hydraulicraft.lib.config.Constants;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class GuiCrusher extends GuiContainer {
	private ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/crusher.png");
	TileHydraulicCrusher crusher;
	
	
	public GuiCrusher(InventoryPlayer invPlayer, TileHydraulicCrusher _crusher) {
		super(new ContainerCrusher(invPlayer, _crusher));
		crusher = _crusher;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2){
		//TODO: Change that color
		fontRenderer.drawString(Names.blockHydraulicCrusher.localized, 8, 6, 0xFFFFFF);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-96 + 2, 0xFFFFFF);
		
		if(crusher.getStored() > 0){
			int color = 0xFFFFFFFF;
			if(!crusher.isOilStored()){
				color = Constants.COLOR_WATER;
			}
			
			int max = crusher.getStorage();
			float perc = (float)crusher.getStored() / (float)max;
			
			int xOffset = 8;
			int yOffset = 14;
			int h = 56;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		if(crusher.getPressure() > 0){
			int color = Constants.COLOR_PRESSURE;
			
			float max = crusher.getMaxPressure();
			float perc = crusher.getPressure() / max;
			
			int xOffset = 152;
			int yOffset = 14;
			int h = 56;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
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
