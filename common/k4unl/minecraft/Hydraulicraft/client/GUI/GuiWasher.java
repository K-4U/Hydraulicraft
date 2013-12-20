package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.containers.ContainerWasher;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

public class GuiWasher extends GuiContainer {
	private ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/washer.png");
	TileHydraulicWasher washer;
	
	
	public GuiWasher(InventoryPlayer invPlayer, TileHydraulicWasher _washer) {
		super(new ContainerWasher(invPlayer, _washer));
		washer = _washer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2){
		//TODO: Change that color
		fontRenderer.drawString(Names.blockHydraulicWasher.localized, 8, 6, 0xFFFFFF);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-96 + 2, 0xFFFFFF);
		
		if(washer.getStored() > 0){
			int color = 0xFFFFFFFF;
			if(!washer.isOilStored()){
				color = Constants.COLOR_WATER;
			}
			
			int max = washer.getStorage();
			float perc = (float)washer.getStored() / (float)max;
			
			int xOffset = 8;
			int yOffset = 14;
			int h = 58;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		
		if(washer.getPressure() > 0){
			int color = Constants.COLOR_PRESSURE;
			
			float max = washer.getMaxPressure();
			float perc = washer.getPressure() / max;
			
			int xOffset = 152;
			int yOffset = 14;
			int h = 58;
			int height = (int)(h * perc);
			//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
			drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
		}
		
		FluidTankInfo[] tankInfo = washer.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = 0xFFFFFFFF;
				color = Constants.COLOR_WATER | 0x7F000000;
				
				int max = tankInfo[0].capacity;
				float perc = (float)tankInfo[0].fluid.amount / (float)max;
				
				int xOffset = 68;
				int yOffset = 15;
				int h = 52;
				int height = (int)(h * perc);
				//drawTexturedModalRect(xOffset, yOffset, 184, 1, 18, 62);
				drawRect(xOffset, yOffset + (h-height), xOffset + 26, yOffset + h, color);
			}
		}
		
		if(washer.isWashing()){
			ItemStack washingItem = washer.getWashingItem();
			ItemStack targetItem = washer.getTargetItem();
	
			//Icon smeltingIcon = smeltingItem.getIconFromDamage(smeltingItem.getDamage(incinerator.getSmeltingItem()));
			
			int done = washer.getWashingTicks();
			int startY = 15;
			int startX = 47;
			int maxTicks = 200;
			int targetX = 97;
			int targetY = 0;
			float percentage = (float)done / (float)maxTicks;
			int travelPath = 0;
			int xPos = startX;
			int yPos = startY;
			
			if(percentage < 0.25F){
				targetX = 73;
				travelPath = (targetX - startX) * 4;
				xPos = startX + (int) (travelPath * percentage);
			}else if(percentage < 0.75F){
				xPos = 73;
				targetY = 50;
				travelPath = targetY - startY;
				yPos = startY + (int) (travelPath * ((percentage-0.25F)*2));
			}else{
				yPos = 50;
				targetX = 97;
				startX = 73;
				travelPath = targetX - startX;
				xPos = startX + (int) (travelPath * (percentage-0.75F)*4);
			}
			
			
			
			
			//drawTexturedModelRectFromIcon(xPos, 19, smeltingIcon, w, h)
			GL11.glEnable(GL11.GL_BLEND);
			
			
			if(percentage < 0.5f){
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), washingItem, xPos, yPos);
			}else{
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), targetItem, xPos, yPos);
			}
			GL11.glDisable(GL11.GL_BLEND);
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
