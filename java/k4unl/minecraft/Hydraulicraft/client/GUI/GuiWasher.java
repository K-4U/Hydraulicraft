package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerWasher;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
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
		fontRenderer.drawString(Names.blockHydraulicWasher.localized, 8, 6, 0xFFFFFF);
		
		FluidTankInfo[] tankInfo = washer.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = 0xFFFFFFFF;
				color = Constants.COLOR_WATER | 0x7F000000;
				
				
				drawVerticalProgressBar(68, 15, 52, 26, tankInfo[0].fluid.amount, tankInfo[0].capacity, color, "Water:", "mB");
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
		
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}
}
