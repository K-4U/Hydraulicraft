package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerWasher;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

public class GuiWasher extends MachineGUI {
	private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/washer.png");
	TileHydraulicWasher washer;
	
	
	public GuiWasher(InventoryPlayer invPlayer, TileHydraulicWasher _washer) {
		super(_washer, new ContainerWasher(invPlayer, _washer), resLoc);
		washer = _washer;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRenderer.drawString(Names.blockHydraulicWasher.localized, 8, 6, 0xFFFFFF);
		drawHorizontalAlignedString(7, 3, xSize-14, HCBlocks.hydraulicWasher.getLocalizedName(), true);
		
		drawFluidAndPressure();
		
		FluidTankInfo[] tankInfo = washer.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = 0xFFFFFFFF;
				color = Constants.COLOR_WATER | 0x7F000000;
				
				
				drawVerticalProgressBar(76, 16, 56, 26, tankInfo[0].fluid.amount, tankInfo[0].capacity, color, "Water:", "mB");
			}
		}
		
		if(washer.isWashing()){
			ItemStack washingItem = washer.getWashingItem();
			ItemStack targetItem = washer.getTargetItem();
	
			//Icon smeltingIcon = smeltingItem.getIconFromDamage(smeltingItem.getDamage(incinerator.getSmeltingItem()));
			
			int done = washer.getWashingTicks();
			int startY = 17;
			int startX = 56;
			int maxTicks = 200;
			int targetX = 106;
			int targetY = 0;
			float percentage = (float)done / (float)maxTicks;
			int travelPath = 0;
			int xPos = startX;
			int yPos = startY;
			
			if(percentage < 0.25F){
				targetX = 80;
				travelPath = (targetX - startX) * 4;
				xPos = startX + (int) (travelPath * percentage);
			}else if(percentage < 0.75F){
				xPos = 80;
				targetY = 56;
				travelPath = targetY - startY;
				yPos = startY + (int) (travelPath * ((percentage-0.25F)*2));
			}else{
				yPos = 56;
				targetX = 106;
				startX = 80;
				travelPath = targetX - startX;
				xPos = startX + (int) (travelPath * (percentage-0.75F)*4);
			}
			
			IconRenderer.drawMergedIcon(xPos, yPos, zLevel, washingItem, targetItem, percentage, true);
			
			//drawTexturedModelRectFromIcon(xPos, 19, smeltingIcon, w, h)
			/*GL11.glEnable(GL11.GL_BLEND);
			if(percentage < 0.5f){
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), washingItem, xPos, yPos);
			}else{
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), targetItem, xPos, yPos);
			}*/
			GL11.glDisable(GL11.GL_BLEND);
		}
		checkTooltips(mouseX, mouseY);
	}
}
