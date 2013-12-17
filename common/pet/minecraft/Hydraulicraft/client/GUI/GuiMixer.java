package pet.minecraft.Hydraulicraft.client.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicMixer;
import pet.minecraft.Hydraulicraft.client.containers.ContainerMixer;
import pet.minecraft.Hydraulicraft.lib.config.Constants;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class GuiMixer extends GuiContainer {
	private ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/mixer.png");
	TileHydraulicMixer mixer;
	
	
	public GuiMixer(InventoryPlayer invPlayer, TileHydraulicMixer _mixer) {
		super(new ContainerMixer(invPlayer, _mixer));
		
		mixer = _mixer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2){
		//TODO: Change that color
		fontRenderer.drawString(Names.blockHydraulicMixer.localized, 8, 6, 0xFFFFFF);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-96 + 2, 0xFFFFFF);
		
		if(mixer.getStored() > 0){
			int color = 0xFFFFFFFF;
			if(!mixer.isOilStored()){
				color = Constants.COLOR_WATER;
			}
			
			int max = mixer.getStorage();
			float perc = (float)mixer.getStored() / (float)max;
			
			int xOffset = 8;
			int yOffset = 14;
			int h = 56;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		if(mixer.getPressure() > 0){
			int color = Constants.COLOR_PRESSURE;
			
			float max = mixer.getMaxPressure();
			float perc = mixer.getPressure() / max;
			
			int xOffset = 152;
			int yOffset = 14;
			int h = 56;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		
		FluidTankInfo[] tankInfo = mixer.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = 0xFFFFFFFF;
				if(inTank.equals(FluidRegistry.WATER)){
					color = 0x7FFFFFFF;
				}
				
				int max = tankInfo[0].capacity;
				float perc = (float)tankInfo[0].fluid.amount / (float)max;
				
				int xOffset = 34;
				int yOffset = 14;
				int h = 52;
				int height = (int)(h * perc);
				//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
				drawRect(xOffset, yOffset + (h-height), xOffset + 26, yOffset + h, color);
			}
		}
		if(tankInfo[1].fluid != null){
			if(tankInfo[1].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[1].fluid.fluidID);
				int color = 0xFFFFFFFF;
				if(inTank.equals(FluidRegistry.WATER)){
					color = 0x7FFFFFFF;
				}
				
				int max = tankInfo[1].capacity;
				float perc = (float)tankInfo[1].fluid.amount / (float)max;
				
				int xOffset = 107;
				int yOffset = 14;
				int h = 52;
				int height = (int)(h * perc);
				//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
				drawRect(xOffset, yOffset + (h-height), xOffset + 26, yOffset + h, color);
			}
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
