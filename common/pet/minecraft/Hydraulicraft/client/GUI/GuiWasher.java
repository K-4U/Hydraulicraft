package pet.minecraft.Hydraulicraft.client.GUI;

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

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicWasher;
import pet.minecraft.Hydraulicraft.client.containers.ContainerWasher;
import pet.minecraft.Hydraulicraft.lib.config.Constants;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

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
				drawRect(xOffset, yOffset + (h-height), xOffset + 16, yOffset + h, color);
			}
		}
		
		if(washer.isWashing()){
			ItemStack washingItem = washer.getWashingItem();
			ItemStack targetItem = washer.getTargetItem();
	
			//Icon smeltingIcon = smeltingItem.getIconFromDamage(smeltingItem.getDamage(incinerator.getSmeltingItem()));
			
			int done = washer.getWashingTicks();
			int startX = 40;
			int maxTicks = 200;
			int targetX = 118;
			int travelPath = targetX - startX;
			float percentage = (float)done / (float)maxTicks;
			int xPos = startX + (int) (travelPath * percentage);
			//drawTexturedModelRectFromIcon(xPos, 19, smeltingIcon, w, h)
			GL11.glEnable(GL11.GL_BLEND);
			if(percentage < 0.5f){
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), washingItem, xPos, 19);
			}else{
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), targetItem, xPos, 19);
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
