package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiRFPump extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyPump.png");
	private TileRFPump pump;
	
	public GuiRFPump(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);

		pump = (TileRFPump)ent;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
				
		drawHorizontalAlignedString(7, 3, xSize - 14, Localization.getLocalizedName(Names.blockRFPump[pump.getTier()].unlocalized), true);
		// TE drawVerticalProgressBar(124, 16, 54, 16, pump.getEnergyStored(pump.getFacing().getOpposite()), pump.getMaxEnergyStored(ForgeDirection.UNKNOWN), Constants.COLOR_RF, "Redstone Flux", "RF");
		
		int startY = 17;
		int step = (int)(Hydraulicraft.smallGuiFont.getLineHeight() / 3.2F);
		//int step = 6;
		
		drawSmallerString(61, startY + (step * 0), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_GENERATING_ENTRY) + ":", false);
		drawSmallerString(65, startY + (step * 1), EnumChatFormatting.GREEN + "" + pump.getGenerating(pump.getFacing()) + "mBar/t", false);
		drawSmallerString(61, startY + (step * 2), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_MAX_ENTRY) + ":", false);
		drawSmallerString(65, startY + (step * 3), EnumChatFormatting.GREEN + "" + pump.getMaxGenerating(pump.getFacing()) + "mBar/t", false);
		drawSmallerString(61, startY + (step * 4), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_USING_ENTRY) + ":", false);
		drawSmallerString(65, startY + (step * 5), EnumChatFormatting.GREEN + "" + pump.getRFUsage() + "RF/t", false);
		
		
		drawFluidAndPressure();
	}
}
