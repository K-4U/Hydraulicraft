package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.client.GUI.HydraulicGUIBase;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiHydraulicDynamo extends HydraulicGUIBase {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/thirdPartyGen.png");
	private TileHydraulicDynamo dynamo;
	
	public GuiHydraulicDynamo(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		
		dynamo = (TileHydraulicDynamo)ent;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		
		drawHorizontalAlignedString(7, 3, xSize - 14, ThermalExpansion.blockHydraulicDynamo.getLocalizedName(), true);
//		drawVerticalProgressBar(40, 16, 54, 16, dynamo.getEnergyStored(dynamo.getFacing()), dynamo.getMaxEnergyStored(EnumFacing.UNKNOWN), Constants.COLOR_RF, "Redstone Flux", "RF");
		
		int startY = 17;
		int step = (int)(Hydraulicraft.smallGuiFont.getLineHeight() / 3.2F);
		drawSmallerString(61, startY + (step * 0), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_GENERATING_ENTRY) + ":", false);
		drawSmallerString(65, startY + (step * 1), EnumChatFormatting.GREEN + "" + dynamo.getGenerating() + "RF/t", false);
		drawSmallerString(61, startY + (step * 2), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_OUTPUT_ENTRY) + ":", false);
//		drawSmallerString(65, startY + (step * 3), EnumChatFormatting.GREEN + "" + dynamo.getInfoEnergyPerTick() + "RF/t", false);
		drawSmallerString(61, startY + (step * 4), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_MAX_ENTRY) + ":", false);
//		drawSmallerString(65, startY + (step * 5), EnumChatFormatting.GREEN + "" + dynamo.getInfoMaxEnergyPerTick() + "RF/t", false);
		drawSmallerString(61, startY + (step * 6), EnumChatFormatting.GREEN + Localization.getString(Localization.GUI_USING_ENTRY) + ":", false);
		drawSmallerString(65, startY + (step * 7), EnumChatFormatting.GREEN + "" + dynamo.getPressureRequired() + "mBar/t", false);
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}
}
