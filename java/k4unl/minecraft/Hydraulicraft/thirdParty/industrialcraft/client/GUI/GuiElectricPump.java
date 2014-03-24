package k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.industrialcraft.tileEntities.TileElectricPump;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiElectricPump extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyPump.png");
	private TileElectricPump pump;
	
	public GuiElectricPump(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		
		pump = (TileElectricPump)ent;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		
		drawHorizontalAlignedString(7, 3, xSize - 14, Localization.getLocalizedName(Names.blockRFPump[pump.getTier()].unlocalized), true);
		drawVerticalProgressBar(124, 16, 54, 16, pump.getEUStored(), pump.getMaxEUStorage(), Constants.COLOR_EU, "Energy Units", "EU");
				
		
		int startY = 17;
		int step = (int)(Hydraulicraft.smallGuiFont.getLineHeight() / 3.2F);
		//int step = 6;
		
		drawSmallerString(61, startY + (step * 0), EnumChatFormatting.GREEN + "Generating:", false);
		drawSmallerString(65, startY + (step * 1), EnumChatFormatting.GREEN + "" + pump.getGenerating(pump.getFacing()) + "mBar/t", false);
		drawSmallerString(61, startY + (step * 2), EnumChatFormatting.GREEN + "Max:", false);
		drawSmallerString(65, startY + (step * 3), EnumChatFormatting.GREEN + "" + pump.getMaxGenerating(pump.getFacing()) + "mBar/t", false);
		drawSmallerString(61, startY + (step * 4), EnumChatFormatting.GREEN + "Using:", false);
		drawSmallerString(65, startY + (step * 5), EnumChatFormatting.GREEN + "" + pump.getEUUsage() + "EU/t", false);
		
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}
}
