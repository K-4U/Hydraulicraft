package pet.minecraft.Hydraulicraft.client.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicMixer;
import pet.minecraft.Hydraulicraft.client.containers.ContainerMixer;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class GuiMixer extends GuiContainer {
	private ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/mixer.png");
	TileHydraulicMixer mixer;
	
	
	public GuiMixer(InventoryPlayer invPlayer, TileHydraulicMixer mixer) {
		super(new ContainerMixer(invPlayer, mixer));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2){
		//TODO: Change that color
		fontRenderer.drawString(Names.blockHydraulicMixer.localized, 8, 6, 0xFFFFFF);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-96 + 2, 0xFFFFFF);
		
		if(washer.getStored() > 0){
			int color = 0xFFFFFFFF;
			if(!washer.isOilStored()){
				color = 0xFF006DD9;
			}
			
			int max = washer.getStorage();
			float perc = (float)washer.getStored() / (float)max;
			
			int xOffset = 8;
			int yOffset = 10;
			int h = 60;
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
