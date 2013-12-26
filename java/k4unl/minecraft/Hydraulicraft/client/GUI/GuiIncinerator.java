package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerIncinerator;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiIncinerator extends MachineGUI {

	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/incinerator.png");
	TileHydraulicFrictionIncinerator incinerator;
	
	
	public GuiIncinerator(InventoryPlayer invPlayer, TileHydraulicFrictionIncinerator _incinerator) {
		super(_incinerator, new ContainerIncinerator(invPlayer, _incinerator), resLoc);
		incinerator = _incinerator;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(incinerator.getInvName(), 8, 6, 0xFFFFFF);

		if(incinerator.isSmelting()){
			ItemStack smeltingItem = incinerator.getSmeltingItem();
			ItemStack targetItem = incinerator.getTargetItem();

			//Icon smeltingIcon = smeltingItem.getIconFromDamage(smeltingItem.getDamage(incinerator.getSmeltingItem()));
			
			int done = incinerator.getSmeltingTicks();
			int startX = 40;
			int maxTicks = 200;
			int targetX = 118;
			int travelPath = targetX - startX;
			float percentage = (float)done / (float)maxTicks;
			int xPos = startX + (int) (travelPath * percentage);
			//drawTexturedModelRectFromIcon(xPos, 19, smeltingIcon, w, h)
			GL11.glEnable(GL11.GL_BLEND);
			if(percentage < 0.5f){
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), smeltingItem, xPos, 19);
			}else{
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.getTextureManager(), targetItem, xPos, 19);
			}
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
		
	}
}
